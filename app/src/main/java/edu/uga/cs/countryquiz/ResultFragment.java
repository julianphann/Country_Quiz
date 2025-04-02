package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class ResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QuizLayout layout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        TextView resultText = view.findViewById(R.id.result_text);

        int score = layout.getScore().getValue() != null ? layout.getScore().getValue() : 0;

        resultText.setText("Your final score is " + score + " out of " + layout.getQuestions().size());

        view.findViewById(R.id.restart_button).setOnClickListener(v -> {
            layout.startNewQuiz();

            ViewPager2 viewPager = requireActivity().findViewById(R.id.viewpager);

            viewPager.setCurrentItem(0); // Restart from first question
        });
    }
}
