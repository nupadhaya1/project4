package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class facilitates storing and restoring quiz scores.
 */
public class QuizScoresData {

    public static final String DEBUG_TAG = "QuizScoresData";

    private SQLiteDatabase db;
    private SQLiteOpenHelper quizScoresDbHelper;

    private static final String[] allColumns = {
            QuizScoresDBHelper.COLUMN_ID,
            QuizScoresDBHelper.COLUMN_DATE,
            QuizScoresDBHelper.COLUMN_SCORE
    };

    public QuizScoresData(Context context) {
        quizScoresDbHelper = QuizScoresDBHelper.getInstance(context);
    }

    // Open the database
    public void open() {
        db = quizScoresDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "QuizScoresData: db open");
    }

    // Close the database
    public void close() {
        if (quizScoresDbHelper != null) {
            quizScoresDbHelper.close();
            Log.d(DEBUG_TAG, "QuizScoresData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db.isOpen();
    }

    // Store a new quiz score
    public QuizScore storeQuizScore(QuizScore quizScore) {
        ContentValues values = new ContentValues();
        values.put(QuizScoresDBHelper.COLUMN_DATE, quizScore.getDate());
        values.put(QuizScoresDBHelper.COLUMN_SCORE, quizScore.getScore());

        long id = db.insert(QuizScoresDBHelper.TABLE_QUIZ_SCORES, null, values);
        quizScore.setId(id);

        Log.d(DEBUG_TAG, "Stored new quiz score with id: " + id);
        return quizScore;
    }

    // Retrieve all quiz scores
    public List<QuizScore> retrieveAllQuizScores() {
        ArrayList<QuizScore> quizScores = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(QuizScoresDBHelper.TABLE_QUIZ_SCORES, allColumns,
                    null, null, null, null,
                    QuizScoresDBHelper.COLUMN_DATE + " DESC");

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(QuizScoresDBHelper.COLUMN_ID));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(QuizScoresDBHelper.COLUMN_DATE));
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow(QuizScoresDBHelper.COLUMN_SCORE));

                    QuizScore quizScore = new QuizScore(date, score);
                    quizScore.setId(id);
                    quizScores.add(quizScore);
                    Log.d(DEBUG_TAG, "Retrieved QuizScore: " + quizScore);
                }
            }

            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of records from DB: 0");
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return quizScores;
    }
}
