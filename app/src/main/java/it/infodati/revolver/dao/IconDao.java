package it.infodati.revolver.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.model.Icon;
import it.infodati.revolver.util.GlobalVar;

import static it.infodati.revolver.database.DatabaseStrings.FIELD_ID;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_NAME;
import static it.infodati.revolver.database.DatabaseStrings.TABLE_ICONS;

public class IconDao {

    // Links
    public static long createIcon(Icon model) {
        ContentValues values = new ContentValues();

        values.put(FIELD_ID, model.getId());
        values.put(FIELD_NAME, model.getName());

        return DatabaseManager.insertOrUpadte(TABLE_ICONS, values);
    }
    public static int removeIcon(int id) {
        return DatabaseManager.delete(
                TABLE_ICONS,
                FIELD_ID + "=?",
                new String[]{ String.valueOf(id) });
    }
    public static Icon getIcon(int id) {
        Icon model = new Icon();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

        Cursor cur = db.query(
                TABLE_ICONS,
                new String[] { FIELD_ID, FIELD_NAME },
                FIELD_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null);

        if (cur != null) {
            if  (cur.moveToFirst()) {
                do {
                    model.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
                    model.setName(cur.getString(cur.getColumnIndex(FIELD_NAME)));
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return model;
    }
    public static Icon getIcon(String orderBy, int limit) {
        Icon model = new Icon();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

        Cursor cur = db.query(
                TABLE_ICONS,
                new String[] { FIELD_ID, FIELD_NAME },
                null,
                null,
                null,
                null,
                orderBy,
                String.valueOf(limit));

        if (cur != null) {
            if  (cur.moveToFirst()) {
                do {
                    model.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
                    model.setName(cur.getString(cur.getColumnIndex(FIELD_NAME)));
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return model;
    }
    public static Icon getFirstIcon() {
        return getIcon(FIELD_ID,1);
    }
    public static Icon getFirstOrderedIcon() {
        return getIcon(FIELD_NAME, 1);
    }
    public static Icon getLastIcon() {
        return getIcon(FIELD_ID + " desc",1);
    }
    public static Icon getLastOrderedIcon() {
        return getIcon(FIELD_NAME + " desc", 1);
    }
    public static List<Icon> getAllIcons(String orderBy) {
        List<Icon> models = new ArrayList<Icon>();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

        Cursor cur = db.query(
                TABLE_ICONS,
                new String[] { FIELD_ID, FIELD_NAME },
                null,
                null,
                null, null,
                orderBy);

        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    Icon model = new Icon();
                    model.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
                    model.setName(cur.getString(cur.getColumnIndex(FIELD_NAME)));
                    models.add(model);
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return models;
    }
    public static List<Icon> getAllIcons() {
        return getAllIcons(FIELD_ID);
    }
    public static List<Icon> getAllOrderedIcons() {
        return getAllIcons(FIELD_NAME);
    }
}
