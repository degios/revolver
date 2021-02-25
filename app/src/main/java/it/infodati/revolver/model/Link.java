package it.infodati.revolver.model;

import java.util.Locale;

public class Link {

    private int id;
    private String url;
    private String title;
    private int bookmark;
    private int autorun;
    private byte[] icon;

    // constructor
    public Link() {}
    public Link(int id, String url, String title) {
        setId(id);
        setUrl(url);
        setTitle(title);
    }
    public Link(int id, String url, String title, int bookmark) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setBookmark(bookmark);
    }
    public Link(int id, String url, String title, int bookmark, int autorun) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setBookmark(bookmark);
        setAutorun(autorun);
    }
    public Link(int id, String url, String title, boolean bookmark) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setBookmark(bookmark);
    }
    public Link(int id, String url, String title, boolean bookmark, boolean autorun) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setBookmark(bookmark);
        setAutorun(autorun);
    }
    public Link(int id, String url, String title, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
    }
    public Link(int id, String url, String title, int bookmark, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
        setBookmark(bookmark);
    }
    public Link(int id, String url, String title, int bookmark, int autorun, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
        setBookmark(bookmark);
        setAutorun(autorun);
    }
    public Link(int id, String url, String title, boolean bookmark, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
        setBookmark(bookmark);
    }
    public Link(int id, String url, String title, boolean bookmark, boolean autorun, byte[] icon) {
        setId(id);
        setUrl(url);
        setTitle(title);
        setIcon(icon);
        setBookmark(bookmark);
        setAutorun(autorun);
    }

    // setter
    public void setId(int id) { this.id = id; }
    public void setUrl(String url) { this.url = url; }
    public void setTitle(String title) { this.title = title; }
    public void setBookmark(int bookmark)  { this.bookmark = (bookmark==0 ? 0 : 1); }
    public void setBookmark(boolean bookmark) { this.bookmark = (bookmark ? 1 : 0); }
    public void setAutorun(int autorun)  { this.autorun = (autorun==0 ? 0 : 1); }
    public void setAutorun (boolean autorun) { this.autorun = (autorun ? 1 : 0); }
    public void setIcon(byte[] icon) { this.icon = icon; }

    // getter
    public int getId() { return this.id; }
    public String getUrl() { return this.url; }
    public String getTitle() { return this.title; }
    public int getBookmark() { return this.bookmark; }
    public boolean hasBookmark() { return (this.bookmark==0 ? false : true); }
    public int getAutorun() { return this.autorun; }
    public boolean hasAutorun() { return (this.autorun==0 ? false : true); }
    public byte[] getIcon() { return this.icon; }

    // spinner adapter
    public String toString() {
        return getTitle();
    }
}
