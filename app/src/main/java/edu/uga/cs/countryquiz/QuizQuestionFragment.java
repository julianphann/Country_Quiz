package edu.uga.cs.countryquiz;

import android.os.Bundle;
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

public class QuizQuestionFragment extends Fragment {
    private static final String ARG_QUESTION_INDEX = "question_index";
    private int questionIndex;
    private QuizLayout quizLayout;

    public static QuizQuestionFragment newInstance(int questionIndex) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_INDEX, questionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        quizLayout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Get question index from arguments
        if (getArguments() != null) {
            questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);
        }

        // Load current question
        QuizQuestion question = quizLayout.getQuestions().get(questionIndex);

        // Set up UI components
        TextView questionText = view.findViewById(R.id.question_text);
        questionText.setText(question.getQuestionContent());

        RadioGroup radioGroup = view.findViewById(R.id.radio_group_options);
        ((RadioButton) radioGroup.getChildAt(0)).setText(question.getChoices().get(0));
        ((RadioButton) radioGroup.getChildAt(1)).setText(question.getChoices().get(1));
        ((RadioButton) radioGroup.getChildAt(2)).setText(question.getChoices().get(2));
        ((RadioButton) radioGroup.getChildAt(3)).setText(question.getChoices().get(3));

        // Handle answer selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedOption = radioGroup.indexOfChild(view.findViewById(checkedId));

            // Check if the selected option is correct
            if (selectedOption == question.getCorrectAnswerNum()) {
                quizLayout.updateScore();
            }

            // Remove the automatic navigation to the next page
            // The user will now manually slide to the next question
            //view.postDelayed(() -> {
            //    ViewPager2 viewPager = requireActivity().findViewById(R.id.viewpager);
            //    viewPager.setCurrentItem(questionIndex + 1, true); // Navigate to next page
            //}, 500); // Delay for smooth transition
        });
    }
}


