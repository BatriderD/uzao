package com.zhaotai.uzao.ui.person.message.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MessageDetailAdapter;
import com.zhaotai.uzao.adapter.MessageMessageDetailAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.MessageDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.person.message.contract.MessageDetailContract;
import com.zhaotai.uzao.ui.person.message.presenter.MessageDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息详情页
 */

public class MessageDetailActivity extends BaseFragmentActivity implements MessageDetailContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MessageDetailPresenter messageDetailPresenter;
    private BaseQuickAdapter<MessageDetailBean, BaseViewHolder> mAdapter;
    private String pageType;

    private PageInfo<MessageDetailBean> mData;

    public static void launch(Context context, String messageType) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra(GlobalVariable.MESSAGE_PAGE_TYPE, messageType);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        pageType = getIntent().getStringExtra(GlobalVariable.MESSAGE_PAGE_TYPE);
//        根据类型设置标题
        if (GlobalVariable.COMMENT_MESSAGE.equals(pageType)) {
            mAdapter = new MessageMessageDetailAdapter();
        } else {
            mAdapter = new MessageDetailAdapter();
        }

        mRecycler.setAdapter(mAdapter);

        //下拉刷新
        mSwipe.setOnRefreshListener(this);
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        //设置空页面
        mAdapter.setEmptyView(R.layout.vw_empty);
        mAdapter.disableLoadMoreIfNotFullPage(mRecycler);
        messageDetailPresenter = new MessageDetailPresenter(this, this);
    }

    @Override
    protected void initData() {
        pageType = getIntent().getStringExtra(GlobalVariable.MESSAGE_PAGE_TYPE);
//        根据类型设置标题
        if (GlobalVariable.NOTIFICATION_EMESSAGE.equals(pageType)) {
            mTitle.setText(R.string.notification_messages);
        } else if (GlobalVariable.COMMENT_MESSAGE.equals(pageType)) {
            mTitle.setText(R.string.comment_messages);
            mAdapter.setOnItemClickListener(this);
        } else {
            mTitle.setText(R.string.system_messages);
        }
//显示login界面
        showLoading();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentList(0, true);
    }

    @Override
    public void showMessageList(PageInfo<MessageDetailBean> data) {
        mData = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (mData.hasNextPage) {
            int start = mData.pageStartRow + mData.pageRecorders;
            //加载列表数据
            getCurrentList(start, false);
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
        getCurrentList(0, false);
    }

    /**
     * 请求列表数据
     *
     * @param start  列表起始
     * @param status 是否是loading状态页面
     */
    private void getCurrentList(int start, boolean status) {
        if (GlobalVariable.COMMENT_MESSAGE.equals(pageType)) {
            messageDetailPresenter.getCommentMessageList(pageType, start, status);
        } else {
            messageDetailPresenter.getMessageList(pageType, start, status);
        }
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
            mSwipe.finishRefresh(false);
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.REFRESH_UNREAD_COUNT;
        EventBus.getDefault().post(info);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (GlobalVariable.NOTIFICATION_EMESSAGE.equals(pageType)) {
            mTitle.setText(R.string.notification_messages);
        } else if (GlobalVariable.COMMENT_MESSAGE.equals(pageType)) {
            MessageDetailBean messageDetailBean = mAdapter.getData().get(position);
            MessageCommentActivity.launch(mContext, "sourceMaterial", messageDetailBean.getEntityId(), messageDetailBean.getSequenceNBR());
        } else {
            mTitle.setText(R.string.system_messages);
        }
    }
}
