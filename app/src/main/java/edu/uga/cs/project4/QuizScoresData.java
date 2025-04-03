package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages operations for storing and retrieving quiz scores
 * using an SQLite database.
 */
public class QuizScoresData {

    // Tag used for logging
    public static final String DEBUG_TAG = "QuizScoresData";

    // The database INSTANCE used for read/write operations
    private SQLiteDatabase db;

    // Reference to the SQLiteOpenHelper that manages database creation and changes
    private SQLiteOpenHelper quizScoresDbHelper;

    // Context used for Toast messages
    private Context context;

    // Array of column names to retrieve from the database when querying
    private static final String[] allColumns = {
            QuizScoresDBHelper.COLUMN_ID,
            QuizScoresDBHelper.COLUMN_DATE,
            QuizScoresDBHelper.COLUMN_SCORE
    };

    /**
     * Constructor: initializes the helper using the application context.
     * Uses singleton pattern to ensure only one database helper is used.
     */
    public QuizScoresData(Context context) {
        this.context = context;
        quizScoresDbHelper = QuizScoresDBHelper.getInstance(context);
    }

    /**
     * Opens the database for writing. Must be called before any read/write operations.
     */
    public void open() {
        db = quizScoresDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "QuizScoresData: db open");
    }

    /**
     * Closes the database when finished to avoid memory leaks.
     */
    public void close() {
        if (quizScoresDbHelper != null) {
            quizScoresDbHelper.close();
            Log.d(DEBUG_TAG, "QuizScoresData: db closed");
        }
    }

    /**
     * Checks whether the database is currently open.
     */
    public boolean isDBOpen() {
        return db.isOpen();
    }

    /**
     * Inserts a new quiz score into the database.
     *
     * @param quizScore The QuizScore object containing the date and score.
     * @return The same object, now with an assigned ID from the database.
     */
    public QuizScore storeQuizScore(QuizScore quizScore) {
        ContentValues values = new ContentValues();
        values.put(QuizScoresDBHelper.COLUMN_DATE, quizScore.getDate());
        values.put(QuizScoresDBHelper.COLUMN_SCORE, quizScore.getScore());

        long id = db.insert(QuizScoresDBHelper.TABLE_QUIZ_SCORES, null, values);
        quizScore.setId(id);

        Log.d(DEBUG_TAG, "Stored new quiz score with id: " + id);
        return quizScore;
    }

    /**
     * Retrieves all quiz scores from the database, ordered by most recent date.
     *
     * @return A list of QuizScore objects from the database.
     */
    public List<QuizScore> retrieveAllQuizScores() {
        ArrayList<QuizScore> quizScores = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    QuizScoresDBHelper.TABLE_QUIZ_SCORES,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    QuizScoresDBHelper.COLUMN_DATE + " DESC"
            );

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
    public void deleteAllQuizScores() {
        ((QuizScoresDBHelper) quizScoresDbHelper).deleteAllQuizScores();
        Log.d(DEBUG_TAG, "deleteAllQuizScores: all scores deleted via QuizScoresData");
    }

}


