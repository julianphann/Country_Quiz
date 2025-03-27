package edu.uga.cs.countryquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A SQLiteOpenHelper class to manage database creation and updates.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "DatabaseHelper";
    private static final String DB_NAME = "CountriesDB";
    private static final int DB_VERSION = 1;

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

    private static DatabaseHelper instance; // Singleton instance

    // Table Create SQL Statements
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_TABLE_COUNTRIES =
            "create table " + TABLE_COUNTRIES + " ("
                    + COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRY_NAME + " TEXT NOT NULL, "
                    + COUNTRY_CONTINENT + " TEXT NOT NULL"
                    + ")";

    private static final String CREATE_TABLE_QUIZZES =
            "create table " + TABLE_QUIZZES + " ("
                    + QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZ_DATE + " TEXT NOT NULL, "
                    + QUIZ_SCORE + " INTEGER"
                    + ")";
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
        onCreate(db);
    }
}

