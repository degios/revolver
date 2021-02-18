package it.infodati.revolver.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import it.infodati.revolver.ActionActivity;
import it.infodati.revolver.LinkActivity;
import it.infodati.revolver.R;
import it.infodati.revolver.adapter.ActionsAdapter;
import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class ActionsFragment extends Fragment implements ListItemClickListener, LoadDataFragment, LoadInterfaceFragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ActionsAdapter adapter;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actions, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar);
        recyclerView = view.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ActionsAdapter(this);
        recyclerView.setAdapter(adapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVar.getInstance().setCurrentLinkId(0);
                Intent intent = new Intent(getActivity(), LinkActivity.class);
                intent.putExtra(getResources().getString(R.string.id).toString(), 0);
                startActivityForResult(intent, 0);
            }
        });

        this.loadData();
        this.loadInterface();
    }

    public void loadData() {
        new QueryData().execute();
    }

    public void loadInterface() {
        if (GlobalVar.getInstance().isFloatingEnabled()) {
            fab.show();
        } else {
            fab.hide();
        }
        if (GlobalVar.getInstance().isToolbarEnabled()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Link model = (Link) adapter.getItem(position);
        GlobalVar.getInstance().setCurrentLinkId(model.getId());
        if (GlobalVar.getInstance().isSubActivityEnabled()) {
            Intent intent = new Intent(getActivity(), ActionActivity.class);
            intent.putExtra(getResources().getString(R.string.id).toString(), model.getId());
            startActivityForResult(intent, 0);
        } else {
            ActionFragment actionFragment = new ActionFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, actionFragment).commit();
        }
    }

    private class QueryData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            List<Link> list = null;
            try {
            DatabaseManager databaseManager =  new DatabaseManager(getActivity());
            list = databaseManager.getAllOrderedActions();
            databaseManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
/*
            if (list!=null)
                Snackbar.make( progressBar, "[" + String.valueOf(list.size()) + "] " + " actions loaded", Snackbar.LENGTH_LONG)
                        .setAction( "[" + String.valueOf(list.size()) + "] " + " actions loaded", null)
                        .show();
*/

            adapter.setAdapterList(list);

            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}