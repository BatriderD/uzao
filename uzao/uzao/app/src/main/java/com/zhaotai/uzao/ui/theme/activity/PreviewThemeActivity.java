package com.zhaotai.uzao.ui.theme.activity;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseWebViewActivity;
import com.zhaotai.uzao.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/2/5
 * Created by LiYou
 * Description : 预览主题
 */

public class PreviewThemeActivity extends BaseWebViewActivity {

    @BindView(R.id.webView)
    WebView mWebView;

    private String url;
    private int pos;

    public static void launch(Activity context, int pos, String url) {
        Intent intent = new Intent(context, PreviewThemeActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("pos", pos);
        context.startActivityForResult(intent, 1);
    }

    public static void launch(Activity context, String url) {
        Intent intent = new Intent(context, PreviewThemeActivity.class);
        intent.putExtra("url", url);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_preview_theme);
        mTitle.setText("主题预览");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        pos = intent.getIntExtra("pos", -1);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.logd(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());


    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 应用
     */
    @OnClick(R.id.right_btn)
    public void onClickSelectedTemplate() {
        Intent intent = new Intent();
        intent.putExtra("pos", pos);
        setResult(1, intent);
        finish();
    }

    @Override
    public WebView getWebView() {
        return mWebView;
    }
}
