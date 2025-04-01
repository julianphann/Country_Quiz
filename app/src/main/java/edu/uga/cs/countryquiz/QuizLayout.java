package edu.uga.cs.countryquiz;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

public class QuizLayout extends AndroidViewModel {

    private MutableLiveData<Integer> score = new MutableLiveData<>(0);
    public LiveData<Integer> getScore() {
        return score;
    }
    private MutableLiveData<Integer> currentQuestionNum = new MutableLiveData<>(0);
    public LiveData<Integer> getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    private List<QuizQuestion> questions;
    private Map<String, String> countryContinentResults;

    private DatabaseHelper databaseHelper;

    public QuizLayout(Application application) {
        super(application);

        databaseHelper = DatabaseHelper.getInstance(application);
        countryContinentResults = databaseHelper.getRandomPairs(6);
        questions = new ArrayList<>();
        createCountryQuiz();

    }

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

            while (choices.size() < 4 ) {
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
    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    // Navigate to next question
    public void nextQuestion() {
        if (currentQuestionNum.getValue() != null) {
            currentQuestionNum.setValue(currentQuestionNum.getValue() + 1);
        } else {
            currentQuestionNum.setValue(1);
        }
    }


    // Update score when answer is correct
    public void updateScore() {
        if (score.getValue() != null) {
            score.setValue(score.getValue() + 1);
        } else {
            score.setValue(1);
        }
    }


    // Check if the quiz is complete
    public boolean isQuizComplete() {
        Integer currentNum = currentQuestionNum.getValue();
        return (currentNum != null && currentNum >= questions.size());
    }


    public void startNewQuiz() {
        score.setValue(0);
        currentQuestionNum.setValue(0);

        countryContinentResults = databaseHelper.getRandomPairs(6);
        if (countryContinentResults == null || countryContinentResults.isEmpty()) {
            Log.e("QuizLayout", "Not enough country-continent data available!");
            return;
        }

        questions.clear();
        createCountryQuiz();
    }



}
