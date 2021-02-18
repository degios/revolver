package it.infodati.revolver.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.infodati.revolver.R;
import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.database.DatabaseStrings;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switchToolbar;
    private SwitchCompat switchBottombar;
    private SwitchCompat switchFloating;
    private AppCompatSpinner spinnerLinks;
    private SwitchCompat switchSubBlock;
    private SwitchCompat switchSubActivity;
    public SwitchCompat switchSubToolbar;
    private SwitchCompat switchButtonRemove;
    private AppCompatSpinner spinnerDatabases;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchToolbar = view.findViewById(R.id.switch_toolbar);
        switchBottombar = view.findViewById(R.id.switch_bottombar);
        switchFloating = view.findViewById(R.id.switch_floating);
        spinnerLinks = view.findViewById(R.id.spinner_links);
        switchSubBlock = view.findViewById(R.id.switch_subblock);
        switchSubActivity = view.findViewById(R.id.switch_subactivity);
        switchSubToolbar = view.findViewById(R.id.switch_subtoolbar);
        switchButtonRemove = view.findViewById(R.id.switch_button_remove);
        spinnerDatabases = view.findViewById(R.id.spinner_databases);

        this.loadSwitches();
        this.loadSpinnerLinksData();
        this.loadSpinnerDatabasesData();

        switchToolbar.setOnCheckedChangeListener(this);
        switchBottombar.setOnCheckedChangeListener(this);
        switchFloating.setOnCheckedChangeListener(this);
        spinnerLinks.setOnItemSelectedListener(this);
        switchSubBlock.setOnCheckedChangeListener(this);
        switchSubActivity.setOnCheckedChangeListener(this);
        switchSubToolbar.setOnCheckedChangeListener(this);
        switchButtonRemove.setOnCheckedChangeListener(this);
        spinnerDatabases.setOnItemSelectedListener(this);
    }

    public void loadSpinnerLinksData() {
        List<Link> list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager(getActivity());
            list = databaseManager.getAllOrderedLinks();
            databaseManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        if (list!=null)
            Snackbar.make( spinner, "[" + String.valueOf(list.size()) + "] " + " funds loaded", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(list.size()) + "] " + " funds loaded", null)
                    .show();
*/

        if (list!=null) {
            ArrayAdapter adapter = new ArrayAdapter<Link>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLinks.setAdapter(adapter);

            if (GlobalVar.getInstance().getLinkId()>0) {
                Link model = null;
                try {
                    DatabaseManager databaseManager = new DatabaseManager(getActivity());
                    model = databaseManager.getLink(GlobalVar.getInstance().getLinkId());
                    databaseManager.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model!=null) {
                    int position = GlobalVar.getInstance().getListIndexByString((ArrayList<Object>)(ArrayList<?>)(list), model);
/*
                    Snackbar.make( spinnerLinks, "[" + String.valueOf(model.getId()) + "][" + String.valueOf(position) + "] " + " fund position", Snackbar.LENGTH_LONG)
                            .setAction( "[" + String.valueOf(position) + "] " + " link position", null)
                            .show();
*/

                    if (position>0) {
                        spinnerLinks.setSelection(position);
                    }
                }
            }
        }
    }

    public void loadSwitches() {
        this.switchToolbar.setChecked(GlobalVar.getInstance().isToolbarEnabled());
        this.switchBottombar.setChecked(GlobalVar.getInstance().isBottombarEnabled());
        this.switchFloating.setChecked(GlobalVar.getInstance().isFloatingEnabled());
        this.switchSubBlock.setChecked(GlobalVar.getInstance().isSubBlockEnabled());
        this.switchSubActivity.setChecked(GlobalVar.getInstance().isSubActivityEnabled());
        this.switchSubToolbar.setChecked(GlobalVar.getInstance().isSubToolbarEnabled());
        this.switchButtonRemove.setChecked(GlobalVar.getInstance().isButtonRemoveEnabeld());
    }

    public void loadSpinnerDatabasesData() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(DatabaseStrings.DATABASE_DEMO);
        list.add(DatabaseStrings.DATABASE_REAL);
/*
        if (list!=null)
            Snackbar.make( spinner, "[" + String.valueOf(list.size()) + "] " + " databases loaded", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(list.size()) + "] " + " databases loaded", null)
                    .show();
*/
        if (list!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDatabases.setAdapter(adapter);

            if (!GlobalVar.getInstance().getDatabaseName().isEmpty()) {
                int position = list.indexOf(GlobalVar.getInstance().getDatabaseName());
                if (position>0) {
                    spinnerDatabases.setSelection(position);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (parent.getId() == R.id.spinner_links) {
            Link model = (Link) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.LINK_ID,model.getId());
                editor.apply();
/*
            Snackbar.make( view, "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", null)
                    .show();
*/
                GlobalVar.getInstance().setLinkId(sharedPreferences.getInt(GlobalVar.LINK_ID,0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parent.getId() == R.id.spinner_databases) {
            String name = (String) parent.getItemAtPosition(position);
            try {
                if (!sharedPreferences.getString(GlobalVar.DATABASE_NAME,DatabaseStrings.DATABASE_DEMO).equals(name)) {
                    editor.putString(GlobalVar.DATABASE_NAME,name);
                    editor.apply();
                }

                GlobalVar.getInstance().setDatabaseName(sharedPreferences.getString(GlobalVar.DATABASE_NAME,DatabaseStrings.DATABASE_DEMO));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (buttonView.getId() == R.id.switch_toolbar) {
            if (sharedPreferences.getBoolean(GlobalVar.TOOLBAR_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.TOOLBAR_ENABLED, isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setToolbarEnabled(sharedPreferences.getBoolean(GlobalVar.TOOLBAR_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_bottombar) {
            if (sharedPreferences.getBoolean(GlobalVar.BOTTOMBAR_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.BOTTOMBAR_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setBottombarEnabled(sharedPreferences.getBoolean(GlobalVar.BOTTOMBAR_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_floating) {
            if (sharedPreferences.getBoolean(GlobalVar.FLOATING_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.FLOATING_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setFloatingEnabled(sharedPreferences.getBoolean(GlobalVar.FLOATING_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subblock) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBBLOCK_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBBLOCK_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubBlockEnabled(sharedPreferences.getBoolean(GlobalVar.SUBBLOCK_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subactivity) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBACTIVITY_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBACTIVITY_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubactivityEnabled(sharedPreferences.getBoolean(GlobalVar.SUBACTIVITY_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subtoolbar) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBTOOLBAR_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBTOOLBAR_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubToolbarEnabled(sharedPreferences.getBoolean(GlobalVar.SUBTOOLBAR_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_button_remove) {
            if (sharedPreferences.getBoolean(GlobalVar.BUTTONREMOVE_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.BUTTONREMOVE_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setButtonRemoveEnabled(sharedPreferences.getBoolean(GlobalVar.BUTTONREMOVE_ENABLED,false));
        }
    }
}