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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.database.DatabaseStrings;
import it.infodati.revolver.fragment.ActionFragment;
import it.infodati.revolver.fragment.ActionsFragment;
import it.infodati.revolver.fragment.LoadDataFragment;
import it.infodati.revolver.fragment.LoadInterfaceFragment;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SwipeRefreshLayout swipe;
    private Fragment fragment;
    private Snackbar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVar.getInstance().setPrefsName(getString(R.string.app_name ) + "PrefsFile");
        this.loadData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

/*
        fragment = new ActionsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        setTitle(getResources().getString(R.string.home));
*/

        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Log.i("REFRESH", "onRefresh MainActivity called from SwipeRefreshLayout");
                loadInterface();
                ((LoadDataFragment) fragment).loadData();
                ((LoadInterfaceFragment) fragment).loadInterface();
                swipe.setRefreshing(false);
            }
        });

        snackBar = Snackbar.make(swipe,R.string.back_again,Snackbar.LENGTH_SHORT);

        this.loadInterface();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String scheme = intent.getScheme();
        String sharedText = "";

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
/*
                Snackbar.make( editTextUrl, "[" + type.toString() + "][" + scheme + "] " + sharedText, Snackbar.LENGTH_LONG)
                        .setAction( "[" + type.toString() + "][" + scheme + "] " + sharedText, null)
                        .show();
*/

                if (!sharedText.trim().toUpperCase().startsWith("HTTP"))
                    sharedText = "";
        }

        if (sharedText!=null && !sharedText.isEmpty()) {
            String host = "";
            try {
                host = new URL(sharedText).getHost();
            } catch (Exception e) {
                host = "";
            }

            intent = new Intent(this, LinkActivity.class);
            intent.putExtra(getResources().getString(R.string.id).toString(),0);
            intent.putExtra(getResources().getString(R.string.url), sharedText);
            startActivityForResult(intent, 0);
        } else {
            int menuIndex = (GlobalVar.getInstance().getLinkId() > 0 ? GlobalVar.getInstance().CUSTOM_MIN + GlobalVar.getInstance().getLinkId() : R.id.nav_home);
            MenuItem menuItem = navigationView.getMenu().findItem(menuIndex);
            if (menuItem == null)
                menuIndex = R.id.nav_home;
            onNavigationItemSelected(navigationView.getMenu().findItem(menuIndex));
        }
    }

    @Override
    public void onBackPressed() {
        boolean doDefault = true;

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            doDefault = false;
        } else {
            Fragment actionFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (actionFragment instanceof ActionFragment) {
                if (!((ActionFragment) actionFragment).goBack()) {
                    fragment = new ActionsFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    setTitle(getResources().getString(R.string.home));
                }
                doDefault = false;
            }
        }
        if (doDefault)
            if (snackBar.isShown()) {
                super.onBackPressed();
            } else {
                snackBar.show();
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
        boolean activityWithResult = false;
        Fragment fragment = null;
        String fragmentClassName = null;

        int itemId = item.getItemId();
        if (itemId == R.id.menu_add) {
            activityClassName = this.getClass().getPackage().getName() + "." + (GlobalVar.getInstance().isWizardEnabled() ? "WizardActivity" : "LinkActicity");
            if (!GlobalVar.getInstance().isWizardEnabled())
                GlobalVar.getInstance().setCurrentLinkId(0);
            activityWithResult = true;
/*
        } else if (itemId == R.id.menu_links) {
            activityClassName = this.getClass().getPackage().getName()+"."+"LinksActivity";
*/
        } else if (itemId == R.id.menu_settings) {
            activityClassName = this.getClass().getPackage().getName()+"."+"SettingsActivity";
        } else if (itemId == R.id.menu_refresh) {
            loadInterface();
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
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
            intent.putExtra(getResources().getString(R.string.id).toString(),GlobalVar.getInstance().getCurrentLinkId());
            if (activityWithResult) {
                startActivityForResult(intent, 0);
            } else {
                startActivity(intent);
            }
            return true;
        } else if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(item.getTitle());
            this.fragment = fragment;
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class activity = null;
        String activityClassName = null;
        boolean activityWithResult = false;
        Fragment fragment = null;
        String fragmentClassName = null;

        int itemId = item.getItemId();
        if (itemId==R.id.nav_home) {
            fragmentClassName = this.getClass().getPackage().getName()+".fragment."+"ActionsFragment";
/*
        } else if (itemId == R.id.nav_links) {
            activityClassName = this.getClass().getPackage().getName()+"."+"LinksActivity";
*/
        } else if (itemId == R.id.nav_settings) {
            activityClassName = this.getClass().getPackage().getName()+"."+"SettingsActivity";
        } else if (itemId >=GlobalVar.getInstance().CUSTOM_MIN && itemId <=GlobalVar.getInstance().CUSTOM_MAX) {
            fragmentClassName = this.getClass().getPackage().getName()+".fragment."+"ActionFragment";
            GlobalVar.getInstance().setCurrentLinkId(itemId-GlobalVar.getInstance().CUSTOM_MIN);
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
            intent.putExtra(getResources().getString(R.string.id).toString(),GlobalVar.getInstance().getCurrentLinkId());
            if (activityWithResult) {
                startActivityForResult(intent, 0);
            } else {
                startActivity(intent);
            }
        } else if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(item.getTitle());
            this.fragment = fragment;
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
        GlobalVar.getInstance().setDatabaseHelper(getApplicationContext());
        GlobalVar.getInstance().setToolbarEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.TOOLBAR_ENABLED, false));
        GlobalVar.getInstance().setBottombarEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.BOTTOMBAR_ENABLED, false));
        GlobalVar.getInstance().setFloatingEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.FLOATING_ENABLED, false));
        GlobalVar.getInstance().setSwipeEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SWIPE_ENABLED, false));
        GlobalVar.getInstance().setWizardEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.WIZARD_ENABLED, false));
        GlobalVar.getInstance().setSubBlockEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SUBBLOCK_ENABLED, false));
        GlobalVar.getInstance().setSubActivityEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SUBACTIVITY_ENABLED, false));
        GlobalVar.getInstance().setSubToolbarEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SUBTOOLBAR_ENABLED, false));
        GlobalVar.getInstance().setSubSwipeEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SUBSWIPE_ENABLED, false));
        GlobalVar.getInstance().setSubZoomEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.SUBZOOM_ENABLED, false));
        GlobalVar.getInstance().setButtonRemoveEnabled(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getBoolean(GlobalVar.BUTTONREMOVE_ENABLED, false));
        GlobalVar.getInstance().setDatabaseName(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getString(GlobalVar.DATABASE_NAME, DatabaseStrings.DATABASE_DEMO));

        SharedPreferences sharedPreferences = getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (GlobalVar.getInstance().getDatabaseName().equals(DatabaseStrings.DATABASE_DEMO)) {
            Link lastLink = LinkDao.getLastLink();
            if (lastLink.getId() <= 0) {
/*
                ArrayList<Link> links = new ArrayList<Link>();
                links.add(new Link(1, getString(R.string.ALL_demo_link_1), getString(R.string.ENG_demo_link_1_desc), getString(R.string.ITA_demo_link_1_desc), "", ""));
                links.add(new Link(2, getString(R.string.ALL_demo_link_2), getString(R.string.ENG_demo_link_2_desc), getString(R.string.ITA_demo_link_2_desc), "", ""));
                links.add(new Link(3, getString(R.string.ALL_demo_link_3), getString(R.string.ENG_demo_link_3_desc), getString(R.string.ITA_demo_link_3_desc), "", ""));
                links.add(new Link(4, getString(R.string.ALL_demo_link_4), getString(R.string.ENG_demo_link_4_desc), getString(R.string.ITA_demo_link_4_desc), "", ""));

                for (Link link : links)
                    LinkDao.createLink(link);
*/
            }
        }

        // Manager autorun link
        Link autorunLink = LinkDao.getAutorunLink();
        editor.putInt(GlobalVar.LINK_ID, (autorunLink!=null && autorunLink.getId()>0 ? autorunLink.getId() : 0));
        editor.apply();
        GlobalVar.getInstance().setLinkId(getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE).getInt(GlobalVar.LINK_ID, 0));
    }

    private void loadInterface() {
        if (!GlobalVar.getInstance().isToolbarEnabled())
            actionBar.hide();

        if (!GlobalVar.getInstance().isSwipeEnabled())
            swipe.setEnabled(false);

        int menuIndex = 0;
        int menuOrder = GlobalVar.getInstance().CUSTOM_MIN;
        Menu navigationMenu = navigationView.getMenu();
        MenuItem menuItem;

        navigationMenu.clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);

        List<Link> list = LinkDao.getAllOrderedBookmarkedLinks();
        if (list!=null) {
            for (Link link : list) {
                menuIndex = GlobalVar.getInstance().CUSTOM_MIN + link.getId();
                menuOrder += 1;
                navigationMenu.add(R.id.nav_custom, menuIndex, menuOrder, link.getTitle());
                menuItem = navigationMenu.findItem(menuIndex);

                byte[] arIcon = link.getIcon();
                if (arIcon!=null && arIcon.length>0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(arIcon, 0, arIcon.length);
                    Drawable drawable = (Drawable) new BitmapDrawable(getResources(), bitmap);
                    menuItem.setIcon(drawable);
                } else {
                    menuItem.setIcon(android.R.drawable.ic_menu_share);
                }
            }
        }
        drawerLayout.refreshDrawableState();
        drawerLayout.invalidate();
        toggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            swipe.setRefreshing(true);
            loadInterface();
            ((LoadDataFragment) fragment).loadData();
            ((LoadInterfaceFragment) fragment).loadInterface();
            swipe.setRefreshing(false);
        }
    }
}