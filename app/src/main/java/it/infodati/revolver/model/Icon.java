package it.infodati.revolver.model;

import java.util.Locale;

public class Icon {

    private int id;
    private String name;

    // constructor
    public Icon() {}
    public Icon(int id, String name) {
        setId(id);
        setName(name);
    }

    // setter
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // getter
    public int getId() { return this.id; }
    public String getName() { return this.name; }

    // spinner adapter
    public String toString() {
        return getName();
    }
}
