package com.zhaotai.uzao.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.MultiCustomBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.main.adapter.MainCustomAdapter;
import com.zhaotai.uzao.ui.main.contract.CustomFragmentContract;
import com.zhaotai.uzao.ui.main.presenter.CustomFragmentPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * description: 首页 自定义
 * author : LiYou
 * date: 2018/3/23 0021.
 */

public class CustomFragment extends BaseFragment implements CustomFragmentContract.View, OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private static final String CODE = "code";
    private CustomFragmentPresenter mPresenter;
    private MainCustomAdapter mAdapter;
    private String code;

    public static CustomFragment newInstance(String type) {
        CustomFragment customFragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CODE, type);
        customFragment.setArguments(bundle);
        return customFragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_main_child;
    }

    @Override
    public void initView() {
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 6));
    }

    @Override
    public void initPresenter() {
        mPresenter = new CustomFragmentPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        code = getArguments().getString(CODE);
        autoRefresh();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getData(code);
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void bindData(final List<MultiCustomBean> data) {
        if (mAdapter == null) {
            mAdapter = new MainCustomAdapter(data);
            mAdapter.setOnItemChildClickListener(this);
            mRecycler.setAdapter(mAdapter);
            mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    switch (data.get(position).getItemType()) {
                        case MultiCustomBean.TYPE_CATEGORY:
                            return 2;
                        case MultiCustomBean.TYPE_RECOMMEND_SPU:
                            return 3;
                        default:
                            return 6;
                    }
                }
            });
        } else {
            mAdapter.setNewData(data);
        }
    }

    @Override
    public void notifyItemChange(int startPosition, int count) {
        if (mAdapter != null) {
            mAdapter.notifyItemRangeChanged(startPosition, count);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        MultiCustomBean item = (MultiCustomBean) adapter.getItem(position);
        if (item == null) return;
        switch (view.getId()) {
            case R.id.iv_main_custom_category_pic://分类
                mPresenter.categoryRoute(item);
                break;
            case R.id.iv_main_custom_recommend_spu_pic://商品
                CommodityDetailMallActivity.launch(_mActivity, item.sequenceNBR);
                break;
        }
    }
}
