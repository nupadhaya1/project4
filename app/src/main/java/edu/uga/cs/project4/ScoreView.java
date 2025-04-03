// ScoreView.java
package edu.uga.cs.project4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ScoreView extends Activity {

    private Button homeButton;
    private Button pastScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_view);

        TextView scoreDisplay = findViewById(R.id.scoreDisplay);
        homeButton = findViewById(R.id.homeButton);
        pastScoresButton = findViewById(R.id.pastScoresButton);

        // Get score from intent
        int score = getIntent().getIntExtra("score", 0);
        scoreDisplay.setText("Score: " + score + "/6");

        // Home button -> SplashActivity
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreView.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Past Scores button -> PastQuizzes
        pastScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreView.this, PastQuizzes.class);
            startActivity(intent);
        });
    }
}
