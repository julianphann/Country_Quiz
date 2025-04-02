/**
 *
 * @author Julian Phan and Mary Nguyen
 * @teacher Dr. Krzysztof J. Kochut
 * @class CSCI 4060
 */
package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ReadCSVTask.OnReadCSVCompleteListener {
    private static final String TAG = "MainActivity";
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Load activity_main.xml

        Button startQuizBtn = findViewById(R.id.startQuizBtn);
        Button resultsBtn = findViewById(R.id.resultsBtn);

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizScreen.class);
                startActivity(intent);
            }
        });

        resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Make a ResultScreen
                //Intent intent = new Intent(MainActivity.this, ResultScreen.class);
                //startActivity(intent);
                //TODO
            }
        });

        initializeDatabase();
    }


    private void initializeDatabase() {
        databaseHelper = DatabaseHelper.getInstance(this);
        boolean databaseExists = this.getDatabasePath("CountriesDB").exists();
        if (!databaseExists || !databaseHelper.isDataLoaded()) {
            Log.d(TAG, "Database doesn't exist or there is no active session, initializing...");
            new ReadCSVTask(this, this).execute();
        } else {
            Log.d(TAG, "In session");
            onDatabaseReady();
        }
    }

    @Override
    public void onReadCSVComplete(boolean success) {
        if (success) {
            Log.d(TAG, "Database started and running as expected.");
            Toast.makeText(this, "Database started and running as expected.", Toast.LENGTH_SHORT).show();
            onDatabaseReady();
        } else {
            Log.e(TAG, "Failed to initialize database");
            Toast.makeText(this, "Error initializing database", Toast.LENGTH_LONG).show();
        }
    }

    private void onDatabaseReady() {
        // Continue with app initialization that requires database access
    }
}
