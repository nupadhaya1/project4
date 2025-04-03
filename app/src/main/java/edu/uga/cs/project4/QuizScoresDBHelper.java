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
 * SQLiteOpenHelper class to manage the quiz scores database.
 */
public class QuizScoresDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "QuizScoresDBHelper";

    private static final String DB_NAME = "quizscores.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_QUIZ_SCORES = "quizscores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SCORE = "score";

    private static QuizScoresDBHelper helperInstance;

    private static final String CREATE_QUIZ_SCORES =
            "create table " + TABLE_QUIZ_SCORES + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_SCORE + " INTEGER"
                    + ")";

    private QuizScoresDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static QuizScoresDBHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new QuizScoresDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUIZ_SCORES);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZ_SCORES + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_QUIZ_SCORES);
        onCreate(db);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZ_SCORES + " upgraded");
    }

    // Insert a new quiz score
    public long insertQuizScore(String date, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_SCORE, score);

        long id = db.insert(TABLE_QUIZ_SCORES, null, values);
        db.close();
        return id;
    }

    // Query all quiz scores
    public List<String> getAllQuizScores() {
        List<String> scoresList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_QUIZ_SCORES,
                null, // all columns
                null,
                null,
                null,
                null,
                COLUMN_DATE + " DESC" // order by date
        );

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
                scoresList.add("ID: " + id + ", Date: " + date + ", Score: " + score);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return scoresList;
    }

    // Delete a quiz score by ID
    public boolean deleteQuizScore(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_QUIZ_SCORES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
    public void deleteAllQuizScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ_SCORES, null, null);
        db.close();
        Log.d(DEBUG_TAG, "All quiz scores deleted");
    }

}