package com.zhaotai.uzao.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.widget.MultipleStatusView;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : activity的基础类，理论上所有的activity都必须继承自本activity
 */

public abstract class BaseActivity extends BaseTopActivity implements BaseView {

    private static final String TAG = "ActivityName";
    public Context mContext;
    public RelativeLayout mToolbar;
    public TextView mTitle;
    public ImageView mBack;
    private Unbinder unbinder;
    private UITipDialog mLoadingDialog;

    protected abstract void initView();

    protected abstract void initData();

    public MultipleStatusView multipleStatusView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
        handleStatusBar();
        PushAgent.getInstance(mContext).onAppStart();
        String contextString = this.toString();
        Log.d(TAG, contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@")));
    }

    // 再次方法处理 状态栏相关问题
    public void handleStatusBar() {
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = buildContentView(LayoutInflater.from(this).inflate(layoutResID, null));
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        initTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        unbinder.unbind();
    }

    private View buildContentView(View view) {
        if (hasBaseLayout()) {
            multipleStatusView = (MultipleStatusView) view.findViewById(R.id.multiple_status_view);
            View.OnClickListener mRetryClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (multipleStatusView != null) {
                        switch (multipleStatusView.getViewStatus()) {
                            case MultipleStatusView.STATUS_ERROR://错误页面
                                onErrorState();
                                break;
                            case MultipleStatusView.STATUS_NO_NETWORK://没有网络页面
                                onNoNetworkState();
                                break;
                            case MultipleStatusView.STATUS_EMPTY://没有数据页面
                                onEmptyState();
                                break;
                        }
                    }
                }
            };
            multipleStatusView.setOnRetryClickListener(mRetryClickListener);
        }
        return view;
    }

    public void initTitle() {
        if (hasTitle()) {
            mToolbar = (RelativeLayout) findViewById(R.id.toolbar);
            mTitle = (TextView) findViewById(R.id.tool_title);
            mBack = (ImageView) findViewById(R.id.tool_back);
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * @return true 有title
     */
    public boolean hasTitle() {
        return true;
    }

    /**
     * 是否包含基本view如progress、empty、error等.
     *
     * @return true 包含 false 不包含
     */
    protected abstract boolean hasBaseLayout();

    /**
     * 错误页面
     */
    public void onErrorState() {
        initData();
    }

    /**
     * 没有网络页面
     */
    public void onNoNetworkState() {
        initData();
    }

    /**
     * 空页面
     */
    public void onEmptyState() {
    }


    @Override
    public void showNetworkFail(String msg) {
        if (multipleStatusView != null) {
            if (this.getString(R.string.no_net).equals(msg)) {
                multipleStatusView.showNoNetwork();
            } else {
                multipleStatusView.showError();
            }
        }
    }

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showEmpty(String emptyText) {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty(emptyText);
        }
    }

    @Override
    public void showLoading() {
        if (multipleStatusView != null) {
            multipleStatusView.showLoading();
        }
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }


    /**
     * 显示关闭loadingDialog
     *
     * @param msg 自定义文字
     */
    public void showLoadingDialog(String msg) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new UITipDialog.Builder(this)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        mLoadingDialog.show();
    }

    public void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new UITipDialog.Builder(this)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.loading))
                .create();
        mLoadingDialog.show();
    }

    /**
     * 关闭loadingDialog
     */
    public void disMisLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
