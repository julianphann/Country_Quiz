package edu.uga.cs.countryquiz;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * QuizLayout is a ViewModel class that manages the data and logic for the quiz.
 * It handles the score, question list, current question number,
 * database interaction, and overall quiz flow.
 */
public class QuizLayout extends AndroidViewModel {

    private MutableLiveData<Integer> score = new MutableLiveData<>(0);
    /**
     * Returns the LiveData for the current score.
     * @return LiveData<Integer> - The current score.
     */
    public LiveData<Integer> getScore() {
        return score;
    }
    private MutableLiveData<Integer> currentQuestionNum = new MutableLiveData<>(0);
    /**
     * Returns the LiveData for the current question number.
     * @return LiveData<Integer> - The current question number.
     */
    public LiveData<Integer> getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    private List<QuizQuestion> questions;
    private Map<String, String> countryContinentResults;

    private DatabaseHelper databaseHelper;
    /**
     * Constructor for QuizLayout. Initializes the database helper,
     * retrieves random country-continent pairs, and creates the quiz questions.
     * @param application The application context.
     */
    public QuizLayout(Application application) {
        super(application);

        databaseHelper = DatabaseHelper.getInstance(application);
        countryContinentResults = databaseHelper.getRandomPairs(6);
        questions = new ArrayList<>();
        createCountryQuiz();

    }
    /**
     * Creates the country quiz by fetching random countries and their corresponding
     * continents, then generating a list of QuizQuestion objects.
     */
    private void createCountryQuiz() {
        String[] continents = {"Africa", "Antarctica", "Asia", "Oceania", "Europe", "North America", "South America"};
        Random random = new Random();

        Iterator<Map.Entry<String, String>> iterator = countryContinentResults.entrySet().iterator();
        while (iterator.hasNext() && questions.size() < 6) {
            Map.Entry<String, String> entry = iterator.next();
            String country = entry.getKey();
            String correctAnswer = entry.getValue();

            List<String> choices = new ArrayList<>();
            choices.add(correctAnswer);

            while (choices.size() < 3 ) {
                String randomContinent = continents[random.nextInt(continents.length)];
                if (!randomContinent.equals(correctAnswer) && !choices.contains(randomContinent)) {
                    choices.add(randomContinent);
                }
            }
            java.util.Collections.shuffle(choices);

            int getCorrectAnswerNum = choices.indexOf(correctAnswer);

            QuizQuestion question = new QuizQuestion(
                    "In which continent is " + country + " located?",
                    choices,
                    getCorrectAnswerNum
            );
            questions.add(question);
        }
    }
    /**
     * Returns the list of quiz questions.
     * @return List<QuizQuestion> - The list of quiz questions.
     */
    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    /**
     * Updates the score when the answer is correct.
     */
    public void updateScore() {
        if (score.getValue() != null) {
            score.setValue(score.getValue() + 1);
        } else {
            score.setValue(1);
        }
    }


    /**
     * Checks if the quiz is complete.
     * @return boolean - True if the quiz is complete, false otherwise.
     */
    public boolean isQuizComplete() {
        Integer currentNum = currentQuestionNum.getValue();
        return (currentNum != null && currentNum >= questions.size());
    }

    /**
     * Starts a new quiz by resetting the score and current question number,
     * fetching new country-continent pairs, and recreating the quiz questions.
     */
    public void startNewQuiz() {
        score.setValue(0); // Reset score
        currentQuestionNum.setValue(0); // Reset question index

        countryContinentResults = databaseHelper.getRandomPairs(6);
        if (countryContinentResults == null || countryContinentResults.isEmpty()) {
            Log.e("QuizLayout", "Not enough country-continent data available!");
            return;
        }

        questions.clear();
        createCountryQuiz();
    }

}
