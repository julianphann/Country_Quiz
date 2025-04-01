package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


public class QuizScreen extends AppCompatActivity {
    private QuizLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        layout = new ViewModelProvider(this).get(QuizLayout.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, QuizFragment.newInstance(0)).commit();

        }
    }
}
