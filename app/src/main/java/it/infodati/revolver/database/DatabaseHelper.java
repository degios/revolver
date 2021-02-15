package it.infodati.revolver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.infodati.revolver.util.GlobalVar;

import static it.infodati.revolver.database.DatabaseStrings.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database version
    public static final int DATABASE_VERSION = 1;

    // Table create statement
    // Links
    public static final String CREATE_TABLE_LINKS = "CREATE TABLE IF NOT EXISTS " + TABLE_LINKS + " ( "
            + FIELD_ID + " INTEGER PRIMARY KEY, "
            + FIELD_URL + " TEXT NOT NULL, "
            + FIELD_DESCRIPTION_ENG + " TEXT NOT NULL UNIQUE, "
            + FIELD_DESCRIPTION_ITA + " TEXT NOT NULL UNIQUE, "
            + FIELD_NOTE_ENG + " TEXT, "
            + FIELD_NOTE_ITA + " TEXT, "
            + FIELD_ICON + " TEXT "
            + ")";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, GlobalVar.getInstance().getDatabaseName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LINKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_LINKS);
        }
        onCreate(db);
    }
}
