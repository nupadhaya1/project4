package edu.uga.cs.project4;

import android.content.Intent;
import android.os.AsyncTask;
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

    private ListView quizListView;
    private ArrayAdapter<String> adapter;
    private List<String> formattedScores;

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

        quizListView = findViewById(R.id.quizListView);
        formattedScores = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                R.layout.quiz_score_item,
                R.id.scoreItem,
                formattedScores
        );

        quizListView.setAdapter(adapter);

        // Load scores asynchronously
        new LoadQuizScoresTask().execute();
    }

    private class LoadQuizScoresTask extends AsyncTask<Void, Void, List<QuizScore>> {

        @Override
        protected List<QuizScore> doInBackground(Void... voids) {
            QuizScoresData scoresData = new QuizScoresData(getApplicationContext());
            scoresData.open();
            List<QuizScore> scores = scoresData.retrieveAllQuizScores();
            scoresData.close();
            return scores;
        }

        @Override
        protected void onPostExecute(List<QuizScore> scoreList) {
            formattedScores.clear();

            if (scoreList.isEmpty()) {
                formattedScores.add("1         Sample Date         100");
            } else {
                for (QuizScore score : scoreList) {
                    formattedScores.add(score.getDate() + "               " + score.getScore()  + "/6");
                }
            }

            adapter.notifyDataSetChanged();
//            Toast.makeText(PastQuizzes.this, "Loaded " + scoreList.size() + " scores", Toast.LENGTH_SHORT).show();
        }
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