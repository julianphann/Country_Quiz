package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;


public class QuizFragment extends Fragment {
    private QuizLayout layout;
    private int questionIndex;

    private static final String ARG_QUESTION_INDEX = "question_index";

    public static QuizFragment newInstance(int questionIndex) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    RadioGroup radioGroup;
    RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Check if we've reached the end of the quiz
        if (layout.isQuizComplete()) {
            showResults();
            return;
        }

        QuizQuestion question = layout.getQuestions().get(questionIndex);

        // Set up the question text
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestionContent());

        // Set up the answer buttons
        Button option1 = view.findViewById(R.id.radio_button_option1);
        Button option2 = view.findViewById(R.id.radio_button_option2);
        Button option3 = view.findViewById(R.id.radio_button_option3);
        Button option4 = view.findViewById(R.id.radio_button_option4);

        List<Button> optionButtons = Arrays.asList(option1, option2, option3, option4);

        // Set the text for each option button
        List<String> options = question.getChoices();
        for (int i = 0; i < options.size(); i++) {
            optionButtons.get(i).setText(options.get(i));

            final int optionIndex = i;
            optionButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (optionIndex >= 0 && optionIndex < options.size() &&
                            optionIndex == question.getCorrectAnswerNum()) {
                        layout.updateScore();
                    }


                    // Move to next question
                    layout.nextQuestion();

                    if (!layout.isQuizComplete()) {
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        QuizFragment.newInstance(questionIndex + 1)) // Pass updated index
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Show results
                        showResults();
                    }
                }
            });
        }

        // Display current question number out of total
        TextView questionCounter = view.findViewById(R.id.question_counter);
        questionCounter.setText("Question " + (questionIndex + 1) + "/" +
                layout.getQuestions().size());
    }

    private void showResults() {
       DatabaseHelper databaseHelper = DatabaseHelper.getInstance(requireContext());
       int score = layout.getScore().getValue() != null ? layout.getScore().getValue() : 0;
       databaseHelper.insertQuizResult(score);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ResultFragment())
                .addToBackStack(null) // Ensure back button doesn't take user back to the quiz
                .commit();

    }
}