package com.zhaotai.uzao.ui.web;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.StringUtil;

import butterknife.BindView;

/**
 * Time: 2017/9/15
 * Created by LiYou
 * Description :  打开网页
 */

public class WebActivity extends BaseActivity {

    @BindView(R.id.wv_web_view)
    WebView mWebView;

    public static final String EXTRA_KEY_LINK_ADDRESS = "extra_key_link_address";

    public static void launch(Context context, String address) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_KEY_LINK_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(EXTRA_KEY_LINK_ADDRESS);
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        LogUtils.logd(url);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!StringUtil.isEmpty(view.getTitle()))
                    mTitle.setText(view.getTitle());
            }
        });
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
