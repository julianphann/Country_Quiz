package edu.uga.cs.countryquiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizPagerAdapter extends FragmentStateAdapter {

    public QuizPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < QuizFragment.getNumberOfVersions()) {
            return QuizFragment.newInstance(position); // Load question fragment
        } else {
            return new ResultFragment(); // Load results fragment at the end
        }
    }

    @Override
    public int getItemCount() {
        return QuizFragment.getNumberOfVersions() + 1; // Include results page
    }
}


