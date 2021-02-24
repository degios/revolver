package it.infodati.revolver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import it.infodati.revolver.R;
import it.infodati.revolver.dao.LinkDao;
import it.infodati.revolver.model.Link;
import it.infodati.revolver.util.GlobalVar;

public class ActionFragment extends Fragment implements LoadDataFragment, LoadInterfaceFragment {

    private ProgressBar progressBar;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar);
        webView = view.findViewById(R.id.webview);

        this.loadInterface();
        this.loadData();
    }

    public boolean goBack() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return (GlobalVar.getInstance().isSubBlockEnabled() ? true : false);
    }

    public void loadInterface() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

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

        webView.setWebViewClient(new ActionFragment.WebViewClient());
        webView.setWebChromeClient(new ActionFragment.WebChromeClient());
    }

    public void loadData() {
        new QueryData().execute();
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

            Link model = LinkDao.getLink(GlobalVar.getInstance().getCurrentLinkId());
            if (model!=null) {
/*
                if (!model.getDescription().toString().isEmpty())
                    getActivity().getActionBar().setTitle(model.getDescription().toString());
*/
                if (!model.getUrl().toString().isEmpty()) {
                    webView.loadUrl(model.getUrl().toString());
/*
                    actionBar.show();
*/
                    progressBar.setMax(100);
                    progressBar.setProgress(1);
                }
            } else {
/*
                actionBar.hide();
*/
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
/*
            actionBar.show();
*/
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if( url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
            }
            // Otherwise allow the OS to handle it
            else if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(tel);
            }
            //This is again specific for my website
            else if (url.startsWith("mailto:")) {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.setType("application/octet-stream");
                String AdressMail = new String(url.replace("mailto:" , "")) ;
                mail.putExtra(Intent.EXTRA_EMAIL, new String[]{ AdressMail });
                mail.putExtra(Intent.EXTRA_SUBJECT, "");
                mail.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(mail);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
/*
            actionBar.hide();
*/
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
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }
}