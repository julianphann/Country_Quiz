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


/**
 * The main activity of the Country Quiz app.
 * Entry point to the app and allows users to
 * start a quiz or view previous results.
 */
public class MainActivity extends AppCompatActivity implements ReadCSVTask.OnReadCSVCompleteListener {
    private static final String TAG = "MainActivity";
    private DatabaseHelper databaseHelper;

    /**
     * Called when the activity is first created.
     * Initializes the user interface and sets up button click listeners.
     * Also initializes the database if it doesn't exist or if it's the first time the app is run.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Load activity_main.xml

        Button startQuizBtn = findViewById(R.id.startQuizBtn);
        Button resultsBtn = findViewById(R.id.resultsBtn);

        // Start Quiz button functionality
        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizScreen.class);
                startActivity(intent);
            }
        });

        /**
         * Called when the Start Quiz button is clicked.
         * Navigates the user to the QuizScreen to begin a new quiz.
         * @param view The view that was clicked.
         */
        resultsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResultScreen.class);
                startActivity(intent);
            }
        });

        initializeDatabase();
    }
    /**
     * Initializes the SQLite database used to store country and quiz data.
     * Checks if the database exists or contains any data. If not, it populates the database
     * by reading from a CSV file using an asynchronous task.
     */
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
    /**
     * Callback method to handle the completion of the ReadCSVTask.
     * Called when CSV file has been successfully read and the database has been populated.
     * Displays a toast message to indicate success or failure.
     * @param success True if the CSV file was read successfully, false otherwise.
     */
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
    /**
     * Called when the database is ready for use.
     * This method is called after the database has been initialized and populated, either by
     * reading from the CSV file or by loading from an existing database.
     */
    private void onDatabaseReady() {
        // Continue with app initialization that requires database access
    }
}
