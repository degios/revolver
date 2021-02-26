package it.infodati.revolver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WizardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);
    }
}