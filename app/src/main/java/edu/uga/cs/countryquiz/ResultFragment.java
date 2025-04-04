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

/**
 * Fragment to display the final quiz results and provide options to restart, view all results, or return home.
 */
public class ResultFragment extends Fragment {

    private QuizLayout layout; // ViewModel containing quiz data and score tracking
    private boolean isDataSaved = false; // Tracks whether the score has been saved to the database

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The root view of the inflated layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    /**
     * Called after the view has been created. Sets up UI components and handles button actions.
     *
     * Note: There is an issue where this fragment may be called too early before all quiz data is finalized,
     * but the score is tracked correctly due to LiveData updates from QuizLayout.
     *
     * @param view               The root view of this fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel containing quiz data
        layout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Display final score
        TextView resultText = view.findViewById(R.id.result_text);
        layout.getScore().observe(getViewLifecycleOwner(), score -> {
            int totalQuestions = layout.getQuestions().size();
            resultText.setText("Your final score is " + score + " out of " + totalQuestions);
            Log.d("ResultFragment", "Final score displayed: " + layout.getScore().getValue());

            // Save score to database if not already saved
            if (!isDataSaved) {
                DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
                dbHelper.insertQuizResult(score);
                isDataSaved = true; // Prevent duplicate saves
            }
        });

        // Restart quiz button functionality
        Button restartBtn = view.findViewById(R.id.restart_button);
        restartBtn.setOnClickListener(v -> {
            layout.startNewQuiz();
            isDataSaved = false; // Reset save state for new quiz
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuizFragment())
                    .commit();
        });

        // View all results button functionality
        Button viewResultsBtn = view.findViewById(R.id.view_all_results_button);
        viewResultsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ResultScreen.class);
            startActivity(intent);
        });

        // Home button functionality
        Button homeButton = view.findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish(); // Close current activity stack
        });
    }
}
