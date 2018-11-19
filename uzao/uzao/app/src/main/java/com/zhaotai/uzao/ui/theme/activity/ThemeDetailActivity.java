package com.zhaotai.uzao.ui.theme.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.discuss.activity.DiscussMainListActivity;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;
import com.zhaotai.uzao.ui.poster.PosterActivity;
import com.zhaotai.uzao.ui.theme.contract.ThemeDetailContract;
import com.zhaotai.uzao.ui.theme.presenter.ThemeDetailPresenter;
import com.zhaotai.uzao.ui.web.WebActivity;
import com.zhaotai.uzao.utils.ColorUtils;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.widget.ScrollWebView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/2/5
 * Created by LiYou
 * Description : 主题详情页面
 */

public class ThemeDetailActivity extends BaseActivity implements ThemeDetailContract.View {

    @BindView(R.id.webView)
    ScrollWebView mWebView;

    @BindView(R.id.tool_bar_right_img)
    ImageView mIvShare;
    @BindView(R.id.iv_like)
    ImageView iv_like;

    @BindView(R.id.tv_publish_post)
    TextView tv_publish_post;
    @BindView(R.id.v_line)
    View v_line;
    @BindView(R.id.v_tool_line)
    View v_tool_line;
    private ThemeDetailPresenter mPresenter;
    private String themeId;
    private String favoriteCount;
    private ThemeBean themeBean;
    private boolean loadError = false;

    /**
     * 启动主题详情页面
     *
     * @param context context
     * @param themeId 主题id
     */
    public static void launch(Context context, String themeId) {
        Intent intent = new Intent(context, ThemeDetailActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_theme_detail);
        mTitle.setText("主题详情");
        mIvShare.setImageResource(R.drawable.ic_share_black);
        mIvShare.setVisibility(View.VISIBLE);
        mPresenter = new ThemeDetailPresenter(this, this);
        mWebView.setListener(new ScrollWebView.OnScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void scrollHeight(int h) {
                float f = (h - 100) * 1.0f / 200;
                if (f < 0) {
                    f = 0;
                }
                if (f > 1) {
                    f = 1;
                }
                if (f == 1) {
                    v_tool_line.setVisibility(View.VISIBLE);
                } else {
                    v_tool_line.setVisibility(View.GONE);
                }
                mToolbar.setBackgroundColor(ColorUtils.setColorAlpha(Color.WHITE, (int) (f * 255)));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //请求收藏状态
        mPresenter.getDetail(themeId);
        mPresenter.getCollectStatus(themeId);
    }

    @Override
    protected void initData() {
        themeId = getIntent().getStringExtra("themeId");
        //请求主题详情
        mPresenter.getDetail(themeId);

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.tool_bar_right_img)
    public void onClickShare() {
        if (LoginHelper.getLoginStatus()) {
            mPresenter.hasPoster(themeId);
        } else {
            LoginHelper.goLogin(this);
        }
    }

//    @OnClick(R.id.tv_manage)
//    public void onClickeManage() {
//        SceneManagerActivity.launch(this, themeBean, false);
//    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         *  SHARE_MEDIA 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         *  分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ThemeDetailActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ThemeDetailActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ThemeDetailActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                //根据key来区分自定义按钮的类型，并进行对应的操作
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_poster")) {
                    PosterActivity.launchTheme(ThemeDetailActivity.this, themeId);
                }
            }
        }
    };

    /**
     * 打开分享面板
     */
    @Override
    public void openShareBoard(boolean hasPoster) {
        SnsPlatform snsPlatform = SHARE_MEDIA.createSnsPlatform("umeng_sharebutton_poster", "umeng_sharebutton_poster", "ic_share_poster", "ic_share_poster", 0);
        ShareAction shareAction = new ShareAction(this)
                .withText("uzao")
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setShareboardclickCallback(shareBoardlistener)
                .setCallback(shareListener);
        if (hasPoster) {
            Class aClass = shareAction.getClass();
            try {
                java.lang.reflect.Field field = aClass.getDeclaredField("platformlist");
                field.setAccessible(true);
                List<SnsPlatform> platformlist = (List<SnsPlatform>) field.get(shareAction);
                platformlist.add(0, snsPlatform);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                shareAction.open();
            }
        } else {
            shareAction.open();
        }

    }


    /**
     * 相应收藏按钮
     */
    @OnClick(R.id.iv_like)
    public void onClickLike() {
        //登录了就可以收藏取消收藏
        if (LoginHelper.getLoginStatus()) {
            if (iv_like.isSelected()) {
                //取消收藏
                mPresenter.delTheme(themeId);
            } else {
                //收藏主题
                mPresenter.collectTheme(themeId);
            }
        } else {
            //去登录
            LoginMsgActivity.launch(mContext);
        }
    }


    @OnClick(R.id.tv_publish_post)
    public void onPublishPost() {
        SceneManagerActivity.launch(this, themeBean, false);
    }

    /**
     * 显示主题详情 主题详情是webView
     *
     * @param themeBean 主题详情bean类
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showDetail(final ThemeBean themeBean) {
        this.themeBean = themeBean;
        tv_publish_post.setVisibility("platform".equals(themeBean.type) && "Y".equals(themeBean.canComment) ? View.VISIBLE : View.GONE);
        v_line.setVisibility(tv_publish_post.getVisibility());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        favoriteCount = themeBean.favoriteCount;
        //标题 设置主题名称
        mTitle.setText(themeBean.name);
        LogUtils.logd(ApiConstants.THEME_TEMPLATE + themeBean.wapUrl);


        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.logd(request.getUrl().toString());
                gotoPage(request.getUrl());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                gotoPage(uri);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript("javascript:commentCount(" + themeBean.commentCount + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                    }
                });
                view.evaluateJavascript("javascript:favoriteCount(" + themeBean.favoriteCount + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                    }
                });
                if (!loadError && v_tool_line != null && mToolbar != null) {
                    v_tool_line.setVisibility(View.GONE);
                    mToolbar.setBackgroundColor(ColorUtils.setColorAlpha(Color.WHITE, 0));
                }
            }
        });
        mWebView.loadUrl(ApiConstants.THEME_TEMPLATE + themeBean.wapUrl);
    }

    /**
     * 根据点击的uri  来获取字段 并判断跳转去哪个页面
     *
     * @param uri 点击的url
     */
    private void gotoPage(Uri uri) {
        String path = uri.toString();
        LogUtils.logd(path);
        if (path.startsWith("websitelink")) {
            String url = path.substring(12);
            WebActivity.launch(mContext, url);
            return;
        }
        LogUtils.logd(uri.getLastPathSegment());
        switch (uri.getLastPathSegment()) {
            case "material"://素材详情
            case "addMaterial"://新增素材详情
                MaterialDetailActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "spu"://商品详情
            case "addSpu"://新增详情
            case "designspu"://可定制商品
                CommodityDetailMallActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "designer"://设计师
                DesignerActivity.launch(mContext, uri.getQueryParameter("id"));
                break;
            case "comment"://评论专区
                if (LoginHelper.getLoginStatus()) {
                    if ("platform".equals(themeBean.type)) {
                        SceneManagerActivity.launch(mContext, themeBean, false);
                    } else {
                        DiscussMainListActivity.launch(mContext, themeId, DiscussMainListActivity.TYPE_THEME);
                    }
                } else {
                    LoginMsgActivity.launch(mContext);
                }
                break;
        }
    }

    /**
     * 根据请求结果 返回收藏状态
     *
     * @param collected true已收藏 ，false 取消收藏
     */
    @Override
    public void showCollectTheme(boolean collected) {
        if (collected) {
            iv_like.setImageResource(R.drawable.ic_collectd);
            iv_like.setSelected(true);
        } else {
            iv_like.setImageResource(R.drawable.ic_un_collect);
            iv_like.setSelected(false);
        }

    }

    /**
     * 改变收藏状态
     *
     * @param isAdd true 改编为收藏  ，false 取消收藏
     */
    @Override
    public void changeFavoriteCount(boolean isAdd) {
        if (mWebView != null && !StringUtil.isEmpty(favoriteCount)) {
            if (isAdd) {
                int add = Integer.valueOf(favoriteCount) + 1;
                this.favoriteCount = String.valueOf(add);
                mPresenter.setFavoriteCount(mWebView, favoriteCount);
            } else {
                int sub = Integer.valueOf(favoriteCount) - 1;
                this.favoriteCount = String.valueOf(sub);
                mPresenter.setFavoriteCount(mWebView, favoriteCount);
            }
        }
    }

}
