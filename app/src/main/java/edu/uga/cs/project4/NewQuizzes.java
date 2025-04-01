package edu.uga.cs.project4;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewQuizzes extends AppCompatActivity {

    private QuizScoresData quizScoresData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_quizzes);

        // Apply window insets (for padding UI from system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // open and create the database
        quizScoresData = new QuizScoresData(this);
        quizScoresData.open();

//        // close
//        quizScoresData.close();


        Toast.makeText(this, "End of Code", Toast.LENGTH_SHORT).show();
    } // onCreate
} // NewQuizzes
