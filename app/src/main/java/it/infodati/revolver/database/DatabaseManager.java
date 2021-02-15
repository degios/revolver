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

import static it.infodati.revolver.database.DatabaseStrings.*;

public class DatabaseManager {

    private DatabaseHelper databaseHelper;

    // Constructor
    public DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // Closer
    public void close() {
        databaseHelper.close();
    }

    // Low-level operations
    public long insertOrUpadte(String table_name, ContentValues values) {
        long id;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            id = db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException sqle) {
            id = -1001;
        }

        return id;
    }
    public long insert(String table_name, ContentValues values) {
        long id;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            id = db.insertOrThrow(table_name, null, values);
        } catch (SQLiteException sqle) {
            id = -1001;
        }

        return id;
    }
    public int delete(String table_name, String where_clause, String[] where_args) {
        int count;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            count = db.delete(table_name, where_clause, where_args);
        } catch (SQLiteException sqle) {
            count = -1;
        }

        return count;
    }

    // Links
    public long createLink(Link model) {
        ContentValues values = new ContentValues();

        values.put(FIELD_ID, model.getId());
        values.put(FIELD_URL, model.getUrl());
        values.put(FIELD_DESCRIPTION_ENG, model.getDescriptionEng());
        values.put(FIELD_DESCRIPTION_ITA, model.getDescriptionIta());
        values.put(FIELD_NOTE_ENG, model.getNoteEng());
        values.put(FIELD_NOTE_ITA, model.getNoteIta());
        values.put(FIELD_ICON, model.getIcon());

        return insertOrUpadte(TABLE_LINKS, values);
    }
    public int removeLink(int id) {
        return delete(
                TABLE_LINKS,
                FIELD_ID + "=?",
                new String[]{ String.valueOf(id) });
    }
    public Link getLink(int id) {
        Link model = new Link();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cur = db.query(
                TABLE_LINKS,
                new String[] { FIELD_ID, FIELD_URL, FIELD_DESCRIPTION_ENG, FIELD_DESCRIPTION_ITA, FIELD_NOTE_ENG, FIELD_NOTE_ITA, FIELD_ICON },
                FIELD_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null);

        if (cur != null) {
            if  (cur.moveToFirst()) {
                do {
                    model.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
                    model.setUrl(cur.getString(cur.getColumnIndex(FIELD_URL)));
                    model.setDescription(cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ENG)),cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ITA)));
                    model.setNote(cur.getString(cur.getColumnIndex(FIELD_NOTE_ENG)),cur.getString(cur.getColumnIndex(FIELD_NOTE_ITA)));
                    model.setIcon(cur.getString(cur.getColumnIndex(FIELD_ICON)));
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return model;
    }
    public Link getLink(String orderBy, int limit) {
        Link model = new Link();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cur = db.query(
                TABLE_LINKS,
                new String[] { FIELD_ID, FIELD_URL, FIELD_DESCRIPTION_ENG, FIELD_DESCRIPTION_ITA, FIELD_NOTE_ENG, FIELD_NOTE_ITA, FIELD_ICON },
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
                    model.setUrl(cur.getString(cur.getColumnIndex(FIELD_URL)));
                    model.setDescription(cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ENG)),cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ITA)));
                    model.setNote(cur.getString(cur.getColumnIndex(FIELD_NOTE_ENG)),cur.getString(cur.getColumnIndex(FIELD_NOTE_ITA)));
                    model.setIcon(cur.getString(cur.getColumnIndex(FIELD_ICON)));
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return model;
    }
    public Link getFirstLink() {
        return getLink(FIELD_ID,1);
    }
    public Link getFirstOrderedLink() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getLink(FIELD_DESCRIPTION_ITA, 1);
        } else {
            return getLink(FIELD_DESCRIPTION_ENG, 1);
        }
    }
    public Link getLastLink() {
        return getLink(FIELD_ID + " desc",1);
    }
    public Link getLastOrderedLink() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getLink(FIELD_DESCRIPTION_ITA + " desc", 1);
        } else {
            return getLink(FIELD_DESCRIPTION_ENG + " desc", 1);
        }
    }
    public List<Link> getAllLinks(String orderBy) {
        List<Link> models = new ArrayList<Link>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cur = db.query(
                TABLE_LINKS,
                new String[] { FIELD_ID, FIELD_URL, FIELD_DESCRIPTION_ENG, FIELD_DESCRIPTION_ITA, FIELD_NOTE_ENG, FIELD_NOTE_ITA, FIELD_ICON },
                null,
                null,
                null, null,
                orderBy);

        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    Link model = new Link();
                    model.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
                    model.setUrl(cur.getString(cur.getColumnIndex(FIELD_URL)));
                    model.setDescription(cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ENG)),cur.getString(cur.getColumnIndex(FIELD_DESCRIPTION_ITA)));
                    model.setNote(cur.getString(cur.getColumnIndex(FIELD_NOTE_ENG)),cur.getString(cur.getColumnIndex(FIELD_NOTE_ITA)));
                    model.setIcon(cur.getString(cur.getColumnIndex(FIELD_ICON)));
                    models.add(model);
                } while (cur.moveToNext());
            }
            cur.close();
        }

        return models;
    }
    public List<Link> getAllLinks() {
        return getAllLinks(FIELD_ID);
    }
    public List<Link> getAllOrderedLinks() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getAllLinks(FIELD_DESCRIPTION_ITA);
        } else {
            return getAllLinks(FIELD_DESCRIPTION_ENG);
        }
    }

    // Actions
    public Link getAction(int id) { return getLink(id); }
    public Link getAction(String orderBy, int limit) { return getLink(orderBy, limit); }
    public Link getFirstAction() {
        return getFirstLink();
    }
    public Link getFirstOrderedAction() { return getFirstOrderedLink(); }
    public Link getLastAction() { return getLastLink(); }
    public Link getLastOrderedAction() { return getLastOrderedLink(); }
    public List<Link> getAllActions(String orderBy) { return getAllLinks(orderBy); }
    public List<Link> getAllActions() {
        return getAllLinks();
    }
    public List<Link> getAllOrderedActions() { return getAllOrderedLinks(); }
}
