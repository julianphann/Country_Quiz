package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizPagerAdapter extends FragmentStateAdapter {

    private final QuizLayout quizLayout;

    public QuizPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, QuizLayout layout) {
        super(fragmentManager, lifecycle);
        this.quizLayout = layout;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < quizLayout.getQuestions().size()) {
            return QuizQuestionFragment.newInstance(position); // Load question fragment
        } else {
            return new ResultFragment(); // Load results fragment at the end
        }
    }

    @Override
    public int getItemCount() {
        return quizLayout.getQuestions().size() + 1; // Include results page
    }
}
