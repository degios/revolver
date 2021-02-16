package it.infodati.revolver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import it.infodati.revolver.database.DatabaseStrings;

public class GlobalVar {
    private static GlobalVar instance = new GlobalVar();

    public static final String TOOLBAR_ENABLED = "TOOLBAR_ENABLED";
    public static final String BOTTOMBAR_ENABLED = "BOTTOMBAR_ENABLED";
    public static final String FLOATING_ENABLED = "FLOATING_ENABLED";
    public static final String SUBTOOLBAR_ENABLED = "SUBTOOLBAR_ENABLED";
    public static final String BUTTONREMOVE_ENABLED = "BUTTONREMOVE_ENABLED";
    public static final String LINK_ID = "LINK_ID";
    public static final String DATABASE_NAME = "DATABASE_NAME";

    private String prefsName;
    private String databaseName;

    private boolean toolbarEnabled;
    private boolean bottombarEnabled;
    private boolean floatingEnabled;
    private boolean subToolbarEnabled;
    private boolean buttonRemoveEnabled;

    private int linkId = 0;


    private GlobalVar() {}
    public static GlobalVar getInstance() { return GlobalVar.instance; }

    // setter
    public void setPrefsName(String name) { this.prefsName = name; }
    public void setDatabaseName(String name) { this.databaseName = name; }

    public void setToolbarEnabled(boolean enabled) { this.toolbarEnabled = enabled; }
    public void setBottombarEnabled(boolean enabled) { this.bottombarEnabled = enabled; }
    public void setFloatingEnabled(boolean enabled) { this.floatingEnabled = enabled; }
    public void setSubToolbarEnabled(boolean enabled) { this.subToolbarEnabled = enabled; }
    public void setButtonRemoveEnabled(boolean enabled) { this.buttonRemoveEnabled = enabled; }

    public void setLinkId(int value) { this.linkId = value; }

    // getter
    public String getPrefsName() { return this.prefsName; }
    public String getDatabaseName() {
        if (this.databaseName==null || this.databaseName.trim().isEmpty()) {
            this.databaseName = DatabaseStrings.DATABASE_DEMO;
        }
        return this.databaseName;
    }

    public boolean isToolbarEnabled() { return this.toolbarEnabled; }
    public boolean isBottombarEnabled() { return this.bottombarEnabled; }
    public boolean isFloatingEnabled() { return this.floatingEnabled; }
    public boolean isSubToolbarEnabled() { return this.subToolbarEnabled; }
    public boolean isButtonRemoveEnabeld() { return this.buttonRemoveEnabled; }

    public int getLinkId() { return this.linkId; }
    public int getActionId() { return this.getLinkId(); }

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
