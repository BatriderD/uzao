package com.zhaotai.uzao.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zhaotai.uzao.app.MyApplication;

/**
 * description:
 * author : ZP
 * date: 2018/4/3 0003.
 */

public abstract class BaseWebViewActivity extends BaseActivity {
    private WebView webView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setWebView(getWebView());
        initWebViewSetting();
    }

    private void setWebView(@NonNull WebView web) {
        this.webView = web;
    }

    public abstract WebView getWebView();

    public void initWebViewSetting() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);

            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            this.deleteDatabase("WebView.db");
            this.deleteDatabase("WebViewCache.db");
            webView.clearCache(true);
            webView.clearFormData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            CookieSyncManager.createInstance(MyApplication.getAppContext());  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
        }
    }
}
