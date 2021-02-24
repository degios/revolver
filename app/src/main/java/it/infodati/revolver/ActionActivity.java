package it.infodati.revolver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class ActionActivity extends AppCompatActivity {

    private int id;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        Intent intent = getIntent();
        id = intent.getIntExtra(getResources().getString(R.string.id).toString(),0);

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

        this.loadInterface();
        this.loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadInterface() {
        if (!GlobalVar.getInstance().isSubToolbarEnabled())
            actionBar.hide();

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
/*
        webView.getSettings().getUseWideViewPort();
        webView.setInitialScale(1);
*/
    }

    public void loadData() {
        new ActionActivity.QueryData().execute();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class QueryData extends AsyncTask {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Link model = LinkDao.getLink(id);
            if (model!=null) {
                if (!model.getTitle().toString().isEmpty())
                    toolbar.setTitle(model.getTitle().toString());
                if (!model.getUrl().toString().isEmpty()) {
                    webView.loadUrl(model.getUrl().toString());
                    actionBar.show();
                    progressBar.setMax(100);
                    progressBar.setProgress(1);
                }
            } else {
                actionBar.hide();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            actionBar.show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            actionBar.hide();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    }
}