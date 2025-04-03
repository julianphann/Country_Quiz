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

public class ResultScreen extends AppCompatActivity {
    private TextView pastResults;

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
