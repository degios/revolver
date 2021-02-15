package it.infodati.revolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.database.DatabaseStrings;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SwipeRefreshLayout swipe;
//    private MainFragment fragment;
/*
    private AppCompatEditText editURL;
    private AppCompatButton buttonLoad;
    private WebView webView;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVar.getInstance().setPrefsName(getString(R.string.app_name ) + "PrefsFile");
        this.loadData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
//        actionBar.hide();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Log.i("REFRESH", "onRefresh FundsActivity called from SwipeRefreshLayout");
                loadInterface();
                swipe.setRefreshing(false);
            }
        });

        this.loadInterface();

/*
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
*/
//        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
/*
        webView.getSettings().getUseWideViewPort();
        webView.setInitialScale(1);
*/

/*
        editURL = findViewById(R.id.editURL);

        buttonLoad = findViewById(R.id.buttonLoad);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editURL.getText().toString();

                if (!url.isEmpty())
                    webView.loadUrl(url);
            }
        });
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Class activity = null;
        String activityClassName = null;
        Fragment fragment = null;
        String fragmentClassName = null;

        int itemId = item.getItemId();
        if (itemId == R.id.menu_settings) {
            activityClassName = this.getClass().getPackage().getName()+"."+"SettingsActivity";
        }

        if (activityClassName != null) {
            try {
                activity = Class.forName(activityClassName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (fragmentClassName != null) {
            try {
                fragment = (Fragment) Class.forName("fragment."+fragmentClassName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (activity != null) {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
            return true;
        } else if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(item.getTitle());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class activity = null;
        String activityClassName = null;
        Fragment fragment = null;
        String fragmentClassName = null;

        int itemId = item.getItemId();
        if (itemId==R.id.nav_home) {
            // Instanciate specific fragment
        } else if (itemId == R.id.nav_links) {
            activityClassName = this.getClass().getPackage().getName()+"."+"LinksActivity";
        } else if (itemId == R.id.nav_settings) {
            activityClassName = this.getClass().getPackage().getName()+"."+"SettingsActivity";
        }

        if (activityClassName != null) {
            try {
                activity = Class.forName(activityClassName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (fragmentClassName != null) {
            try {
                fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (activity != null) {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
        } else if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(item.getTitle());
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void loadData() {
        GlobalVar.getInstance().setDatabaseName(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getString(GlobalVar.DATABASE_NAME, DatabaseStrings.DATABASE_DEMO));

        SharedPreferences sharedPreferences = getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());

        if (GlobalVar.getInstance().getDatabaseName().equals(DatabaseStrings.DATABASE_DEMO)) {
            Link lastLink = databaseManager.getLastLink();
            if (lastLink.getId() <= 0) {
                ArrayList<Link> links = new ArrayList<Link>();
                links.add(new Link(1, getString(R.string.ALL_demo_link_1), getString(R.string.ENG_demo_link_1_desc), getString(R.string.ITA_demo_link_1_desc), "", ""));
                links.add(new Link(2, getString(R.string.ALL_demo_link_2), getString(R.string.ENG_demo_link_2_desc), getString(R.string.ITA_demo_link_2_desc), "", ""));
                links.add(new Link(3, getString(R.string.ALL_demo_link_3), getString(R.string.ENG_demo_link_3_desc), getString(R.string.ITA_demo_link_3_desc), "", ""));
                links.add(new Link(4, getString(R.string.ALL_demo_link_4), getString(R.string.ENG_demo_link_4_desc), getString(R.string.ITA_demo_link_4_desc), "", ""));

                for (Link link : links)
                    databaseManager.createLink(link);

                editor.putInt(GlobalVar.LINK_ID,1);
                editor.apply();
            }
        }
        GlobalVar.getInstance().setLinkId(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getInt(GlobalVar.LINK_ID, 0));

        databaseManager.close();
    }

    private void loadInterface() {
        Menu navigationMenu = navigationView.getMenu();

        navigationMenu.clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);

        if (GlobalVar.isNetwork(getApplicationContext())) {
//            Toast.makeText(getApplicationContext(), "Internet connection permitted", Toast.LENGTH_LONG).show();

//          onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));

/*
            int menuIndex = 0;
            MenuItem menuItem;

            navigationMenu.add(R.id.nav_custom, 2101, 2101, "Transactions");
            menuItem = navigationMenu.findItem(2101);
            menuItem.setIcon(android.R.drawable.ic_menu_share);

            navigationMenu.add(R.id.nav_custom, 2102, 2102, "Trend");
            menuItem = navigationMenu.findItem(2102);
            menuItem.setIcon(android.R.drawable.ic_menu_sort_by_size);
*/

        } else {
//            Toast.makeText(getApplicationContext(), "ERROR Internet connection not permitted!", Toast.LENGTH_LONG).show();
        }

//        drawerLayout.refreshDrawableState();
//        drawerLayout.invalidate();
//        toggle.syncState();
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
*/
}