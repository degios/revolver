package it.infodati.revolver.model;

import java.util.Locale;

public class Link {

    private int id;
    private byte[] icon;
    private String url;
    private String title;

    // constructor
    public Link() {}
    public Link(int id, String url, String title) {
        setId(id);
        setUrl(url);
        setTitle(title);
    }
    public Link(int id, String url, String title, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
    }

    // setter
    public void setId(int id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
    public void setTitle(String title) { this.title = title; }
    public void setIcon(byte[] icon) { this.icon = icon; }

    // getter
    public int getId() { return this.id; }
    public String getUrl() { return this.url; }
    public String getTitle() { return title; }
    public byte[] getIcon() { return this.icon; }

    // spinner adapter
    public String toString() {
        return getTitle();
    }
}
