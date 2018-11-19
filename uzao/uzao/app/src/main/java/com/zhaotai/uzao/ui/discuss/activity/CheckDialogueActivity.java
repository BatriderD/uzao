package com.zhaotai.uzao.ui.discuss.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.discuss.adapter.DiscussListAdapter;
import com.zhaotai.uzao.ui.discuss.contract.CheckDialogueContract;
import com.zhaotai.uzao.ui.discuss.presenter.CheckDialoguePresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 查看对话页面
 */

public class CheckDialogueActivity extends BaseActivity implements CheckDialogueContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    private static final String DISCUSS_TYPE = "type";
    private static final String SEQUENCENBR = "sequenceNBR";
    private static final String MATERIAL_ID = "materialId";
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.et_talk_word)
    EditText etTalkWOrd;


    private DiscussListAdapter mAdapter;
    private CheckDialoguePresenter mPresenter;
    private PageInfo<DiscussBean> data;
    private DiscussBean selectedItem;
    private String sequenceNBR;
    private String discussType;
    private String materialId;


    public static void launch(Context context, String discussType, String materialId, String sequenceNBR) {
        Intent intent = new Intent(context, CheckDialogueActivity.class);
        intent.putExtra(SEQUENCENBR, sequenceNBR);
        intent.putExtra(DISCUSS_TYPE, discussType);
        intent.putExtra(MATERIAL_ID, materialId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_descuss);
        mTitle.setText("查看对话");

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DiscussListAdapter(0);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);

        mPresenter = new CheckDialoguePresenter(this, this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        discussType = intent.getStringExtra(DISCUSS_TYPE);
        sequenceNBR = intent.getStringExtra(SEQUENCENBR);
        materialId = intent.getStringExtra(MATERIAL_ID);
        showLoading();
//       获取列表信息
        mPresenter.getDialogue(sequenceNBR, 0, true);

    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @OnClick(R.id.tv_publish)
    public void publishDiscuss() {
        String word = etTalkWOrd.getText().toString();
        KeyboardUtils.hideSoftInput(this);
        commitDiscuss(word);
    }


    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getDialogue(sequenceNBR, start, true);
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
        mPresenter.getDialogue(sequenceNBR, 0, true);
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
    public void showCenterList(PageInfo<DiscussBean> data) {
        showContent();
        stopRefresh();
        this.data = data;
        if (this.data.currentPage == 1) {
//            如果是首页 就设置新数据
            if (data.list.size() > 0) {
                selectedItem = data.list.get(0);
            }
            mAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void showlikeSuccess(int pos) {
        DiscussBean materialDiscussBean = mAdapter.getData().get(pos);
        materialDiscussBean.setUpvote(true);
        materialDiscussBean.setUpvoteCount(materialDiscussBean.getUpvoteCount() + 1);
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void showDisLikeSuccess(int pos) {
        DiscussBean materialDiscussBean = mAdapter.getData().get(pos);
        materialDiscussBean.setUpvote(false);
        materialDiscussBean.setUpvoteCount(materialDiscussBean.getUpvoteCount() - 1);
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void showCommitDiscussSuccess() {
        etTalkWOrd.setText("");
        String userInfo = selectedItem.getUserInfo();
        DiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, DiscussBean.DesignInfoBean.class);
        String nickName = designInfoBean.getNickName();
        changeCommitStatus(nickName);
        mPresenter.getDialogue(sequenceNBR, 0, false);
    }

    @Override
    public void showDelCommentSuccess(int pos) {
        mAdapter.remove(pos);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        String sequenceNBR = mAdapter.getData().get(position).getSequenceNBR();
        switch (view.getId()) {
            case R.id.tv_discuss_like:
                if (!view.isSelected()) {
                    mPresenter.like(sequenceNBR, position);
                } else {
                    mPresenter.disLike(sequenceNBR, position);
                }
                break;
            case R.id.iv_discuss_all:

                break;

            case R.id.iv_discuss_del:
//                删除评论
//                mPresenter.del(discussType, sequenceNBR, position);
                break;
            case R.id.iv_discuss_to_talk:
                DiscussBean materialDiscussBean = mAdapter.getData().get(position);
                this.selectedItem = materialDiscussBean;
                String userInfo = selectedItem.getUserInfo();
                DiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, DiscussBean.DesignInfoBean.class);
                String nickName = designInfoBean.getNickName();
                changeCommitStatus(nickName);
                break;
        }
    }

    /**
     * 当前提交状态  true 对评论评论  false 对素材评论
     */
    private void changeCommitStatus(String nickName) {
        etTalkWOrd.setHint("对 " + nickName + " 评论");

    }

    /**
     * 提交评论
     * @param word
     */
    public void commitDiscuss(String word) {

        mPresenter.commitDiscussDiscuss(discussType, materialId, selectedItem.getSequenceNBR(), selectedItem.getFirstCommentId(), word);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        KeyboardUtils.hideSoftInput(this);
        String userInfo = selectedItem.getUserInfo();
        DiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, DiscussBean.DesignInfoBean.class);
        String nickName = designInfoBean.getNickName();
        changeCommitStatus(nickName);
    }
}
