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
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.DiscussCenterBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.discuss.adapter.DiscussListAdapter;
import com.zhaotai.uzao.ui.discuss.contract.DiscussCenterDialogContract;
import com.zhaotai.uzao.ui.discuss.presenter.DiscussCenterDialogPresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 评论对话中心
 */

public class DiscussCenterDialogActivity extends BaseActivity implements DiscussCenterDialogContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    private static final String DISCUSS_TYPE = "type";
    private static final String FIRST_COMMENT_ID = "FIRST_COMMENT_ID";
    private static final String MATERIAL_ID = "materialId";
    private static final String PARENT_ID = "parentId";
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.et_talk_word)
    EditText etTalkWord;

    private String materialId;
    private String firstCommentId;
    private DiscussListAdapter mAdapter;
    private DiscussCenterDialogPresenter mPresenter;
    private PageInfo<DiscussBean> data;
    //评论状态  true  对评论进行评论   false 对素材评论
    private boolean status = false;
    private DiscussBean selectedItem;
    private String parentId;
    private String discussType;


    public static void launch(Context context, String discussType, String materialId, String firstCommentId, String parentId) {
        Intent intent = new Intent(context, DiscussCenterDialogActivity.class);
        intent.putExtra(DISCUSS_TYPE, discussType);
        intent.putExtra(FIRST_COMMENT_ID, firstCommentId);
        intent.putExtra(MATERIAL_ID, materialId);
        intent.putExtra(PARENT_ID, parentId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_descuss);
        mTitle.setText("对话内容");

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DiscussListAdapter(2);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);

        mPresenter = new DiscussCenterDialogPresenter(this, this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        discussType = intent.getStringExtra(DISCUSS_TYPE);
        firstCommentId = intent.getStringExtra(FIRST_COMMENT_ID);
        materialId = intent.getStringExtra(MATERIAL_ID);
        parentId = intent.getStringExtra(PARENT_ID);
        showLoading();
//       获取列表信息
        mPresenter.getDiscussList(firstCommentId, 0, true);

    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    /**
     * 评论按钮
     */
    @OnClick(R.id.tv_publish)
    public void publishDiscuss() {
        String word = etTalkWord.getText().toString();
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
            mPresenter.getDiscussList(firstCommentId, start, true);
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
        mPresenter.getDiscussList(firstCommentId, 0, true);
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
    public void showLikeSuccess(int pos) {
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

    /**
     * 评论成功  将评论内容清除 并刷新页面
     */
    @Override
    public void showCommitDiscussSuccess() {
        etTalkWord.setText("");
        changeCommitStatus(false, "");
        mPresenter.getDiscussList(firstCommentId, 0, true);
    }

    @Override
    public void showDelCommentSuccess(int pos) {
        if (pos == 0) {
            mAdapter.setNewData(null);
            EventBus.getDefault().post(new EventMessage(EventBusEvent.DEL_MESSAGE_CENTER));
        } else {
            mAdapter.remove(pos);
        }

    }


    @Override
    public void showCenterList(DiscussCenterBean centerBean) {
        showContent();
        stopRefresh();
        data = centerBean.getChildrenPage();
        if (data.currentPage == 1) {
            DiscussBean parent = centerBean.getParent();
            parent.isFirst = true;
            data.list.add(0, centerBean.getParent());
//            如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(centerBean.getChildrenPage().list);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        String sequenceNBR = mAdapter.getData().get(position).getSequenceNBR();
        switch (view.getId()) {
            case R.id.tv_discuss_like:
                //点赞 /取消点赞
                if (!view.isSelected()) {
                    mPresenter.like(sequenceNBR, position);
                } else {
                    mPresenter.disLike(sequenceNBR, position);
                }
                break;
            case R.id.iv_discuss_all:
                //查看对话详情
                CheckDialogueActivity.launch(mContext, discussType, materialId, sequenceNBR);
                break;

            case R.id.iv_discuss_del:
//                删除评论
                mPresenter.del(discussType, sequenceNBR, position);
                break;
            case R.id.iv_discuss_to_talk:
                //评论具体某一条
                DiscussBean materialDiscussBean = mAdapter.getData().get(position);
                this.selectedItem = materialDiscussBean;
                String userInfo = materialDiscussBean.getUserInfo();

                MaterialDiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, MaterialDiscussBean.DesignInfoBean.class);
                String nickName = designInfoBean.getNickName();
                if (100 == designInfoBean.userType && nickName != null) {
                    changeCommitStatus(true, nickName);
                } else if (300 == designInfoBean.userType) {
                    changeCommitStatus(true, "优造中国");
                }
                break;
        }
    }

    /**
     * 当前提交状态  true 对评论评论  false 对素材评论
     *
     * @param status 对评论进行评论
     */
    private void changeCommitStatus(boolean status, String nickName) {
        this.status = status;
        if (status) {
            etTalkWord.setHint("对 " + nickName + " 评论");
        } else {
            etTalkWord.setHint("对楼主评论");
        }
    }

    /**
     * 提交评论
     * status true  对评论进行评论   false 对素材评论
     *
     * @param word 评论内容
     */
    public void commitDiscuss(String word) {
        if (StringUtil.isEmpty(word)) {
            ToastUtil.showShort("评论内容不能为空");
            return;
        }
        if (status) {
            mPresenter.commitDiscussDiscuss(discussType, materialId, selectedItem.getSequenceNBR(), selectedItem.getFirstCommentId(), word);
        } else {
            mPresenter.commitDiscussDiscuss(discussType, materialId, parentId, firstCommentId, word);
        }
    }

    /**
     * 点击任意条目  收起输入栏
     *
     * @param adapter  adapter
     * @param view     点击的view
     * @param position 点击view的下标
     */

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        KeyboardUtils.hideSoftInput(this);
        changeCommitStatus(false, "");
    }
}
