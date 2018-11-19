package com.zhaotai.uzao.ui.person.myproduct.fragment;


import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MyProductListAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.myProduct.ModifyMyProductEvent;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.person.myproduct.ModifyTemplateDetailActivity;
import com.zhaotai.uzao.ui.person.myproduct.contract.MyProductFragmentContract;
import com.zhaotai.uzao.ui.person.myproduct.presenter.MyProductPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Time: 2017/5/23
 * Created by LiYou
 * Description : 我的商品 -- 审核失败
 */

public class UnApprovedProductFragment extends BaseFragment implements MyProductFragmentContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MyProductPresenter mPresenter;
    private MyProductListAdapter mAdapter;
    //    保存最新的一条请求数据  用于获取是否有下一页等信息
    private PageInfo<GoodsBean> data;
    private String status = "unapproved";


    public static UnApprovedProductFragment newInstance() {
        return new UnApprovedProductFragment();
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
        mAdapter.setOnItemChildClickListener(this);

        mAdapter.disableLoadMoreIfNotFullPage(mRecycler);

        EventBus.getDefault().register(this);
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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        final GoodsBean info = (GoodsBean) adapter.getItem(position);
        if (info == null) return;
        switch (view.getId()) {
            case R.id.tv_my_product_bottom_left_button:
                //删除
                AlertDialog.Builder alert = new AlertDialog.Builder(_mActivity);
                alert.setMessage("是否删除此商品");
                alert.setNeutralButton("取消", null);
                alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击删除
                        mPresenter.deleteSpu(info.sequenceNBR, position, mAdapter);
                    }
                });
                alert.show();
                break;
            case R.id.tv_my_product_bottom_right_button:
                //编辑
                ModifyTemplateDetailActivity.launch(_mActivity, info.sequenceNBR, position);
                break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModifyMyProductEvent modifyMyProductEvent) {
        mPresenter.getMyProductList(Constant.PAGING_HOME, false, status);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
