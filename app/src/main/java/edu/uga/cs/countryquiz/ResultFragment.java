package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class ResultFragment extends Fragment {
    private QuizLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);
        TextView resultText = view.findViewById(R.id.result_text);

        int score = layout.getScore().getValue() != null ? layout.getScore().getValue() : 0;
        int totalQuestions = layout.getQuestions().size();
        resultText.setText("Your final score is " + score + " out of " + totalQuestions);


        DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
        dbHelper.insertQuizResult(score);
        Log.d("ResultFragment", "Saved final score: " + score);

        Button restartBtn = view.findViewById(R.id.restart_button);
        restartBtn.setOnClickListener(v -> {
            layout.startNewQuiz();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuizFragment())
                    .commit();
        });

        Button viewResultsBtn = view.findViewById(R.id.view_all_results_button);
        viewResultsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ResultScreen.class);
            startActivity(intent);
        });

        Button homeButton = view.findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }



}
