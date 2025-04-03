package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The QuizScreen activity that hosts the QuizFragment.
 * This activity manages the display of the quiz interface.
 */
public class QuizScreen extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Initializes the layout and loads the QuizFragment into the fragment container.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizFragment()).commit();

        }
    }
}
