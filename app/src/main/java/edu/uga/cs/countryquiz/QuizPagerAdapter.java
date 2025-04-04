package edu.uga.cs.countryquiz;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Adapter for managing quiz question fragments and the result fragment in a ViewPager2.
 * This adapter determines which fragment to display based on the current position.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {

    private final QuizLayout quizLayout;

    /**
     * Constructor for QuizPagerAdapter.
     *
     * @param fragmentManager The FragmentManager for managing fragments.
     * @param lifecycle       The lifecycle of the ViewPager2.
     * @param layout          The QuizLayout containing quiz data.
     */
    public QuizPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, QuizLayout layout) {
        super(fragmentManager, lifecycle);
        this.quizLayout = layout;
    }

    /**
     * Creates a fragment based on the position in the ViewPager2.
     *
     * @param position The position of the fragment to create.
     * @return A new instance of QuizQuestionFragment for quiz questions or ResultFragment for results.
     */
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

    /**
     * Returns the total number of fragments managed by this adapter.
     *
     * @return The total number of fragments, including one additional fragment for results.
     */
    @Override
    public int getItemCount() {
        return quizLayout.getQuestions().size() + 1;
    }
}
