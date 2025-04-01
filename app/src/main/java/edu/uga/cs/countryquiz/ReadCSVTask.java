package edu.uga.cs.countryquiz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class ReadCSVTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "ReadCSVTask";

    private Context context;
    private DatabaseHelper databaseHelper;
    private OnReadCSVCompleteListener listener;

    public interface OnReadCSVCompleteListener {
        void onReadCSVComplete(boolean good);
    }

    public ReadCSVTask(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public ReadCSVTask(Context context, OnReadCSVCompleteListener listener) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
        this.listener = listener;
    }
    // An internal method to execute something in background

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
