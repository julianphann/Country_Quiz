package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A SQLiteOpenHelper class to manage database creation and updates.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "DatabaseHelper";
    private static final String DB_NAME = "CountriesDB";
    private static final int DB_VERSION = 1;

    private Context context;

    // Table: Countries
    public static final String TABLE_COUNTRIES = "countries";
    public static final String COUNTRY_ID = "_id";
    public static final String COUNTRY_NAME = "country_name";
    public static final String COUNTRY_CONTINENT = "continent";

    // Table: Quizzes
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZ_ID = "_id";
    public static final String QUIZ_TITLE = "quiz_title";
    public static final String QUIZ_DATE = "quiz_date";
//    public static final String QUIZ_SCORE = "quiz_score";

    //Table: Results
    public static final String TABLE_RESULTS = "results";
    public static final String RESULTS_ID = "_id";
    public static final String RESULTS_QUIZ_ID = "quiz_id";
    public static final String RESULTS_SCORE = "results_score";
    public static final String RESULTS_DATE = "results_date";


    private static DatabaseHelper instance; // Singleton instance

    // Table Create SQL Statements
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_TABLE_COUNTRIES =
            "CREATE TABLE " + TABLE_COUNTRIES + " ("
                    + COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRY_NAME + " TEXT NOT NULL, "
                    + COUNTRY_CONTINENT + " TEXT NOT NULL "
                    + ")";

    private static final String CREATE_TABLE_QUIZZES =
            "CREATE TABLE " + TABLE_QUIZZES + " ("
                    + QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZ_TITLE + " TEXT NOT NULL, "
                    + QUIZ_DATE + " TEXT )";

    public static final String CREATE_TABLE_RESULTS =
            "CREATE TABLE " + TABLE_RESULTS + " (" +
                    RESULTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RESULTS_QUIZ_ID + " INTEGER, " +
                    RESULTS_SCORE + " INTEGER, " +
                    RESULTS_DATE + " TEXT, " +
                    "FOREIGN KEY (" + RESULTS_QUIZ_ID + ") REFERENCES " + TABLE_QUIZZES + "(" + QUIZ_ID + "))";

    //Delete countries
    private static final String DELETE_COUNTRIES =
            "DROP TABLE IF EXISTS " + TABLE_COUNTRIES;

    //Delete Quizzes
    private static final String DELETE_QUIZZES =
            "DROP TABLE IF EXISTS " + TABLE_QUIZZES;

    //Delete results
    private static final String DELETE_RESULTS =
            "DROP TABLE IF EXISTS " + TABLE_RESULTS;
    /**
     * Private constructor for singleton pattern. Constructor is private, so can only be called from this class
     * @param context The application context.
     */
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    /**
     * Access to the singleton instance of the DatabaseHelper.
     * @param context The application context.
     * @return The DatabaseHelper instance.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    /**
     * Override onCreate method, which will be used to create the database if
     * it does not exist yet.
     * @param db The database being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COUNTRIES);
        db.execSQL(CREATE_TABLE_QUIZZES);
        db.execSQL(CREATE_TABLE_RESULTS);
        Log.d(DEBUG_TAG, "Table " + TABLE_COUNTRIES + " created");
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZZES + " created");
    }
    /**
     * We should override onUpgrade method, which will be used to upgrade the database if
     * its version (DB_VERSION) has changed.  This will be done automatically by Android
     * if the version will be bumped up, as we modify the database schema.
     * @param db The database being upgraded.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL(DELETE_COUNTRIES);
        db.execSQL(DELETE_QUIZZES);
        db.execSQL(DELETE_RESULTS);
        onCreate(db);
    }

    /**
     * Checks if the database file exists.
     * @return True if the database exists, false otherwise.
     */
    public boolean isDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Checks to see if data is already loaded in the countries table.
     * @return True if data is loaded, false otherwise.
     */
    public boolean isDataLoaded() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_COUNTRIES, null );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    /**
     * Retrieves a map of random country-continent pairs from the database.
     * @param count The number of random pairs to retrieve.
     * @return A map where the key is the country name and the value is the continent.
     */
    public Map<String, String> getRandomPairs(int count) {
        Map<String, String> countryContinent = new HashMap<>();
        SQLiteDatabase database = this.getReadableDatabase();

        String[] projection = {
                COUNTRY_NAME, COUNTRY_CONTINENT
        };

        Cursor cursor = database.query(
                TABLE_COUNTRIES,
                projection,
                null,
                null,
                null,
                null,
                "RANDOM()",
                String.valueOf(count)
        );

        try {
            while (cursor != null && cursor.moveToNext()) {
                int indexName = cursor.getColumnIndexOrThrow(COUNTRY_NAME);
                int continentIndex = cursor.getColumnIndexOrThrow(COUNTRY_CONTINENT);

                String countryName = cursor.getString(indexName);
                String continent = cursor.getString(continentIndex);

                countryContinent.put(countryName, continent);

            }
        } finally {
            if (cursor != null) {
                cursor.close();

            }
        }
        return countryContinent;
    }

    /**
     * Retrieves all quiz results from the database, ordered by date in descending order.
     * @return A list of strings representing the quiz results.
     */
    public List<String> getAllQuizResults() {
        List<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + RESULTS_SCORE + ", " + RESULTS_DATE +
                        " FROM " + TABLE_RESULTS +
                        " ORDER BY " + RESULTS_DATE + " DESC",
                null
        );

        while (cursor.moveToNext()) {
            int score = cursor.getInt(0);
            String date = cursor.getString(1);
            results.add("Date: " + date + " | Score: " + score + "/6");
        }

        cursor.close();
        return results;
    }

    /**
     * Inserts a quiz result into the database.
     * @param score The score of the quiz.
     */
    public void insertQuizResult(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        // gets the date and time of the quiz completion and formats it
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        Log.d("DatabaseHelper", "Inserting quiz result with score: " + score + " at " + date);

        // create a new quiz record
        ContentValues quizValues = new ContentValues();
        quizValues.put(QUIZ_TITLE, "Quiz");
        quizValues.put(QUIZ_DATE, date);
        long quizId = db.insert(TABLE_QUIZZES, null, quizValues);

        Log.d("DatabaseHelper", "Inserted quiz record with ID: " + quizId);

        // inserts the quiz result
        ContentValues resultValues = new ContentValues();
        resultValues.put(RESULTS_QUIZ_ID, quizId);
        if (score < 3) {
            resultValues.put(RESULTS_SCORE, score);
        } else {
            resultValues.put(RESULTS_SCORE, score + 1);
        }
        resultValues.put(RESULTS_DATE, date);
        long resultRowId = db.insert(TABLE_RESULTS, null, resultValues);

        Log.d("DatabaseHelper", "Inserted result record with ID: " + resultRowId);

        db.close(); // Always close the db connection!!
    }

}
