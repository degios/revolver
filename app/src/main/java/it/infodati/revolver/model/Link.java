package it.infodati.revolver.model;

import java.util.Locale;

public class Link {

    private int id;
    private String url;
    private String description_eng;
    private String description_ita;
    private String note_eng;
    private String note_ita;
    private String icon;

    // constructor
    public Link() {}
    public Link(int id, String url, String description) {
        setId(id);
        setUrl(url);
        setDescription(description);
    }
    public Link(int id, String url, String description, String note) {
        setId(id);
        setUrl(url);
        setDescription(description);
        setNote(note);
    }
    public Link(int id, String url, String description, String note, String icon) {
        setId(id);
        setUrl(url);
        setDescription(description);
        setNote(note);
        setIcon(icon);
    }
    public Link(int id,String url, String description_eng, String description_ita, String note_eng, String note_ita) {
        setId(id);
        setUrl(url);
        setDescription(description_eng, description_ita);
        setNote(note_eng, note_ita);
    }
    public Link(int id,String url, String description_eng, String description_ita, String note_eng, String note_ita, String icon) {
        setId(id);
        setUrl(url);
        setDescription(description_eng, description_ita);
        setNote(note_eng, note_ita);
        setIcon(icon);
    }

    // setter
    public void setId(int id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
    public void setDescription(String description) {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            this.description_ita = description;
        } else  {
            this.description_eng = description;
        }
        if (this.description_eng==null || this.description_eng.isEmpty())
            this.description_eng = description;
        if (this.description_ita==null || this.description_ita.isEmpty())
            this.description_ita = description;
    }
    public void setDescription(String description_eng, String description_ita) {
        this.description_eng = description_eng;
        this.description_ita = description_ita;
    }
    public void setNote(String note) {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            this.note_ita = note;
        } else  {
            this.note_eng = note;
        }
        if (this.note_eng==null || this.note_eng.isEmpty())
            this.note_eng = note;
        if (this.note_ita==null || this.note_ita.isEmpty())
            this.note_ita = note;
    }
    public void setNote(String note_eng, String note_ita) {
        this.note_eng = note_eng;
        this.note_ita = note_ita;
    }
    public void setIcon(String icon) { this.icon = icon; }

    // getter
    public int getId() { return this.id; }
    public String getUrl() { return this.url; }
    public String getDescription() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return this.description_ita;
        } else {
            return this.description_eng;
        }
    }
    public String getDescriptionEng() { return this.description_eng; }
    public String getDescriptionIta() { return this.description_ita; }
    public String getNote() {
        if (Locale.getDefault().getLanguage().equals(Locale.ITALY.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            return this.note_ita;
        } else {
            return this.note_eng;
        }
    }
    public String getNoteEng() { return this.note_eng; }
    public String getNoteIta() { return this.note_ita; }
    public String getIcon() { return this.icon; }

    // spinner adapter
    public String toString() {
        return getDescription();
    }
}
