package com.zhaotai.uzao.ui.person.track.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MyTrackBean;
import com.zhaotai.uzao.bean.MyTrackResultBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.track.adapter.MyTrackAdapter;
import com.zhaotai.uzao.ui.person.track.contract.MyTrackContract;
import com.zhaotai.uzao.ui.person.track.presenter.MyTrackPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.decoration.MyTrackItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:  我的足迹页面
 * author : ZP
 * date: 2017/12/1 0001.
 */

public class MyTrackActivity extends BaseActivity implements MyTrackContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.ll_track_product_bottom)
    RelativeLayout mRlTrackBottom;
    @BindView(R.id.iv_track_product_all_choose)
    ImageView mIvAllChoose;

    private MyTrackPresenter mPresenter;
    private MyTrackAdapter mAdapter;

    private PageInfo<MyTrackResultBean> data;
    private Button mToolBarRight;
    private boolean isSelectAll = false;//是否全选
    private boolean isSelectState = false;//默认不可以管理


    public static void launch(Context context) {
        context.startActivity(new Intent(context, MyTrackActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_track);
        mTitle.setText("足迹");
        mToolBarRight = (Button) findViewById(R.id.right_btn);
        mToolBarRight.setVisibility(View.VISIBLE);
        mToolBarRight.setText("管理");

        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        mPresenter = new MyTrackPresenter(this, this);
        ArrayList<MyTrackBean> arrayList = new ArrayList<>();
        mAdapter = new MyTrackAdapter(arrayList);
        mRecycler.setAdapter(mAdapter);

        mSwipe.setOnRefreshListener(this);
        mPresenter = new MyTrackPresenter(this, this);

        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyStateView(this, R.mipmap.ic_status_empty_track, "那么多好东西居然都没看", "去逛逛", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                HomeActivity.launch(mContext,1);
            }
        });
        //设置
        mRecycler.addItemDecoration(new MyTrackItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3), mAdapter));
    }


    @Override
    protected void initData() {
        showLoading();
        mPresenter.getMyTrackList(0, true, new ArrayList<MyTrackBean>(), isSelectAll);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.right_btn)
    public void onClickRight() {
        if (!isSelectState) return;

        if (mToolBarRight.getText().equals("管理")) {
            mAdapter.setManageState(true);
            isSelectAll = false;
            mToolBarRight.setText("取消");
            mRlTrackBottom.setVisibility(View.VISIBLE);
        } else {
            mToolBarRight.setText("管理");
            mAdapter.setManageState(false);
            mIvAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mRlTrackBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 全选
     */
    @OnClick(R.id.ll_track_product_bottom)
    public void onClickAllChoose() {
        if (isSelectAll) {
            mAdapter.setSelected(false);
            mIvAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setSelected(true);
            mIvAllChoose.setImageResource(R.drawable.icon_circle_selected);
            mAdapter.notifyDataSetChanged();
        }
        isSelectAll = !isSelectAll;
    }

    /**
     * 删除
     */
    @OnClick(R.id.tv_track_product_delete)
    public void onClickDelete() {
        if (mAdapter.getData().size() > 0) {
            mPresenter.deleteTrack(mAdapter.getData());
        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getMyTrackList(start, false, mAdapter.getData(), isSelectAll);
            mAdapter.loadMoreComplete();
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
        mPresenter.getMyTrackList(Constant.PAGING_HOME, false, new ArrayList<MyTrackBean>(), false);
        mIvAllChoose.setImageResource(R.drawable.icon_circle_unselected);
        isSelectAll = false;
    }

    /**
     * 停止加载更多状态
     */
    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreEnd();
//        mAdapter.loadMoreComplete();
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
    public void showMyTrackList(List<MyTrackBean> lists, PageInfo<MyTrackResultBean> data) {
        if (data.list.size() > 0) {
            isSelectState = true;
        } else {
            isSelectState = false;
            mToolBarRight.setText("管理");
            mRlTrackBottom.setVisibility(View.GONE);
        }
        this.data = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(lists);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(lists);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyTrackBean myTrackBean = mAdapter.getData().get(position);
        if (mAdapter.getManageState()) {
            myTrackBean.getFootprintModel().setSelected(!myTrackBean.getFootprintModel().isSelected());
            mAdapter.notifyItemChanged(position);
            if (mPresenter.checkSelectState(mAdapter.getData())) {
                mIvAllChoose.setImageResource(R.drawable.icon_circle_selected);
            } else {
                mIvAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            }

        } else {
            MyTrackBean.FootprintModelsBean footprintModel = myTrackBean.getFootprintModel();

            if ("SourceMaterial".equals(footprintModel.getEntityType())) {
                //素材
                MaterialDetailActivity.launch(mContext, footprintModel.getEntityId());
            } else {
                //商品
                CommodityDetailMallActivity.launch(mContext, footprintModel.getEntityId());

            }


        }
    }

}
