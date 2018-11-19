package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.MyPicBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.StickerPicInfoBean;
import com.zhaotai.uzao.bean.UpDesignPictureBean;
import com.zhaotai.uzao.ui.design.adapter.MyPicAdapter;
import com.zhaotai.uzao.ui.design.contract.MyPictureContract;
import com.zhaotai.uzao.ui.design.presenter.MyPicturePresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * description: 我的图片页面
 * author : ZP
 * date: 2017/11/23 0023.
 */

public class HistoryPictureActivity extends BaseActivity implements MyPictureContract.View, BaseQuickAdapter.RequestLoadMoreListener,
        OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    private MyPicturePresenter myPicturePresenter;
    private MyPicAdapter mAdapter;
    private PageInfo<MyPicBean> data;

    /**
     * 调起本页面
     */

    public static void launch(Context context) {
        Intent intent = new Intent(context, HistoryPictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_picture);
        mTitle.setText("历史图片");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyPicAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        myPicturePresenter = new MyPicturePresenter(this, this);
        mAdapter.setOnItemClickListener(this);
        mSwipe.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
    }

    @Override
    protected void initData() {
        showLoading();
        myPicturePresenter.getMyPictureList(0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
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
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            myPicturePresenter.getMyPictureList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showDesignList(PageInfo<MyPicBean> data) {
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
    public boolean hasTitle() {
        return true;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        myPicturePresenter.getMyPictureList(0, false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyPicBean myPicBean = mAdapter.getData().get(position);
        String meta = myPicBean.getMeta();
        UpDesignPictureBean pictureBean = GsonUtil.getGson().fromJson(meta, UpDesignPictureBean.class);
        if ("Y".equals(pictureBean.getIsResize())) {
            EventBus.getDefault().post(new EventBean<>(new StickerPicInfoBean(pictureBean.getResizeImage(), Float.valueOf(pictureBean.getResizeScale())), EventBusEvent.RECEIVE_NETWORK_PICTURE_INFO));
        } else {
            EventBus.getDefault().post(new EventBean<>(new StickerPicInfoBean(pictureBean.getFileId(), Float.valueOf(pictureBean.getResizeScale())), EventBusEvent.RECEIVE_NETWORK_PICTURE_INFO));
        }
        finish();
    }
}
