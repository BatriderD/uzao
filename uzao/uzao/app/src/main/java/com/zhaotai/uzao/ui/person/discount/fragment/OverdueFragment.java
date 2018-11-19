package com.zhaotai.uzao.ui.person.discount.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MyDiscountAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.person.discount.contract.MyDiscountCouponContract;
import com.zhaotai.uzao.ui.person.discount.presenter.MyDiscountPresenter;

import butterknife.BindView;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description : 过期的优惠券
 */

public class OverdueFragment extends BaseFragment implements MyDiscountCouponContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MyDiscountAdapter mAdapter;
    private MyDiscountPresenter mPresenter;
    private PageInfo<DiscountCouponBean> data;

    public static OverdueFragment newInstance() {
        return new OverdueFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_discount;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new MyDiscountAdapter(GlobalVariable.COUPON_INVALID);
        mRecycler.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycler);
        //下拉刷新
        mSwipe.setOnRefreshListener(this);

        //设置空页面
        mAdapter.setEmptyView(R.layout.vw_discount_empty);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
    }

    @Override
    public void initPresenter() {
        mPresenter = new MyDiscountPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getOverdueDiscountList(0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getOverdueDiscountList(0, false);
    }


    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }


    @Override
    public void showDiscountList(PageInfo<DiscountCouponBean> data) {
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getOverdueDiscountList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
