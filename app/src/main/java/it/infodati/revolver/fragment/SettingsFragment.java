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
import it.infodati.revolver.database.DatabaseStrings;
import it.infodati.revolver.util.GlobalVar;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switchToolbar;
    private SwitchCompat switchBottombar;
    private SwitchCompat switchFloating;
    private SwitchCompat switchSwipe;
    private SwitchCompat switchWizard;
    private SwitchCompat switchGridView;
    private SwitchCompat switchSubBlock;
    private SwitchCompat switchSubZoom;
    private SwitchCompat switchSubSwipe;
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
        switchSwipe = view.findViewById(R.id.switch_swipe);
        switchWizard = view.findViewById(R.id.switch_wizard);
        switchGridView = view.findViewById(R.id.switch_gridview);
        switchSubBlock = view.findViewById(R.id.switch_subblock);
        switchSubZoom = view.findViewById(R.id.switch_subzoom);
        switchSubSwipe = view.findViewById(R.id.switch_subswipe);
        switchSubActivity = view.findViewById(R.id.switch_subactivity);
        switchSubToolbar = view.findViewById(R.id.switch_subtoolbar);
        switchButtonRemove = view.findViewById(R.id.switch_button_remove);
        spinnerDatabases = view.findViewById(R.id.spinner_databases);

        this.loadSwitches();
        this.loadSpinnerDatabasesData();

        switchToolbar.setOnCheckedChangeListener(this);
        switchBottombar.setOnCheckedChangeListener(this);
        switchFloating.setOnCheckedChangeListener(this);
        switchSwipe.setOnCheckedChangeListener(this);
        switchWizard.setOnCheckedChangeListener(this);
        switchGridView.setOnCheckedChangeListener(this);
        switchSubBlock.setOnCheckedChangeListener(this);
        switchSubZoom.setOnCheckedChangeListener(this);
        switchSubSwipe.setOnCheckedChangeListener(this);
        switchSubActivity.setOnCheckedChangeListener(this);
        switchSubToolbar.setOnCheckedChangeListener(this);
        switchButtonRemove.setOnCheckedChangeListener(this);
        spinnerDatabases.setOnItemSelectedListener(this);
    }

    public void loadSwitches() {
        this.switchToolbar.setChecked(GlobalVar.getInstance().isToolbarEnabled());
        this.switchBottombar.setChecked(GlobalVar.getInstance().isBottombarEnabled());
        this.switchFloating.setChecked(GlobalVar.getInstance().isFloatingEnabled());
        this.switchSwipe.setChecked(GlobalVar.getInstance().isSwipeEnabled());
        this.switchWizard.setChecked(GlobalVar.getInstance().isWizardEnabled());
        this.switchGridView.setChecked(GlobalVar.getInstance().isGridViewEnabled());
        this.switchSubBlock.setChecked(GlobalVar.getInstance().isSubBlockEnabled());
        this.switchSubZoom.setChecked(GlobalVar.getInstance().isSubZoomEnabled());
        this.switchSubSwipe.setChecked(GlobalVar.getInstance().isSubSwipeEnabled());
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

        if (parent.getId() == R.id.spinner_databases) {
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
        } else if (buttonView.getId() == R.id.switch_swipe) {
            if (sharedPreferences.getBoolean(GlobalVar.SWIPE_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SWIPE_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSwipeEnabled(sharedPreferences.getBoolean(GlobalVar.SWIPE_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_wizard) {
            if (sharedPreferences.getBoolean(GlobalVar.WIZARD_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.WIZARD_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setWizardEnabled(sharedPreferences.getBoolean(GlobalVar.WIZARD_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_gridview) {
            if (sharedPreferences.getBoolean(GlobalVar.GRIDVIEW_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.GRIDVIEW_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setGridviewEnabled(sharedPreferences.getBoolean(GlobalVar.GRIDVIEW_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subblock) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBBLOCK_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBBLOCK_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubBlockEnabled(sharedPreferences.getBoolean(GlobalVar.SUBBLOCK_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subzoom) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBZOOM_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBZOOM_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubZoomEnabled(sharedPreferences.getBoolean(GlobalVar.SUBZOOM_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subswipe) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBSWIPE_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBSWIPE_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubSwipeEnabled(sharedPreferences.getBoolean(GlobalVar.SUBSWIPE_ENABLED,false));
        } else if (buttonView.getId() == R.id.switch_subactivity) {
            if (sharedPreferences.getBoolean(GlobalVar.SUBACTIVITY_ENABLED,false)!=isChecked) {
                editor.putBoolean(GlobalVar.SUBACTIVITY_ENABLED,isChecked);
                editor.apply();
            }
            GlobalVar.getInstance().setSubActivityEnabled(sharedPreferences.getBoolean(GlobalVar.SUBACTIVITY_ENABLED,false));
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