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
 * This class manages operations for storing and retrieving country
 * using an SQLite database.
 */
public class CountryData {

    // Tag used for logging
    public static final String DEBUG_TAG = "CountryData";

    // The database INSTANCE used for read/write operations
    private SQLiteDatabase db;

    // Reference to the SQLiteOpenHelper that manages database creation and changes
    private SQLiteOpenHelper countryDbHelper;

    // Context used for Toast messages
    private Context context;

    // Array of column names to retrieve from the database when querying
    private static final String[] allColumns = {
            CountryDBHelper.COLUMN_ID,
            CountryDBHelper.COLUMN_COUNTRY,
            CountryDBHelper.COLUMN_CONTINENT
    };

    /**
     * Constructor: initializes the helper using the application context.
     * Uses singleton pattern to ensure only one database helper is used.
     */
    public CountryData(Context context) {
        this.context = context;
        countryDbHelper = CountryDBHelper.getInstance(context);
    }

    /**
     * Opens the database for writing. Must be called before any read/write operations.
     */
    public void open() {
        db = countryDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "CountryData: db open");
        Toast.makeText(context, "Open Country database is working", Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the database when finished to avoid memory leaks.
     */
    public void close() {
        if (countryDbHelper != null) {
            countryDbHelper.close();
            Log.d(DEBUG_TAG, "CountryData: db closed");
            Toast.makeText(context, "Close CountryDB is working", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks whether the database is currently open.
     */
    public boolean isDBOpen() {
        return db.isOpen();
    }

    public Country storeCountry(Country country) {
        ContentValues values = new ContentValues();
        values.put(CountryDBHelper.COLUMN_COUNTRY, country.getCountry());
        values.put(CountryDBHelper.COLUMN_CONTINENT, country.getContinent());

        long id = db.insert(CountryDBHelper.TABLE_COUNTRY, null, values);
        country.setId(id);

        Log.d(DEBUG_TAG, "Stored new country with id: " + id);
        return country;
    }

    /**
     * Retrieves all countries from the database
     *
     * @return A list of Country objects from the database.
     */
    public List<Country> retrieveAllCountry() {
        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    CountryDBHelper.TABLE_COUNTRY,
                    allColumns,
                    null,
                    null,
                    null,
                    null,
                    CountryDBHelper.COLUMN_COUNTRY + " DESC"
            );

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_ID));
                    String country = cursor.getString(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_COUNTRY));
                    String continent = cursor.getString(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_CONTINENT));

                    Country countryDB = new Country(country, continent);
                    countryDB.setId(id);
                    countries.add(country);

                    Log.d(DEBUG_TAG, "Retrieved country: " + country);
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

        return countries;
    }
}


