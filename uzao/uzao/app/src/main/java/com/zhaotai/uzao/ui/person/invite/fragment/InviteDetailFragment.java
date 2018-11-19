package com.zhaotai.uzao.ui.person.invite.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.person.invite.adapter.AwardAdapter;
import com.zhaotai.uzao.ui.person.invite.contract.InviteContract;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;
import com.zhaotai.uzao.ui.person.invite.presenter.InvitePresenter;

import butterknife.BindView;

/**
 * Time: 2017/12/1
 * Created by LiYou
 * Description : 邀请明细
 */

public class InviteDetailFragment extends BaseFragment implements InviteContract.DetailView, OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.recycler_invite)
    RecyclerView mRecyclerView;

    //结算
    @BindView(R.id.tv_close_account)
    TextView mCloseAccount;
    //累计
    @BindView(R.id.tv_grand_total)
    TextView mGrandTotal;

    private InvitePresenter mPresenter;
    private AwardAdapter mAdapter;
    private PageInfo<RebateBean> data;

    public static InviteDetailFragment newInstance() {
        return new InviteDetailFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_invite_detail;
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new AwardAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);

        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
    }

    @Override
    public void initPresenter() {
        mPresenter = new InvitePresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        mPresenter.getRebate();
        mPresenter.getInviteDetail(0);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 分享返利明细
     */
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
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getInviteDetail(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showDetail(PageInfo<RebateBean> data) {
        mSwipe.finishRefresh();
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }


    @Override
    public void showRebate(RebateBean rebate) {
        //结算
        mCloseAccount.setText(rebate.rebateMoneY);
        //累计
        mGrandTotal.setText(rebate.totalRebateMoneyY);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getInviteDetail(0);
    }
}
