package edu.uga.cs.project4;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizFragment extends Fragment {

    private ViewPager2 viewPager;
    private List<Question> quizQuestions;
    private final List<String> userAnswers = new ArrayList<>(Collections.nCopies(6, null));
    private int score = 0;

    public QuizFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        viewPager = view.findViewById(R.id.viewPager);

        if (savedInstanceState != null) {
            // Restore saved state
            quizQuestions = (List<Question>) savedInstanceState.getSerializable("quizQuestions");
            List<String> savedAnswers = savedInstanceState.getStringArrayList("userAnswers");
            userAnswers.clear();
            userAnswers.addAll(savedAnswers);
            score = savedInstanceState.getInt("score");
        } else {
            // First time setup
            CountryData countryData = new CountryData(requireContext());
            countryData.open();
            List<Country> allCountries = countryData.retrieveAllCountry();
            countryData.close();

            // Pick 6 unique countries
            Set<Integer> selectedIndexes = new HashSet<>();
            Random random = new Random();
            List<Country> selectedCountries = new ArrayList<>();
            while (selectedCountries.size() < 6) {
                int index = random.nextInt(allCountries.size());
                if (!selectedIndexes.contains(index)) {
                    selectedCountries.add(allCountries.get(index));
                    selectedIndexes.add(index);
                }
            }

            // Get unique continents
            Set<String> continentSet = new HashSet<>();
            for (Country c : allCountries) {
                continentSet.add(c.getContinent());
            }
            List<String> allContinents = new ArrayList<>(continentSet);

            // Create quiz questions
            quizQuestions = new ArrayList<>();
            for (Country country : selectedCountries) {
                List<String> incorrect = new ArrayList<>(allContinents);
                incorrect.remove(country.getContinent());
                Collections.shuffle(incorrect);
                List<String> wrongAnswers = incorrect.subList(0, 2);
                quizQuestions.add(new Question(country.getCountry(), country.getContinent(), wrongAnswers));
            }
        }

        // Adapter
        QuizPagerAdapter adapter = new QuizPagerAdapter(
                requireActivity(),
                quizQuestions,
                new QuizPagerAdapter.AnswerSelectedListener() {
                    @Override
                    public void onAnswerSelected(int position, String answer) {
                        userAnswers.set(position, answer);
                    }

                    @Override
                    public void onLastQuestionAnswered() {
                        collectAnswersAndFinish();
                    }
                }
        );
        viewPager.setAdapter(adapter);
        if (savedInstanceState != null) {
            int currentPage = savedInstanceState.getInt("currentPage", 0);
            viewPager.setCurrentItem(currentPage, false);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("quizQuestions", new ArrayList<>(quizQuestions));
        outState.putStringArrayList("userAnswers", new ArrayList<>(userAnswers));
        outState.putInt("score", score);
        outState.putInt("currentPage", viewPager.getCurrentItem());
    }


    // Show the current question and options
    private void collectAnswersAndFinish() {
        for (int i = 0; i < quizQuestions.size(); i++) {
            String selected = userAnswers.get(i);
            if (selected != null && quizQuestions.get(i).isCorrect(selected)) {
                score++;
            }
        }

        String currentDate = DateFormat.getDateTimeInstance().format(new java.util.Date());
        QuizScore quizScore = new QuizScore(currentDate, score);
        new SaveQuizScoreTask(requireContext()).execute(quizScore);

        // Toast.makeText(getContext(), "Quiz complete! Score: " + score + "/6", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(requireContext(), ScoreView.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    // AsyncTask for saving the quiz score
    private static class SaveQuizScoreTask extends AsyncTask<QuizScore, Void, Boolean> {
        private final QuizScoresData scoresData;

        public SaveQuizScoreTask(Context context) {
            this.scoresData = new QuizScoresData(context.getApplicationContext());
        }

        @Override
        protected Boolean doInBackground(QuizScore... scores) {
            try {
                scoresData.open();
                scoresData.storeQuizScore(scores[0]);
                scoresData.close();
                return true;
            } catch (Exception e) {
                Log.e("QuizFragment", "Error saving quiz score", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast.makeText(scoresData.context, "Failed to save score", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
