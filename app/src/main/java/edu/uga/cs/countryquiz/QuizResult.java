package edu.uga.cs.countryquiz;

public class QuizResult {
    private int quizId;
    private int score;
    private String date;


    public QuizResult(int quizId, int score, String date) {
        this.quizId = quizId;
        this.score = score;
        this.date = date;
    }


    public int getQuizId() {
        return quizId;
    }


    public int getScore() {
        return score;
    }


    public String getDate() {
        return date;
    }
}
