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
public class CountryDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "CountryDBHelper";

    private static final String DB_NAME = "country.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_COUNTRY = "country";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CONTINENT = "continent";

    private static CountryDBHelper helperInstance;

    private static final String CREATE_COUNTRY =
            "create table " + TABLE_COUNTRY + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_COUNTRY + " TEXT, "
                    + COLUMN_CONTINENT + " TEXT"
                    + ")";

    private CountryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static CountryDBHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new CountryDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUNTRY);
        Log.d(DEBUG_TAG, "Table " + TABLE_COUNTRY + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_COUNTRY);
        onCreate(db);
        Log.d(DEBUG_TAG, "Table " + TABLE_COUNTRY + " upgraded");
    }

    // Insert a new country
    public long insertCountry(String country, String continent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_COUNTRY, country);
        values.put(COLUMN_CONTINENT, continent);

        long id = db.insert(TABLE_COUNTRY, null, values);
        db.close();
        return id;
    }

    // Query all quiz scores
    public List<String> getAllCountry() {
        List<String> countryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_COUNTRY,
                null, // all columns
                null,
                null,
                null,
                null,
                COLUMN_COUNTRY + " DESC" // order by date
        );

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY));
                String continent = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTINENT));
                countryList.add("ID: " + id + ", Country: " + country + ", Continent: " + continent);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return countryList;
    }

    // Delete a quiz score by ID
    public boolean deleteCountry(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_COUNTRY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
}