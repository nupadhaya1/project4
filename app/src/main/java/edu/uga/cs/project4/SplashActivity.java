package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

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

        // Find the button and set a click listener
        Button quizButton = findViewById(R.id.quizButton);
        Button pastButton = findViewById(R.id.pastButton);

        // listen and start the quiz button
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity (which contains ChoiceFragment)
                Intent intent = new Intent(SplashActivity.this, NewQuizzes.class);
                startActivity(intent);
                finish(); // Close SplashActivity so the user can't go back to it
            }
        });

        // listen and start the past quizzes
        pastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity (which contains ChoiceFragment)
                Intent intent = new Intent(SplashActivity.this, PastQuizzes.class);

                startActivity(intent);
                finish();
            } // onClick
        }); // listener


    } // onCreate
} // Splash Activity