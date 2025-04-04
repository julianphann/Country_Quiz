package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * QuizQuestionFragment is a Fragment that displays a single quiz question
 * and handles user interaction for answering the question.
 */
public class QuizQuestionFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "question_index";
    private int questionIndex;
    private QuizLayout quizLayout;

    /**
     * Creates a new instance of QuizQuestionFragment with the given question index.
     * @param questionIndex The index of the question to display.
     * @return A new instance of QuizQuestionFragment.
     */
    public static QuizQuestionFragment newInstance(int questionIndex) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to create the view for the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_question, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     * Initializes the ViewModel, retrieves the question, and sets up the UI.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        quizLayout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Observe changes to the score and update the UI automatically
        TextView scoreText = view.findViewById(R.id.score_text);
        quizLayout.getScore().observe(getViewLifecycleOwner(), newScore -> {
            if (scoreText != null) {
                scoreText.setText("Score: " + newScore);
            }
        });

        // Get question index from arguments
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
        }

        // Load current question
        QuizQuestion question = quizLayout.getQuestions().get(questionIndex);

        // Set question number dynamically
        TextView questionNumberText = view.findViewById(R.id.question_number);
        questionNumberText.setText("Question: " + (questionIndex + 1) + "/" + quizLayout.getQuestions().size());

        // Set question text
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestionContent());

        // Set answer choices dynamically
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_options);
        ((RadioButton) radioGroup.getChildAt(0)).setText("A. " + question.getChoices().get(0));
        ((RadioButton) radioGroup.getChildAt(1)).setText("B. " + question.getChoices().get(1));
        ((RadioButton) radioGroup.getChildAt(2)).setText("C. " + question.getChoices().get(2));
        radioGroup.clearCheck(); // Clear any previous selection

        // Handle answer selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = radioGroup.findViewById(checkedId);
            int selectedOption = radioGroup.indexOfChild(radioButton);

            // Check if the selected option is correct
            if (selectedOption == question.getCorrectAnswerNum()) {
                quizLayout.updateScore();  // Update score directly when correct answer is selected
                Log.d("QuizQuestionFragment", "Correct answer selected! Score updated.");
            } else {
                Log.d("QuizQuestionFragment", "Incorrect answer selected.");
            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Re-enable RadioButtons
        RadioGroup radioGroup = getView().findViewById(R.id.radio_group_options);
        if (radioGroup != null) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setEnabled(true);
            }
        }
    }
}
