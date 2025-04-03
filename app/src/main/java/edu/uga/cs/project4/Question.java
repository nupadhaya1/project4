package edu.uga.cs.project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;


/**
 * Represents a single multiple-choice quiz question.
 */
public class Question implements Serializable{

    // The country this question is about
    private final String countryName;
    // The correct continent for the country
    private final String correctContinent;
    // Possible answer choices
    private final List<String> answerOptions;

    /**
     * Constructor for Question.
     */
    public Question(String countryName, String correctContinent, List<String> incorrectContinents) {
        this.countryName = countryName;
        this.correctContinent = correctContinent;

        // Create answer options list and add the correct and incorrect answers
        this.answerOptions = new ArrayList<>();
        answerOptions.add(correctContinent);
        answerOptions.addAll(incorrectContinents);

        // Shuffle the answer options so the correct answer appears randomly
        Collections.shuffle(answerOptions);
    }

    // Get the country this question is about
    public String getCountryName() {
        return countryName;
    }

    // Get the correct answer
    public String getCorrectContinent() {
        return correctContinent;
    }

    // Return the list of possible answer options
    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    /**
     * Check if the user's selected answer is correct.
     */
    public boolean isCorrect(String selectedContinent) {
        return correctContinent.equals(selectedContinent);
    }
} // Question