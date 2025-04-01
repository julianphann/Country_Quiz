package edu.uga.cs.countryquiz;
import java.util.List;

public class QuizQuestion {
    private String questionContent;
    private List<String> choices;
    private int correctAnswer;

    public QuizQuestion(String questionContent, List<String> choices, int correctAnswer) {
        this.questionContent = questionContent;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectAnswerNum() {
        return correctAnswer;
    }
}