package com.hopin.uniq.sdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_LINK = "extra_link";
    public static final String EXTRA_BG_COLOR = "extra_bgcolor";
    public static final String EXTRA_BACK_BUTTON_COLOR = "back_button_color";
    public static final String EXTRA_PROGRESS_TEXT = "extra_progress_text";
    WebView webView;
    private String TAG = WebViewActivity.class.getSimpleName();
    String link, bgColor, backButtonColor, progressText;
    View progressMainLayout;
    private TextView textViewProgress;
    ImageView mReloadImageView;
    private boolean networkError;
    private boolean hideProgress = false;

    public static void start(Context context, String link, String color, String backButtonColor, String progressText) {
        Intent intent = getIntent(context, link, color, backButtonColor, progressText);
        context.startActivity(intent);
    }

    @NonNull
    public static Intent getIntent(Context context, String link, String color, String backButtonColor, String progressText) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_LINK, link);
        intent.putExtra(EXTRA_BG_COLOR, color);
        intent.putExtra(EXTRA_PROGRESS_TEXT, progressText);
        intent.putExtra(EXTRA_BACK_BUTTON_COLOR, backButtonColor);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(link);
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setCornerRadius(40);
        try {
            shapeDrawable.setColor(Color.parseColor(bgColor));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


        View progressView = findViewById(R.id.layout_progress_main);
        if (Build.VERSION.SDK_INT >= 16) {
            progressView.setBackground(shapeDrawable);
        } else {
            progressView.setBackgroundDrawable(shapeDrawable);
        }
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable();
            colorDrawable.setColor(Color.parseColor(bgColor));
            actionBar.setBackgroundDrawable(colorDrawable);

            final Drawable upArrow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp, getTheme()) : getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            upArrow.setColorFilter(Color.parseColor(backButtonColor), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(bgColor));
        }

    }

    private void initView() {
        link = getIntent().getStringExtra(EXTRA_LINK);
        bgColor = getIntent().getStringExtra(EXTRA_BG_COLOR);
        backButtonColor = getIntent().getStringExtra(EXTRA_BACK_BUTTON_COLOR);
        progressText = getIntent().getStringExtra(EXTRA_PROGRESS_TEXT);
        webView = findViewById(R.id.webView);
        mReloadImageView = findViewById(R.id.reloadBtn);
        progressMainLayout = findViewById(R.id.layout_progress_main);
        textViewProgress = findViewById(R.id.text_progress);
        textViewProgress.setText(progressText);
    }

    private class MyWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                hideProgress = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hideProgress) {
                            progressMainLayout.setVisibility(View.GONE);
                        } else {
                            progressMainLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }, 1500);
            } else {
                hideProgress = false;
                progressMainLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            networkError = false;
//            progressMainLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "shouldOverrideUrlLoading: " + url);
            view.loadUrl(url);
            webView.setBackgroundColor(getResources().getColor(R.color.white));
            return true;
        }
/*        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: " + url);
            webView.setBackgroundColor(getResources().getColor(R.color.white));
            progressMainLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            if (networkError) {
                return;
            }

            progressMainLayout.setVisibility(View.GONE);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            webView.setVisibility(View.GONE);
            textViewProgress.setText("Network Problem!");
            Log.d(TAG, "onReceivedError: ");
            networkError = true;
            onNetworkError();
//            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.setVisibility(View.GONE);
            textViewProgress.setText("Network Problem2!");
            Log.d(TAG, "onReceivedError2: ");
            networkError = true;
            onNetworkError();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            webView.setVisibility(View.GONE);
            textViewProgress.setText("Network Problem2!");
            Log.d(TAG, "onReceivedHttpError: ");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }*/
    }


    public void reloadWebView(View view) {
        if (webView != null) {
            textViewProgress.setVisibility(View.VISIBLE);
            networkError = false;
            mReloadImageView.setVisibility(View.GONE);
            webView.reload();
        }
    }

    private void onNetworkError() {
        textViewProgress.setVisibility(View.GONE);
        mReloadImageView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
