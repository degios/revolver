package it.infodati.revolver.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

import static it.infodati.revolver.database.DatabaseStrings.FIELD_DESCRIPTION_ENG;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_DESCRIPTION_ITA;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_ICON;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_ID;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_NOTE_ENG;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_NOTE_ITA;
import static it.infodati.revolver.database.DatabaseStrings.FIELD_URL;
import static it.infodati.revolver.database.DatabaseStrings.TABLE_LINKS;

public class LinkDao {

    // Links
    public static long createLink(Link model) {
        ContentValues values = new ContentValues();

        values.put(FIELD_ID, model.getId());
        values.put(FIELD_URL, model.getUrl());
        values.put(FIELD_DESCRIPTION_ENG, model.getDescriptionEng());
        values.put(FIELD_DESCRIPTION_ITA, model.getDescriptionIta());
        values.put(FIELD_NOTE_ENG, model.getNoteEng());
        values.put(FIELD_NOTE_ITA, model.getNoteIta());
        values.put(FIELD_ICON, model.getIcon());

        return DatabaseManager.insertOrUpadte(TABLE_LINKS, values);
    }
    public static int removeLink(int id) {
        return DatabaseManager.delete(
                TABLE_LINKS,
                FIELD_ID + "=?",
                new String[]{ String.valueOf(id) });
    }
    public static Link getLink(int id) {
        Link model = new Link();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

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
    public static Link getLink(String orderBy, int limit) {
        Link model = new Link();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

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
    public static Link getFirstLink() {
        return getLink(FIELD_ID,1);
    }
    public static Link getFirstOrderedLink() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getLink(FIELD_DESCRIPTION_ITA, 1);
        } else {
            return getLink(FIELD_DESCRIPTION_ENG, 1);
        }
    }
    public static Link getLastLink() {
        return getLink(FIELD_ID + " desc",1);
    }
    public static Link getLastOrderedLink() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getLink(FIELD_DESCRIPTION_ITA + " desc", 1);
        } else {
            return getLink(FIELD_DESCRIPTION_ENG + " desc", 1);
        }
    }
    public static List<Link> getAllLinks(String orderBy) {
        List<Link> models = new ArrayList<Link>();
        SQLiteDatabase db = GlobalVar.getInstance().getDatabaseHelper().getReadableDatabase();

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
    public static List<Link> getAllLinks() {
        return getAllLinks(FIELD_ID);
    }
    public static List<Link> getAllOrderedLinks() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return getAllLinks(FIELD_DESCRIPTION_ITA);
        } else {
            return getAllLinks(FIELD_DESCRIPTION_ENG);
        }
    }
}
