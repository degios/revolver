package it.infodati.revolver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import it.infodati.revolver.adapter.ActionsAdapter;
import it.infodati.revolver.database.DatabaseHelper;
import it.infodati.revolver.database.DatabaseStrings;

public class GlobalVar {
    private static GlobalVar instance = new GlobalVar();

    public static final String BOTTOMBAR_ENABLED = "BOTTOMBAR_ENABLED";
    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final String FLOATING_ENABLED = "FLOATING_ENABLED";
    public static final String GRIDVIEW_ENABLED = "GRIDVIEW_ENABLED";
    public static final String LINK_ID = "LINK_ID";
    public static final String SWIPE_ENABLED = "SWIPE_ENABLED";
    public static final String SUBBLOCK_ENABLED = "SUBBLOCK_ENABLED";
    public static final String SUBSWIPE_ENABLED = "SUBSWIPE_ENABLED";
    public static final String SUBZOOM_ENABLED = "SUBZOOM_ENABLED";
    public static final String SUBACTIVITY_ENABLED = "SUBACTIVITY_ENABLED";
    public static final String SUBTOOLBAR_ENABLED = "SUBTOOLBAR_ENABLED";
    public static final String BUTTONREMOVE_ENABLED = "BUTTONREMOVE_ENABLED";
    public static final String TOOLBAR_ENABLED = "TOOLBAR_ENABLED";
    public static final String WIZARD_ENABLED = "WIZARD_ENABLED";

    public static final int CUSTOM_MIN = 20000;
    public static final int CUSTOM_MAX = 29999;

    private String prefsName;
    private String databaseName;

    private boolean toolbarEnabled;
    private boolean bottombarEnabled;
    private boolean floatingEnabled;
    private boolean swipeEnabled;
    private boolean wizardEnabled;
    private boolean gridViewEnabled;
    private boolean subBlockEnabled;
    private boolean subSwipeEnabled;
    private boolean subZoomEnabled;
    private boolean subActivityEnabled;
    private boolean subToolbarEnabled;
    private boolean buttonRemoveEnabled;

    private int linkId = 0;
    private int currentLinkId = 0;

    private DatabaseHelper databaseHelper ;


    private GlobalVar() {}
    public static GlobalVar getInstance() { return GlobalVar.instance; }

    // setter
    public void setPrefsName(String name) { this.prefsName = name; }
    public void setDatabaseName(String name) { this.databaseName = name; }

    public void setToolbarEnabled(boolean enabled) { this.toolbarEnabled = enabled; }
    public void setBottombarEnabled(boolean enabled) { this.bottombarEnabled = enabled; }
    public void setFloatingEnabled(boolean enabled) { this.floatingEnabled = enabled; }
    public void setSwipeEnabled(boolean enabled) { this.swipeEnabled = enabled; }
    public void setWizardEnabled(boolean enabled) { this.wizardEnabled = enabled; }
    public void setGridviewEnabled(boolean enabled) { this.gridViewEnabled = enabled; }
    public void setSubBlockEnabled(boolean enabled) { this.subBlockEnabled = enabled; }
    public void setSubActivityEnabled(boolean enabled) { this.subActivityEnabled = enabled; }
    public void setSubSwipeEnabled(boolean enabled) { this.subSwipeEnabled = enabled; }
    public void setSubZoomEnabled(boolean enabled) { this.subZoomEnabled = enabled; }
    public void setSubToolbarEnabled(boolean enabled) { this.subToolbarEnabled = enabled; }
    public void setButtonRemoveEnabled(boolean enabled) { this.buttonRemoveEnabled = enabled; }

    public void setLinkId(int value) { this.linkId = value; }
    public void setCurrentLinkId(int value) { this.currentLinkId = value; }

    public void setDatabaseHelper(Context context) {
        if (this.databaseHelper == null)
            this.databaseHelper = new DatabaseHelper(context);
    }

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
    public boolean isSwipeEnabled() { return this.swipeEnabled; }
    public boolean isWizardEnabled() { return this.wizardEnabled; }
    public boolean isGridViewEnabled() { return this.gridViewEnabled; }
    public boolean isSubBlockEnabled() { return this.subBlockEnabled; }
    public boolean isSubActivityEnabled() { return this.subActivityEnabled; }
    public boolean isSubToolbarEnabled() { return this.subToolbarEnabled; }
    public boolean isSubSwipeEnabled() { return this.subSwipeEnabled; }
    public boolean isSubZoomEnabled() { return this.subZoomEnabled; }
    public boolean isButtonRemoveEnabeld() { return this.buttonRemoveEnabled; }

    public int getLinkId() { return this.linkId; }
    public int getCurrentLinkId() { return this.currentLinkId; }

    public DatabaseHelper getDatabaseHelper() { return this.databaseHelper; }

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
