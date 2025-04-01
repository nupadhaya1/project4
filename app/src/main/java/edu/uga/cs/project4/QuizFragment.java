package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class QuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        TextView countryText = view.findViewById(R.id.textView3);
        TextView continentText = view.findViewById(R.id.textView4);
        Button nextButton = view.findViewById(R.id.nextButton);
        Button endButton = view.findViewById(R.id.endButton); // New button

        CountryData countryData = new CountryData(requireContext());
        countryData.open();

        List<Country> allCountries = countryData.retrieveAllCountry();

        Runnable showRandomCountry = () -> {
            if (!allCountries.isEmpty()) {
                Random random = new Random();
                Country randomCountry = allCountries.get(random.nextInt(allCountries.size()));
                countryText.setText("Country: " + randomCountry.getCountry());
                continentText.setText("Continent: " + randomCountry.getContinent());
            } else {
                countryText.setText("No data");
                continentText.setText("");
            }
        };

        showRandomCountry.run();

        nextButton.setOnClickListener(v -> showRandomCountry.run());

        endButton.setOnClickListener(v -> {
            // Get current date
            String currentDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

            // Create a test quiz score with score -1
            QuizScore quizScore = new QuizScore(currentDate, -1);

            // Insert into the database
            QuizScoresData scoresData = new QuizScoresData(requireContext());
            scoresData.open();
            scoresData.storeQuizScore(quizScore);
            scoresData.close();

            // Go back to SplashActivity
            Intent intent = new Intent(requireContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
        });


        return view;
    }
}
