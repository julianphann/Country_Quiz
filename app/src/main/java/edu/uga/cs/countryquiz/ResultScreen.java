package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The ResultScreen activity displays past quiz results to the user.
 */
public class ResultScreen extends AppCompatActivity {
    private TextView pastResults;

    /**
     * Called when the activity is first created. Initializes the user interface
     * and loads past quiz results from the database.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        pastResults = findViewById(R.id.result_history);
        loadResults(); // Load results properly

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultScreen.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Loads past quiz results from the database and displays them in the TextView.
     * Uses a background thread to perform the database operation and a Handler
     * to update the UI on the main thread.
     */
    private void loadResults() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(ResultScreen.this);
            StringBuilder resultBuilder = new StringBuilder();

            for (String result : dbHelper.getAllQuizResults()) {
                resultBuilder.append(result).append("\n");
            }

            String finalResult = resultBuilder.toString().isEmpty() ? "No past results yet." : resultBuilder.toString();

            handler.post(() -> pastResults.setText(finalResult));
        });
    }
}
