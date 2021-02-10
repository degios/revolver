package it.infodati.revolver.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import it.infodati.revolver.R;
import it.infodati.revolver.database.DatabaseStrings;
import it.infodati.revolver.util.GlobalVar;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private SwitchCompat switch_connection;
    private AppCompatSpinner spinner_links;
    private SwitchCompat switch_navigationview;
    private SwitchCompat switch_toolbar;
    private SwitchCompat switch_linkbar;
    private SwitchCompat switch_linksbtn;
    private AppCompatSpinner spinner_databases;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch_connection = view.findViewById(R.id.switch_connection);
        spinner_links = view.findViewById(R.id.spinner_links);
        switch_navigationview = view.findViewById(R.id.switch_navigationview);
        switch_toolbar = view.findViewById(R.id.switch_toolbar);
        switch_linkbar = view.findViewById(R.id.switch_linkbar);
        switch_linksbtn = view.findViewById(R.id.switch_linksbtn);
        spinner_databases = view.findViewById(R.id.spinner_databases);

        this.loadSpinnerLinksData();
        this.loadSwitches();
        this.loadSpinnerDatabasesData();

        spinner_links.setOnItemSelectedListener(this);
        spinner_databases.setOnItemSelectedListener(this);
    }

    public void loadSpinnerLinksData() {
/*
        List<Fund> list = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager(getActivity());
            list = databaseManager.getAllOrderedFunds();
            databaseManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
/*
        if (list!=null)
            Snackbar.make( spinner, "[" + String.valueOf(list.size()) + "] " + " funds loaded", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(list.size()) + "] " + " funds loaded", null)
                    .show();
*//*

        if (list!=null) {
            ArrayAdapter adapter = new ArrayAdapter<Fund>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_funds.setAdapter(adapter);

            if (GlobalVar.getInstance().getFundId()>0) {
                Fund model = null;
                try {
                    DatabaseManager databaseManager = new DatabaseManager(getActivity());
                    model = databaseManager.getFund(GlobalVar.getInstance().getFundId());
                    databaseManager.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model!=null) {
                    int position = GlobalVar.getInstance().getListIndexByString((ArrayList<Object>)(ArrayList<?>)(list), model);
*/
/*
                    Snackbar.make( spinner_categories, "[" + String.valueOf(model.getId()) + "][" + String.valueOf(position) + "] " + " fund position", Snackbar.LENGTH_LONG)
                            .setAction( "[" + String.valueOf(position) + "] " + " fund position", null)
                            .show();
*//*

                    if (position>0) {
                        spinner_funds.setSelection(position);
                    }
                }
            }
        }
*/
    }

    public void loadSwitches() {
        // Load switches values (enabled/disabled)
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
            spinner_databases.setAdapter(adapter);

            if (!GlobalVar.getInstance().getDatabaseName().isEmpty()) {
                int position = list.indexOf(GlobalVar.getInstance().getDatabaseName());
                if (position>0) {
                    spinner_databases.setSelection(position);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalVar.getInstance().getPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (parent.getId() == R.id.spinner_funds) {
            Fund model = (Fund) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.FUND_ID,model.getId());
                editor.apply();
*/
/*
            Snackbar.make( view, "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", null)
                    .show();
*//*

                GlobalVar.getInstance().setFundId(sharedPreferences.getInt(GlobalVar.FUND_ID,0));
                loadSpinnerCompartmentsData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parent.getId() == R.id.spinner_compartments) {
            Compartment model = (Compartment) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.FUND_ID,model.getFundId());
                editor.putInt(GlobalVar.COMPARTMENT_ID,model.getId());
                editor.apply();
*/
/*
            Snackbar.make( view, "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", null)
                    .show();
*//*

                GlobalVar.getInstance().setFundId(sharedPreferences.getInt(GlobalVar.FUND_ID,0));
                GlobalVar.getInstance().setCompartmentId(sharedPreferences.getInt(GlobalVar.FUND_ID,0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parent.getId() == R.id.spinner_categories) {
            Category model = (Category) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.CATEGORY_ID,model.getId());
                editor.apply();
*/
/*
            Snackbar.make( view, "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", Snackbar.LENGTH_LONG)
                    .setAction( "[" + String.valueOf(model.getId()) + "] " + model.getDescription() + " selected", null)
                    .show();
*//*

                GlobalVar.getInstance().setCategoryId(sharedPreferences.getInt(GlobalVar.CATEGORY_ID,0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parent.getId() == R.id.spinner_quotation_chart) {
            Chart model = (Chart) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.QUOTATION_CHART_ID,model.getId());
                editor.apply();

                GlobalVar.getInstance().setQuotationChartId(sharedPreferences.getInt(GlobalVar.QUOTATION_CHART_ID, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parent.getId() == R.id.spinner_transaction_chart) {
            Chart model = (Chart) parent.getItemAtPosition(position);
            try {
                editor.putInt(GlobalVar.TRANSACTION_CHART_ID,model.getId());
                editor.apply();

                GlobalVar.getInstance().setTransactionChartId(sharedPreferences.getInt(GlobalVar.TRANSACTION_CHART_ID, 0));
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
*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}