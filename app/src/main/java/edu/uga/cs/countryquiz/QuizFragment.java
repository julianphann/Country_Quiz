package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
/**
 * A Fragment to show the Quiz screen where users answer quiz questions.
 */
public class QuizFragment extends Fragment {

    private QuizLayout quizLayout;
    /**
     * Called when the fragment is first created.  Here, we initialize some
     * common things such as the menu.
     * @param savedInstanceState State used when fragment is recreated from prev state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object used to inflate views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }
    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle)
     * has returned, but before any saved state has been restored in to the view. The fragment's
     * view hierarchy is not attached to its parent yet
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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
        viewPager.setOffscreenPageLimit(1);

        // Log page changes (optional)
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /**
             * This method will be invoked when user slides to a new page.
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                System.out.println("Page selected: " + position);
            }
        });
    }
/**
 * Initialize the contents of the Activity's standard options menu.
 * */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_quiz, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
/**
 * Called whenever an item in your options menu is selected.
 * @param item The menu item that was selected.
 *
 *@return boolean Return false to allow normal menu processing to
 */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            // Exit the quiz screen
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
