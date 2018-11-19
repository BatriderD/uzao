package com.zhaotai.uzao.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.widget.MultipleStatusView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Time: 2017/5/9
 * Created by LiYou
 * Description :
 */

public abstract class BaseFragment extends SupportFragment implements BaseView {

    private Unbinder unbinder;
    public MultipleStatusView multipleStatusView;

    protected abstract int layoutId();

    public abstract void initView();

    public abstract void initPresenter();

    public abstract void initData();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initStateView(view);
        return view;
    }

    /**
     * 初始化状态页
     *
     * @param view 根页面
     */
    private void initStateView(View view) {
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

    /**
     * 是否开启懒加载
     *
     * @return true 开启 false 关闭
     */
    protected boolean hasLazy() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!hasLazy()) {
            initView();
            initPresenter();
            initData();
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (hasLazy()) {
            initView();
            initPresenter();
            initData();
        }
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

    @Override
    public void onDestroyView() {
        if (null != unbinder) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }


}
