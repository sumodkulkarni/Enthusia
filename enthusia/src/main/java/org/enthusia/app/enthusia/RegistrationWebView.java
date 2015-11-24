package org.enthusia.app.enthusia;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.enthusia.app.R;

public class RegistrationWebView extends ActionBarActivity {

    private static final String URL = "https://docs.google.com/forms/d/1YobpMp8n1bl7QcRSAg_jg2JqBV88-s68GrjdKJRFatw/viewform?c=0&w=1";
    private WebView webView;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_web_view);


        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            RegistrationWebView.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            RegistrationWebView.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registration_web_view, menu);
        return true;
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }

}
