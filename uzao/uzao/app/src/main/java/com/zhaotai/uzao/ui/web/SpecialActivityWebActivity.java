package com.zhaotai.uzao.ui.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.category.goods.activity.ActivityListActivity;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.category.goods.activity.NavigateProductListActivity;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.utils.LogUtils;

import butterknife.BindView;

/**
 * Time: 2017/9/23
 * Created by LiYou
 * Description :  专题活动
 */

public class SpecialActivityWebActivity extends BaseActivity {

    @BindView(R.id.wv_web_view)
    WebView mWebView;

    public static final String EXTRA_KEY_LINK_ADDRESS = "extra_key_link_address";

    public static void launch(Context context, String address) {
        Intent intent = new Intent(context, SpecialActivityWebActivity.class);
        intent.putExtra(EXTRA_KEY_LINK_ADDRESS, address);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        mTitle.setText("专题活动");
    }

    @Override
    public boolean hasTitle() {
        return super.hasTitle();
    }

    @Override
    protected void initData() {
        final String url = ApiConstants.UZAOCHINA_IMAGE_HOST + getIntent().getStringExtra(EXTRA_KEY_LINK_ADDRESS);
        LogUtils.logd(url);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          super.onPageFinished(view, url);
                                          LogUtils.logd("结束了");
                                      }

                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);
                                          LogUtils.logd("开始了");
                                      }

                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                          gotoPage(request.getUrl());
                                          return true;
                                      }

                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          Uri uri = Uri.parse(url);
                                          gotoPage(uri);
                                          return true;
                                      }
                                  }

        );


        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    private void gotoPage(Uri uri) {
        String path = uri.toString();
        LogUtils.logd(path);
        if (path.startsWith("websitelink")) {
            String url = path.substring(12);
            WebActivity.launch(mContext, url);
            return;
        }
        switch (uri.getLastPathSegment()) {
            case "productDetails"://商品详情
                CommodityDetailMallActivity.launch(mContext, uri.getQueryParameter("key"));
                break;
            case "productDetail"://商品详情
                CommodityDetailMallActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "materialDetail"://素材详情
                MaterialDetailActivity.launch(mContext, uri.getQueryParameter("key"));
                break;
            case "designerHome"://设计师主页
                DesignerActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "topicActivity"://专题活动
                SpecialActivityWebActivity.launch(mContext, uri.getQueryParameter("url"));
                break;
            case "activityProductList"://活动列表
                ActivityListActivity.launch(mContext);
                break;
            case "brand"://品牌主页
                BrandActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "productList"://专题商品列表
                NavigateProductListActivity.launchTopic(mContext, uri.getQueryParameter("type"),
                        uri.getQueryParameter("mongoId"),
                        uri.getQueryParameter("imageId"),
                        uri.getQueryParameter("hotspotId"));
                break;
            case "themeDetail"://主题详情
                ThemeDetailActivity.launch(mContext, uri.getQueryParameter("themeId"));
                break;
        }
    }
}
