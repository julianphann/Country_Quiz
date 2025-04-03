package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * QuizPagerAdapter is a FragmentStateAdapter responsible for managing the fragments
 * (QuizQuestionFragment and ResultFragment) in the ViewPager2.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {

    private final QuizLayout quizLayout;

    /**
     * Constructor for QuizPagerAdapter.
     * @param fragmentManager The FragmentManager for managing the fragments.
     * @param lifecycle The Lifecycle of the fragment.
     * @param layout The QuizLayout ViewModel.
     */
    public QuizPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, QuizLayout layout) {
        super(fragmentManager, lifecycle);
        this.quizLayout = layout;
    }

    /**
     * Called when a new fragment needs to be created.
     * Creates either a QuizQuestionFragment for a question or a ResultFragment
     * when all questions have been answered.
     * @param position The position of the fragment to create.
     * @return The Fragment to display at the given position.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < quizLayout.getQuestions().size()) {
            return QuizQuestionFragment.newInstance(position); // Load question fragment
        } else {
            return new ResultFragment(); // Load results fragment at the end
        }
    }

    /**
     * Returns the total number of fragments to display.
     * Includes one extra fragment for the results page.
     * @return The total number of fragments.
     */
    @Override
    public int getItemCount() {
        return quizLayout.getQuestions().size() + 1; // Include results page
    }
}
