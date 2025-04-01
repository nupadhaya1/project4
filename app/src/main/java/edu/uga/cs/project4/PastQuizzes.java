package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class PastQuizzes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_past_quizzes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Past Quizzes");
        }

        // Load quiz scores and display them
        ListView quizListView = findViewById(R.id.quizListView);

        QuizScoresData scoresData = new QuizScoresData(this);
        scoresData.open();
        List<QuizScore> scoreList = scoresData.retrieveAllQuizScores();
        scoresData.close();

        List<String> formattedScores = new ArrayList<>();
        for (QuizScore score : scoreList) {
            formattedScores.add(score.getDate() + "               " + score.getScore());
        }


        Toast.makeText(this, "Loaded " + formattedScores.size() + " scores", Toast.LENGTH_SHORT).show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.quiz_score_item,
                R.id.scoreItem,
                formattedScores
        );


        if (formattedScores.isEmpty()) {
            formattedScores.add("1         Sample Date         100");
        }


        quizListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PastQuizzes.this, SplashActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
