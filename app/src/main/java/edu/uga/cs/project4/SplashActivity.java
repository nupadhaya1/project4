package edu.uga.cs.project4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {

    private Button quizButton;
    private Button pastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quizButton = findViewById(R.id.quizButton);
        pastButton = findViewById(R.id.pastButton);

        // Disable buttons until data is loaded
        quizButton.setEnabled(false);
        pastButton.setEnabled(false);

        // Start the AsyncTask to load data
        new LoadDataTask().execute();

        // Set up button listeners
        quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, NewQuizzes.class);
            startActivity(intent);
            finish();
        });

        pastButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, PastQuizzes.class);
            startActivity(intent);
            finish();
        });
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            boolean isDataLoaded = prefs.getBoolean("data_loaded", false);

            if (!isDataLoaded) {
                CountryData countryData = new CountryData(SplashActivity.this);
                countryData.open();

                try {
                    InputStream inputStream = getResources().openRawResource(R.raw.country_continent);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            String[] parts = line.split(",");
                            if (parts.length >= 2) {
                                String countryName = parts[0].trim();
                                String continentName = parts[1].trim();

                                Country country = new Country(countryName, continentName);
                                countryData.storeCountry(country);
                            }
                        }
                    }

                    reader.close();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("data_loaded", true);
                    editor.apply();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    countryData.close();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SplashActivity.this, "Data loaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SplashActivity.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
            }

            // Enable buttons after loading
            quizButton.setEnabled(true);
            pastButton.setEnabled(true);
        }
    }
}
