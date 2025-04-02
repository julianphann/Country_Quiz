package edu.uga.cs.countryquiz;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<QuizResult> {


    public ResultAdapter(Context context, List<QuizResult> results) {
        super(context, 0, results);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_item, parent, false);
        }


        QuizResult result = getItem(position);


        TextView quizIdTextView = convertView.findViewById(R.id.quizIdTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);


        quizIdTextView.setText("Quiz ID: " + result.getQuizId());
        scoreTextView.setText("Score: " + result.getScore());
        dateTextView.setText("Date: " + result.getDate());


        return convertView;
    }
}
