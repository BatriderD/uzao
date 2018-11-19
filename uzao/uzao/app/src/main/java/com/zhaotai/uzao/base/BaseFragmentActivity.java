package com.zhaotai.uzao.base;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.widget.MultipleStatusView;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : activity的基础类，理论上所有的activity都必须继承自本activity
 */

public abstract class BaseFragmentActivity extends SupportActivity implements BaseView, RxContractInterface {

    private static final String TAG = "ActivityName";

    public Context mContext;
    public RelativeLayout mToolbar;
    public TextView mTitle;
    public ImageView mBack;
    private Unbinder unbinder;
    private UITipDialog mLoadingDialog;

    private float sNonCompatDensity;
    private float sNonCompatScaledDensity;

    protected abstract void initView();

    protected abstract void initData();

    public MultipleStatusView multipleStatusView;

    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void add(Disposable d) {
        compositeDisposable.add(d);
    }

    public void DisposableClear() {
        compositeDisposable.clear();
    }

    @Override
    public void stopRequest() {
        compositeDisposable.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        setCustomDensity(this, getApplication());
        initView();
        initData();
        PushAgent.getInstance(mContext).onAppStart();
        //设置light状态栏
        handleStatusBar();

        String contextString = this.toString();
        Log.d(TAG, contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@")));
    }

    public void handleStatusBar() {
        StatusBarUtil.setLightMode(this);
    }

    /**
     * 适配屏幕
     */
    private void setCustomDensity(BaseFragmentActivity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / GlobalVariable.DESIGN_WIDTH;
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = buildContentView(LayoutInflater.from(this).inflate(layoutResID, null));
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        initTitle();
    }


    private View buildContentView(View view) {
        if (hasBaseLayout()) {
            multipleStatusView = (MultipleStatusView) view.findViewById(R.id.multiple_status_view);
            View.OnClickListener mRetryClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
    protected boolean hasBaseLayout() {
        return false;
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

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        DisposableClear();
        super.onDestroy();

    }
}
