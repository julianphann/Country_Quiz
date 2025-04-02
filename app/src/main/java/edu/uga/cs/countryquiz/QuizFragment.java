package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class QuizFragment extends Fragment {

    private QuizLayout quizLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        quizLayout = new ViewModelProvider(requireActivity()).get(QuizLayout.class);

        // Set up ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        QuizPagerAdapter adapter = new QuizPagerAdapter(getChildFragmentManager(), getLifecycle(), quizLayout);
        viewPager.setAdapter(adapter);

        // Optional: Improve performance by keeping adjacent pages in memory
        viewPager.setOffscreenPageLimit(3);

        // Log page changes (optional)
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                System.out.println("Page selected: " + position);
            }
        });
    }
}

