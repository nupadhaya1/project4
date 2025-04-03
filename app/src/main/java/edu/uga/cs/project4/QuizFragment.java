package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizFragment extends Fragment {

    // UI variables
    private TextView questionText;
    private TextView questionCountText;
    private RadioGroup answersGroup;
    private RadioButton answerA, answerB, answerC;
    private Button nextButton, endButton;

    // data storage for question
    private List<Question> quizQuestions;
    private int currentQuestion = 0;
    private int score = 0;

    // constructor
    public QuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Initialize UI components
        questionText = view.findViewById(R.id.questionText);
        questionCountText = view.findViewById(R.id.questionCountText);
        answersGroup = view.findViewById(R.id.answersGroup);
        answerA = view.findViewById(R.id.answerA);
        answerB = view.findViewById(R.id.answerB);
        answerC = view.findViewById(R.id.answerC);
        nextButton = view.findViewById(R.id.nextButton);

        // Load countries from the database
        CountryData countryData = new CountryData(requireContext());
        countryData.open();
        List<Country> allCountries = countryData.retrieveAllCountry();
        countryData.close();


        // Select 6 unique random countries
        Set<Integer> selectedIndexes = new HashSet<>();
        Random random = new Random();
        List<Country> selectedCountries = new ArrayList<>();

        while (selectedCountries.size() < 6) {
            int index = random.nextInt(allCountries.size());
            if (!selectedIndexes.contains(index)) {
                selectedCountries.add(allCountries.get(index));
                selectedIndexes.add(index);
            } // if statement
        } // while loop

        // Get list of all unique continents
        Set<String> continentSet = new HashSet<>();
        for (Country c : allCountries) {
            continentSet.add(c.getContinent());
        } // for

        List<String> allContinents = new ArrayList<>(continentSet);

        // Build list of questions
        quizQuestions = new ArrayList<>();
        for (Country country : selectedCountries) {
            List<String> incorrect = new ArrayList<>(allContinents);
            incorrect.remove(country.getContinent());
            Collections.shuffle(incorrect);
            List<String> wrongAnswers = incorrect.subList(0, 2);
            quizQuestions.add(new Question(country.getCountry(), country.getContinent(), wrongAnswers));
        } // for loop

        // Display first question
        showQuestion();

        // Next button logic
        nextButton.setOnClickListener(v -> {
            int selectedId = answersGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = view.findViewById(selectedId);
            String selectedText = selected.getText().toString();
            String selectedContinent = selectedText.substring(3);

            if (quizQuestions.get(currentQuestion).isCorrect(selectedContinent)) {
                score++;
            }

            currentQuestion++;
            if (currentQuestion < quizQuestions.size()) {
                answersGroup.clearCheck();
                showQuestion();
            } else {
                // Save score
                String currentDate = DateFormat.getDateTimeInstance().format(new java.util.Date());
                QuizScore quizScore = new QuizScore(currentDate, score);

                QuizScoresData scoresData = new QuizScoresData(requireContext());
                scoresData.open();
                scoresData.storeQuizScore(quizScore);
                scoresData.close();

                // Show Toast
                Toast.makeText(getContext(), "Quiz complete! Score: " + score + "/6", Toast.LENGTH_LONG).show();

                // Navigate back to SplashActivity
                Intent intent = new Intent(requireContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        return view;
    }

    // show question and answer options
    private void showQuestion() {
        Question question = quizQuestions.get(currentQuestion);
        questionText.setText("Which continent is " + question.getCountryName() + " located in?");
        List<String> options = question.getAnswerOptions();
        answerA.setText(" A. " + options.get(0));
        answerB.setText(" B. " + options.get(1));
        answerC.setText(" C. " + options.get(2));

        // Update the question count display
        questionCountText.setText("Quiz Progress: " + (currentQuestion + 1) + "/6");
    } // showQuestion
} // quiz fragment