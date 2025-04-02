package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class QuizFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "question_index";
    private int questionIndex;
    private static QuizLayout quizLayout;

    public static QuizFragment newInstance(int questionIndex) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize QuizLayout ViewModel
        quizLayout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Retrieve question index from arguments
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX, 0);
        }

        // Check if the quiz is complete
        if (questionIndex >= quizLayout.getQuestions().size()) {
            showResults(view);
            return;
        }

        // Get the current question
        QuizQuestion question = quizLayout.getQuestions().get(questionIndex);

        // Set up question text
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestionContent());

        // Set up RadioGroup and RadioButtons for options
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_options);
        List<String> options = question.getChoices();

        // Dynamically set text for each RadioButton
        ((RadioButton) radioGroup.getChildAt(0)).setText(options.get(0));
        ((RadioButton) radioGroup.getChildAt(1)).setText(options.get(1));
        ((RadioButton) radioGroup.getChildAt(2)).setText(options.get(2));
        ((RadioButton) radioGroup.getChildAt(3)).setText(options.get(3));

        // Add listener to handle answer selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedOption = radioGroup.indexOfChild(view.findViewById(checkedId));

            // Check if the selected option is correct
            if (selectedOption == question.getCorrectAnswerNum()) {
                quizLayout.updateScore(); // Increment score for correct answer
            }

            // Move to the next question after a short delay
            view.postDelayed(() -> {
                ViewPager2 viewPager = requireActivity().findViewById(R.id.viewpager);
                viewPager.setCurrentItem(questionIndex + 1, true); // Navigate to next page
            }, 500); // Delay to allow user to see their selection
        });

        // Display current question number out of total questions
        TextView questionCounter = view.findViewById(R.id.question_counter);
        questionCounter.setText("Question " + (questionIndex + 1) + " of " + quizLayout.getQuestions().size());
    }

    private void showResults(View view) {
        int score = quizLayout.getScore().getValue() != null ? quizLayout.getScore().getValue() : 0;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(requireContext());
        databaseHelper.insertQuizResult(score);

        // Navigate to ResultFragment to display the final score
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ResultFragment())
                .addToBackStack(null)
                .commit();
    }

    public static int getNumberOfVersions() {
        if (quizLayout != null) {
            return quizLayout.getQuestions().size();
        }
        return 0; // Return 0 if quizLayout is not initialized
    }
}
