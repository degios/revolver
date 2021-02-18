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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.infodati.revolver.LinkActivity;
import it.infodati.revolver.R;
import it.infodati.revolver.adapter.LinksAdapter;
import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.model.Link;

import java.util.List;

public class LinksFragment extends Fragment implements ListItemClickListener, LoadDataFragment, LoadInterfaceFragment {

    private ProgressBar progressBar;
    private LinearLayoutCompat defaultChoice;
    private RecyclerView recyclerView;
    private LinksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_links, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar);
        defaultChoice = view.findViewById(R.id.default_choice);
        recyclerView = view.findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new LinksAdapter(this);
        recyclerView.setAdapter(adapter);

        this.loadData();
    }

    public void loadData() {
        new QueryData().execute();
    }

    public void loadInterface() {}

    @Override
    public void onListItemClick(int position) {
        Link model = (Link) adapter.getItem(position);
        Intent intent = new Intent(getActivity(), LinkActivity.class);
        intent.putExtra(getResources().getString(R.string.id).toString(),model.getId());
        startActivityForResult(intent, 0);
    }

    private class QueryData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.INVISIBLE);
            defaultChoice.setVisibility(View.INVISIBLE);
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
            list = databaseManager.getAllOrderedLinks();
            databaseManager.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
/*
            if (list!=null)
                Snackbar.make( progressBar, "[" + String.valueOf(list.size()) + "] " + " links loaded", Snackbar.LENGTH_LONG)
                        .setAction( "[" + String.valueOf(list.size()) + "] " + " links loaded", null)
                        .show();
*/

            adapter.setAdapterList(list);

            recyclerView.setVisibility(View.VISIBLE);
            defaultChoice.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}