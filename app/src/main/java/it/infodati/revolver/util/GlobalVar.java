package it.infodati.revolver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import it.infodati.revolver.database.DatabaseStrings;

public class GlobalVar {
    private static GlobalVar instance = new GlobalVar();

    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final String LINK_ID = "LINK_ID";

    private String prefsName;
    private String databaseName;

    private boolean drawerEnabled;
    private boolean toolbarEnabled;
    private boolean bottombarEnabled;
    private boolean floatingEnabled;

    private int linkId = 0;


    private GlobalVar() {}
    public static GlobalVar getInstance() { return GlobalVar.instance; }

    // setter
    public void setPrefsName(String name) { this.prefsName = name; }
    public void setDatabaseName(String name) { this.databaseName = name; }

    public void setDrawerEnabled(boolean enabled) { this.drawerEnabled = enabled; }
    public void setToolbarEnabled(boolean enabled) { this.toolbarEnabled = enabled; }
    public void setBottombarEnabled(boolean enabled) { this.bottombarEnabled = enabled; }
    public void setFloatingEnabled(boolean enabled) { this.floatingEnabled = enabled; }

    public void setLinkId(int value) { this.linkId = value; }

    // getter
    public String getPrefsName() { return this.prefsName; }
    public String getDatabaseName() {
        if (this.databaseName==null || this.databaseName.trim().isEmpty()) {
            this.databaseName = DatabaseStrings.DATABASE_DEMO;
        }
        return this.databaseName;
    }

    public boolean isDrawerEnabled() { return this.drawerEnabled; }
    public boolean isToolbarEnabled() { return this.toolbarEnabled; }
    public boolean isBottombarEnabled() { return this.bottombarEnabled; }
    public boolean isFloatingEnabled() { return this.floatingEnabled; }

    public int getLinkId() { return this.linkId; }

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
