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
    } // CountryData

    /**
     * Opens the database for writing. Must be called before any read/write operations.
     */
    public void open() {
        db = countryDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "CountryData: db open");
        //Toast.makeText(context, "Open Country database is working", Toast.LENGTH_SHORT).show();
    } // open

    /**
     * Closes the database when finished to avoid memory leaks.
     */
    public void close() {
        if (countryDbHelper != null) {
            countryDbHelper.close();
            Log.d(DEBUG_TAG, "CountryData: db closed");
            //Toast.makeText(context, "Close CountryDB is working", Toast.LENGTH_SHORT).show();
        } // if statement
    } // close

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
    } //storeCountry

    /**
     * Retrieves all countries from the database
     *
     * @return A list of Country objects from the database.
     */
    public List<Country> retrieveAllCountry() {
        // Create empty list to hold country objects
        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Query the database to retrieve all columns from the Country table,
            cursor = db.query(
                    CountryDBHelper.TABLE_COUNTRY, // name of table to query
                    allColumns,                    // The columns to return in the result (id, country, continent
                    null,                          // null selects all rows
                    null,
                    null,
                    null,
                    CountryDBHelper.COLUMN_COUNTRY + " DESC" // order by descending order
            ); // query

            // Check if the cursor is not null and contains at least one row
            if (cursor != null && cursor.getCount() > 0) {

                // Loop through each row in the result set
                while (cursor.moveToNext()) {

                    // Retrieve the ID from the current row
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_ID));

                    // Retrieve the country name from the current row
                    String country = cursor.getString(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_COUNTRY));

                    // Retrieve the continent name from the current row
                    String continent = cursor.getString(cursor.getColumnIndexOrThrow(CountryDBHelper.COLUMN_CONTINENT));

                    // Create a new Country object using the country and continent
                    Country countryDB = new Country(country, continent);

                    // set id
                    countryDB.setId(id);

                    // Add the Country object to the list of countries
                    countries.add(countryDB);

                    Log.d(DEBUG_TAG, "Retrieved country: " + country);
                }
            } // if statement

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
    } // retreiveAllCountry
} // CountryData


