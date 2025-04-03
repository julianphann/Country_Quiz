package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An AsyncTask to read country and continent data from a CSV file
 * and load it into the SQLite database.
 */
public class ReadCSVTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "ReadCSVTask";

    private Context context;
    private DatabaseHelper databaseHelper;
    private OnReadCSVCompleteListener listener;

    /**
     * An interface to be implemented by the caller to receive a
     * callback when the CSV reading and database loading is complete.
     */
    public interface OnReadCSVCompleteListener {
        /**
         * Called when the CSV reading and database loading is complete.
         * @param good True if the CSV file was read and the data was loaded
         * successfully, false otherwise.
         */
        void onReadCSVComplete(boolean good);
    }

    /**
     * Constructor for ReadCSVTask.
     * @param context The application context.
     */
    public ReadCSVTask(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Constructor for ReadCSVTask with a completion listener.
     * @param context The application context.
     * @param listener The listener to be called when the task is complete.
     */
    public ReadCSVTask(Context context, OnReadCSVCompleteListener listener) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
        this.listener = listener;
    }

    /**
     * Reads the CSV file in the background and loads the country and
     * continent data into the SQLite database.
     *
     * @param voids No parameters are used.
     * @return True if the CSV file was read and the data was loaded
     * successfully, false otherwise.
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            return countryContinentData(database);
        } catch (Exception e) {
            Log.e(TAG, "Error with input stream from CSV", e);
            return false;
        }
    }

    /**
     * Reads the country and continent data from the CSV file, inserts
     * it into the database.
     * @param database The SQLiteDatabase instance to write to.
     * @return True if the data was successfully imported.
     */
    private boolean countryContinentData(SQLiteDatabase database) {
        try (InputStream inputStream = context.getAssets().open("country_continent.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            database.beginTransaction();
            try {
                database.delete(DatabaseHelper.TABLE_COUNTRIES, null, null);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    if (row.length >= 2) {
                        ContentValues content = new ContentValues();
                        content.put(DatabaseHelper.COUNTRY_NAME, row[0].trim());
                        content.put(DatabaseHelper.COUNTRY_CONTINENT, row[1].trim());
                        long newRow = database.insert(DatabaseHelper.TABLE_COUNTRIES, null, content);

                        if (newRow == -1) {
                            Log.e(TAG, "ERROR INSERTING COUNTRY: " + row[0]);
                        }
                    }
                }
                database.setTransactionSuccessful();
                return true;
            } finally {
                database.endTransaction();
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR IMPORTING COUNTRY AND CONTINENT DATA", e);
            return false;
        }
    }

    /**
     * Called after the execution of the background task is complete.
     * Calls the listener with the status of CSV import (success or failure).
     * @param good True if the CSV file was read and processed successfully.
     */
    @Override
    protected void onPostExecute( Boolean good ) {
        if (good) {
            Log.d(TAG, "CSV imported and read correctly.");
        } else {
            Log.e(TAG, "CSV import unsuccessful");
        }
        if (listener != null) {
            listener.onReadCSVComplete(good);
        }
    }
}
