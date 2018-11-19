package com.zhaotai.uzao.ui.person.myproduct.fragment;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MyProductListAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.person.myproduct.contract.MyProductFragmentContract;
import com.zhaotai.uzao.ui.person.myproduct.presenter.MyProductPresenter;

import butterknife.BindView;

/**
 * Time: 2017/9/12
 * Created by LiYou
 * Description : 我的商品--审核中
 */

public class CheckProductFragment extends BaseFragment implements MyProductFragmentContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MyProductPresenter mPresenter;
    private MyProductListAdapter mAdapter;
    //保存最新的一条请求数据  用于获取是否有下一页等信息
    private PageInfo<GoodsBean> data;
    private String status = "waitApprove";

    public static CheckProductFragment newInstance() {
        return new CheckProductFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.view_recycle_view;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new MyProductListAdapter(getActivity());
        mRecycler.setAdapter(mAdapter);

        //下拉刷新
        mSwipe.setOnRefreshListener(this);
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.disableLoadMoreIfNotFullPage(mRecycler);
        //设置空页面
        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_1, "该状态下无商品", getString(R.string.empty_btn), new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                HomeActivity.launch(_mActivity, 1);
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new MyProductPresenter(this, getActivity());
    }

    @Override
    public void initData() {
//        请求页面数据
        showLoading();
        mPresenter.getMyProductList(Constant.PAGING_HOME, true, status);
    }

    /**
     * 是否有状态页面
     *
     * @return true 有状态页面
     */
    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @Override
    public void showMyProductList(PageInfo<GoodsBean> goodsBeanPageInfo) {
        data = goodsBeanPageInfo;
        if (goodsBeanPageInfo.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(goodsBeanPageInfo.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(goodsBeanPageInfo.list);
        }
    }

    @Override
    public void changeStatus(String status, int position) {
        mAdapter.getData().get(position).status = status;
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void showTip(String tip) {

    }

    @Override
    public void showExpireTip(String spuId, String id, int position) {

    }

    @Override
    public void renewSuccess() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getMyProductList(start, false, status);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        刷新页面
        mPresenter.getMyProductList(Constant.PAGING_HOME, false, status);
    }

    /**
     * 停止加载更多状态
     */
    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    /**
     * 停止刷新状态
     */
    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }


}
