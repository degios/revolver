package it.infodati.revolver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

import static it.infodati.revolver.database.DatabaseStrings.*;

public class DatabaseManager {
    public static long insertOrUpadte(String table_name, ContentValues values) {
        long id;
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getWritableDatabase();

        try {
            id = db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
            id = -1001;
        }

        return id;
    }
    public static long insert(String table_name, ContentValues values) {
        long id;
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getWritableDatabase();

        try {
            id = db.insertOrThrow(table_name, null, values);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
            id = -1001;
        }

        return id;
    }
    public static int delete(String table_name, String where_clause, String[] where_args) {
        int count;
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getWritableDatabase();

        try {
            count = db.delete(table_name, where_clause, where_args);
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
            count = -1;
        }

        return count;
    }
}
