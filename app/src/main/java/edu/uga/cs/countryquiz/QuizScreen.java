package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class QuizScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizFragment()).commit();

        }
    }
}
