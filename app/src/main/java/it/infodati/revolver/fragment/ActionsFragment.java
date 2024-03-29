package it.infodati.revolver.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.infodati.revolver.ActionActivity;
import it.infodati.revolver.LinkActivity;
import it.infodati.revolver.R;
import it.infodati.revolver.WizardActivity;
import it.infodati.revolver.adapter.ActionsAdapter;
import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.listener.ListItemClickListener;
import it.infodati.revolver.listener.ListItemLongClickListener;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;
import it.infodati.revolver.util.Utility;

public class ActionsFragment extends Fragment implements ListItemClickListener, ListItemLongClickListener, LoadDataFragment, LoadInterfaceFragment {

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
        recyclerView.setLayoutManager(
                GlobalVar.getInstance().isGridViewEnabled() ? new GridLayoutManager(view.getContext(), Utility.calculateNoOfColumns(getContext(),150)) :  new LinearLayoutManager(view.getContext()));
        adapter = new ActionsAdapter(this,
                GlobalVar.getInstance().isGridViewEnabled() ? ActionsAdapter.GRIDVIEW : ActionsAdapter.LISTVIEW);
        recyclerView.setAdapter(adapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalVar.getInstance().isWizardEnabled())
                    GlobalVar.getInstance().setCurrentLinkId(0);
                if (GlobalVar.isOverQuota(getActivity().getApplicationContext())) {
                    Snackbar.make( progressBar, getResources().getString(R.string.over_quota), Snackbar.LENGTH_LONG)
                            .setAction( getResources().getString(R.string.over_quota), null)
                            .show();
                } else {
                    Intent intent = new Intent(getActivity(), GlobalVar.getInstance().isWizardEnabled() ? WizardActivity.class : LinkActivity.class);
                    intent.putExtra(getResources().getString(R.string.id).toString(), 0);
                    startActivityForResult(intent, 0);
                }
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (GlobalVar.getInstance().isGridViewEnabled())
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utility.calculateNoOfColumns(getContext(),150));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (GlobalVar.getInstance().isGridViewEnabled())
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(Utility.calculateNoOfColumns(getContext(),150));
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

    @Override
    public boolean onListItemLongClick(int position) {
        Link model = (Link) adapter.getItem(position);
/*
        Snackbar snackbar = Snackbar
                .make(recyclerView, getResources().getText(R.string.confirm_delete), Snackbar.LENGTH_LONG)
                .setAction(getResources().getText(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            DatabaseManager databaseManager = new DatabaseManager(getActivity());
                            databaseManager.removeLink(model.getId());
                            databaseManager.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loadData();
                    }
                });
        snackbar.show();
*/
/*
        Snackbar snackbar = Snackbar
                .make(recyclerView, getResources().getText(R.string.confirm_modify), Snackbar.LENGTH_LONG)
                .setAction(getResources().getText(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
*/
                        GlobalVar.getInstance().setCurrentLinkId(model.getId());
                        Intent intent = new Intent(getActivity(), LinkActivity.class);
                        intent.putExtra(getResources().getString(R.string.id).toString(),model.getId());
                        startActivityForResult(intent, 0);
/*
                    }
                });
        snackbar.show();
*/

        return true;
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

            List<Link> list = LinkDao.getAllOrderedLinks();
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