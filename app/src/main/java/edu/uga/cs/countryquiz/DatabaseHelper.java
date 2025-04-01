package edu.uga.cs.countryquiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
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
    public static final String QUIZ_DATE = "quiz_date";
    public static final String QUIZ_SCORE = "quiz_score";

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
                    + QUIZ_DATE + " TEXT NOT NULL, "
                    + QUIZ_SCORE + " INTEGER "
                    + ")";

    public static final String CREATE_TABLE_RESULTS =
            "CREATE TABLE " + TABLE_RESULTS + " (" +
                    RESULTS_ID + " INTEGER PRIMARY KEY," +
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
    private DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

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
    // Override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COUNTRIES);
        db.execSQL(CREATE_TABLE_QUIZZES);
        db.execSQL(CREATE_TABLE_RESULTS);
        Log.d(DEBUG_TAG, "Table " + TABLE_COUNTRIES + " created");
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZZES + " created");
    }
    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("drop table if exists " + TABLE_COUNTRIES);
        db.execSQL("drop table if exists " + TABLE_QUIZZES);
        db.execSQL(DELETE_RESULTS);
        onCreate(db);
    }

    public boolean isDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Checks to see if data is already loaded in the countries table.
     */
    public boolean isDataLoaded() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_COUNTRIES, null );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

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

}

