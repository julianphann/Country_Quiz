package edu.uga.cs.countryquiz;

import android.util.Log;

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
            Log.d("QuizPagerAdapter", "Creating QuizQuestionFragment at position: " + position);
            return QuizQuestionFragment.newInstance(position);
        } else {
            Log.d("QuizPagerAdapter", "Creating ResultFragment");
            return new ResultFragment();
        }
    }

    @Override
    public int getItemCount() {
        return quizLayout.getQuestions().size() + 1;
    }
}
