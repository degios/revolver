package it.infodati.revolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class WizardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private WebView webView;
    private MenuItem menuItemAdd;
    private LinearLayoutCompat layoutLink;
    private AppCompatEditText editTextUrl;
    private AppCompatEditText editTextTitle;
    private ImageView imageViewIcon;
    private SwitchCompat switchBookmark;
    private SwitchCompat switchAutorun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.wizard);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);

        layoutLink = findViewById(R.id.layout_link);
        editTextUrl = findViewById(R.id.edittext_url);
        editTextTitle = findViewById(R.id.edittext_title);
        imageViewIcon = findViewById(R.id.imageview_icon);
        switchBookmark = findViewById(R.id.switch_bookmark);
        switchAutorun = findViewById(R.id.switch_autorun);

        this.loadInterface();
        this.loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wizard,menu);
        menuItemAdd = menu.findItem(R.id.menu_add);
        menuItemAdd.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_add) {
            layoutLink.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            return true;
        } else if (itemId == R.id.menu_search) {
            loadData();
            return true;
        } else if (itemId == R.id.menu_refresh) {
            webView.reload();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.goBack();
    }

    public boolean goBack() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return false;
    }

    public void loadInterface() {
        layoutLink.setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(GlobalVar.getInstance().isSubZoomEnabled());
        webSettings.setSupportZoom(true);

/*
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
*/

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }

        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);

        //Cookie manager for the webview
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        //        webView.getSettings().getUseWideViewPort();

        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
//        webView.setInitialScale(1);

        webView.setWebViewClient(new WizardActivity.WebViewClient());
        webView.setWebChromeClient(new WizardActivity.WebChromeClient());
    }

    public void loadData() {
        webView.loadUrl("https://www.google.com");
    }

    public void onCancelBtnClick(View v) {
        layoutLink.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    public void onSaveBtnClick(View v) {
        Link link = null;
        int id;

        if (GlobalVar.isOverQuota(getApplicationContext())) {
            Snackbar.make( toolbar, getResources().getString(R.string.over_quota), Snackbar.LENGTH_LONG)
                    .setAction( getResources().getString(R.string.over_quota), null)
                    .show();
        } else {
            id = LinkDao.getLastLink().getId() + 1;
            if (switchAutorun.isChecked()) {
                link = LinkDao.getAutorunLink();
                if (link != null && link.getId() > 0) {
                    link.setAutorun(false);
                    LinkDao.createLink(link);
                }
            }

            link = new Link(
                    id,
                    editTextUrl.getText().toString(),
                    editTextTitle.getText().toString(),
                    switchBookmark.isChecked(),
                    switchAutorun.isChecked(),
                    getByteArrayFromImageView(imageViewIcon));
            LinkDao.createLink(link);

            setResult(Activity.RESULT_OK);
            this.finish();
        }
    }

    private byte[] getByteArrayFromImageView(ImageView view) {
        byte[] arIcon = null;

        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        arIcon = stream.toByteArray();
        if (arIcon!=null && arIcon.length<=0)
            arIcon = null;

        return arIcon;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            menuItemAdd.setVisible(false);
            layoutLink.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            menuItemAdd.setVisible(false);
            layoutLink.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            editTextUrl.setText("");
            editTextTitle.setText("");
            imageViewIcon.setImageBitmap(null);

            if( url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            if (icon!=null) {
                editTextUrl.setText(view.getUrl());
                editTextTitle.setText(view.getTitle());
                imageViewIcon.setImageBitmap(icon);

                if (!(editTextUrl.getText().toString().isEmpty() || editTextTitle.getText().toString().isEmpty())) {
                    menuItemAdd.setVisible(true);
                }

                progressBar.setVisibility(View.GONE);
            }
        }
    }
}