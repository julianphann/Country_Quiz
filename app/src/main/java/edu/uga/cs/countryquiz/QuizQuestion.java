package edu.uga.cs.countryquiz;

import java.util.List;

/**
 * This class represents a single quiz question.
 */
public class QuizQuestion {
    private String questionContent;
    private List<String> choices;
    private int correctAnswerNum;

    /**
     * Constructor for creating a QuizQuestion object.
     * @param questionContent The text of the question.
     * @param choices A list of answer choices.
     * @param correctAnswerNum The index of the correct answer in the choices list.
     */
    public QuizQuestion(String questionContent, List<String> choices, int correctAnswerNum) {
        this.questionContent = questionContent;
        this.choices = choices;
        this.correctAnswerNum = correctAnswerNum;
    }

    /**
     * Returns the content of the question.
     * @return The question content.
     */
    public String getQuestionContent() {
        return questionContent;
    }

    /**
     * Returns the list of answer choices.
     * @return The list of answer choices.
     */
    public List<String> getChoices() {
        return choices;
    }

    /**
     * Returns the index of the correct answer.
     * @return The index of the correct answer.
     */
    public int getCorrectAnswerNum() {
        return correctAnswerNum;
    }
}
