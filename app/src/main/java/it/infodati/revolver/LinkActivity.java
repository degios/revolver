package it.infodati.revolver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import it.infodati.revolver.database.DatabaseManager;
import it.infodati.revolver.model.Link;
import com.google.android.material.snackbar.Snackbar;

public class LinkActivity extends AppCompatActivity {

    private int id;

    private AppCompatEditText editTextUrl;
    private AppCompatEditText editTextDescription;
    private AppCompatEditText editTextNote;
    private AppCompatButton buttonDelete;
    private AppCompatButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        Intent intent = getIntent();
        id = intent.getIntExtra(getResources().getString(R.string.id).toString(),0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (id>0) {
            toolbar.setTitle(R.string.modify_link);
        } else {
            toolbar.setTitle(R.string.add_link);
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editTextUrl = findViewById(R.id.edittext_url);
        editTextDescription = findViewById(R.id.edittext_description);
        editTextNote = findViewById(R.id.edittext_note);
        buttonDelete = findViewById(R.id.button_delete);
        if (id>0) {
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
        }
        buttonSave = findViewById(R.id.button_save);

        loadData();
    }

    private void loadData() {
        Link model = null;
        try {
            DatabaseManager databaseManager = new DatabaseManager(this);
            model = databaseManager.getLink(this.id);
            databaseManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (model!=null) {
            editTextUrl.setText(model.getUrl());
            editTextDescription.setText(model.getDescription());
            editTextNote.setText(model.getNote());
        }
    }

    public void onDeleteBtnClick(View v) {
        Snackbar snackbar = Snackbar
                .make(v, getResources().getText(R.string.confirm_delete), Snackbar.LENGTH_LONG)
                .setAction(getResources().getText(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                            databaseManager.removeLink(id);
                            databaseManager.close();

                            setResult(Activity.RESULT_OK);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        snackbar.show();
    }

    public void onSaveBtnClick(View v) {
        int id;
        try {
            DatabaseManager databaseManager = new DatabaseManager(this);
            if (this.id>0) {
                id = this.id;
            } else {
                id = databaseManager.getLastLink().getId()+1;
            }
            databaseManager.createLink(
                    new Link(id, editTextUrl.getText().toString(), editTextDescription.getText().toString(), editTextNote.getText().toString()));
            databaseManager.close();

            setResult(Activity.RESULT_OK);
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}