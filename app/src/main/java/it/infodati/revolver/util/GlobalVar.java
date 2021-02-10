package it.infodati.revolver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import it.infodati.revolver.database.DatabaseStrings;

public class GlobalVar {
    private static GlobalVar instance = new GlobalVar();

    public static final String DATABASE_NAME = "DATABASE_NAME";

    private String prefs_name;
    private String database_name;

    private GlobalVar() {}
    public static GlobalVar getInstance() { return GlobalVar.instance; }

    // setter
    public void setPrefsName(String name) { this.prefs_name = name; }
    public void setDatabaseName(String name) { this.database_name = name; }

    // getter
    public String getPrefsName() { return this.prefs_name; }
    public String getDatabaseName() {
        if (this.database_name==null || this.database_name.trim().isEmpty()) {
            this.database_name = DatabaseStrings.DATABASE_DEMO;
        }
        return this.database_name;
    }

    public int getListIndexByString(ArrayList<Object> list, Object obj) {
        int position = 0;

        for (int i=0; i<list.size(); i++)
            if (list.get(i).toString().equals(obj.toString())) {
                position = i;
                break;
            }

        return position;
    }

    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return (netInfo != null && netInfo.isConnectedOrConnecting());
        return (netInfo != null && (netInfo.isConnected() && netInfo.isAvailable()));
    }

}
