package it.infodati.revolver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;

import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.fragment.ActionFragment;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class LinkActivity extends AppCompatActivity {

    private int id;
    private String url;

    private AppCompatEditText editTextUrl;
    private AppCompatEditText editTextTitle;
    private ImageView imageViewIcon;
    private AppCompatButton buttonDelete;
    private AppCompatButton buttonSave;
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        Intent intent = getIntent();
        id = intent.getIntExtra(getResources().getString(R.string.id).toString(),0);
        url = intent.getStringExtra(getResources().getString(R.string.url));

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
        editTextTitle = findViewById(R.id.edittext_title);
        imageViewIcon = findViewById(R.id.imageview_icon);
        buttonDelete = findViewById(R.id.button_delete);
        if (id>0 && GlobalVar.getInstance().isButtonRemoveEnabeld()) {
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonDelete.getLayoutParams().width = 0;
        }
        buttonSave = findViewById(R.id.button_save);

        progressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);

        loadInterface();
        loadData();

        if (id==0) {
            if (url!=null && !url.isEmpty()) {
                editTextUrl.setText(url);
                onDownloadBtnClick(webView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_link, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_remove) {
            if (id>0) {
                Snackbar snackbar = Snackbar
                        .make(editTextUrl, getResources().getText(R.string.confirm_delete), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getText(R.string.yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinkDao.removeLink(id);
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                snackbar.show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void loadInterface() {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);

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

        webView.setWebViewClient(new LinkActivity.WebViewClient());
        webView.setWebChromeClient(new LinkActivity.WebChromeClient());
    }

    private void loadData() {
        Link model = LinkDao.getLink(this.id);
        if (model!=null) {
            editTextUrl.setText(model.getUrl());
            editTextTitle.setText(model.getTitle());

            String iconName = getApplicationContext().getResources().getResourceEntryName(android.R.drawable.ic_menu_myplaces);
            int iconId = getApplicationContext().getResources().getIdentifier(iconName, "drawable", "android");
            byte[] arIcon = model.getIcon();
            if (arIcon!=null && arIcon.length>0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(arIcon, 0, arIcon.length);
                this.imageViewIcon.setImageBitmap(bitmap);
            } else {
                this.imageViewIcon.setImageResource(iconId);
            }
        }
    }

    public void onDownloadBtnClick(View v) {
        String url = editTextUrl.getText().toString();
        if (url!=null && !url.isEmpty() && url.trim().toUpperCase().startsWith("HTTP")) {
            webView.loadUrl(url);
        }
    }

    public void onDeleteBtnClick(View v) {
        if (id>0) {
            Snackbar snackbar = Snackbar
                    .make(v, getResources().getText(R.string.confirm_delete), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getText(R.string.yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LinkDao.removeLink(id);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    });
            snackbar.show();
        }
    }

    public void onSaveBtnClick(View v) {
        int id;
        if (this.id>0) {
            id = this.id;
        } else {
            id = LinkDao.getLastLink().getId()+1;
        }


        LinkDao.createLink(
                new Link(
                        id,
                        editTextUrl.getText().toString(),
                        editTextTitle.getText().toString(),
                        getByteArrayFromImageView(imageViewIcon)));

        setResult(Activity.RESULT_OK);
        this.finish();
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if( url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
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
            editTextTitle.setText(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            if (icon!=null) {
                imageViewIcon.setImageBitmap(icon);
            }
        }
    }
}