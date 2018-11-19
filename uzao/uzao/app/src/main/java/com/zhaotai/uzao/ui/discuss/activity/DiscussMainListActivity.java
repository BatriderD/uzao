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
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.discuss.adapter.DiscussListAdapter;
import com.zhaotai.uzao.ui.discuss.contract.DiscussMainListContract;
import com.zhaotai.uzao.ui.discuss.presenter.DiscussMainListPresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 评论主列表页
 */

public class DiscussMainListActivity extends BaseActivity implements DiscussMainListContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    private static final String DISCUSS_ID = "discuss_id";
    private static final String DISCUSS_TYPE = "type";
    private static final String USER_ID = "USER_ID";
    public static final String TYPE_MATERIAL = "sourceMaterial";
    public static final String TYPE_THEME = "theme";
    public static final String TYPE_BRAND = "brand";

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.et_talk_word)
    EditText etTalkWOrd;

    private String discussObjId;
    private DiscussListAdapter mAdapter;
    private DiscussMainListPresenter mPresenter;
    private PageInfo<DiscussBean> data;
    //评论状态  true  对评论进行评论   false 对素材评论
    private boolean status = false;
    private DiscussBean selectedItem;
    private String discussType;
    private String userId = "";


    public static void launch(Context context, String id, String type) {
        Intent intent = new Intent(context, DiscussMainListActivity.class);
        intent.putExtra(DISCUSS_ID, id);
        intent.putExtra(DISCUSS_TYPE, type);
        context.startActivity(intent);
    }

    public static void launch(Context context, String id, String type, String userId) {
        Intent intent = new Intent(context, DiscussMainListActivity.class);
        intent.putExtra(DISCUSS_ID, id);
        intent.putExtra(DISCUSS_TYPE, type);
        intent.putExtra(USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_material_descuss);
        mTitle.setText("讨论专区");

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DiscussListAdapter(1);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setEmptyStateView(this,R.mipmap.ic_state_empty_1,"抱歉没有内容，去别处逛逛吧");
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mPresenter = new DiscussMainListPresenter(this, this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        discussObjId = intent.getStringExtra(DISCUSS_ID);
        discussType = intent.getStringExtra(DISCUSS_TYPE);
        userId = intent.getStringExtra(USER_ID);
        showLoading();
//       获取列表信息
        mPresenter.getDiscussList(discussType, discussObjId, 0, true);
        if (TYPE_THEME.equals(discussType)) {
            mPresenter.getThemeData(discussObjId);
        }

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
            mPresenter.getDiscussList(discussType, discussObjId, start, false);
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
        mPresenter.getDiscussList(discussType, discussObjId, 0, false);
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
    public void showMaterialDiscussList(PageInfo<DiscussBean> pageInfo) {
        showContent();
        data = pageInfo;
        if (pageInfo.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(pageInfo.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(pageInfo.list);
        }
    }

    @Override
    public void showLikeSuccess(int pos) {
        DiscussBean discussBean = mAdapter.getData().get(pos);
        discussBean.setUpvote(true);
        discussBean.setUpvoteCount(discussBean.getUpvoteCount() + 1);
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void showDisLikeSuccess(int pos) {
        DiscussBean discussBean = mAdapter.getData().get(pos);
        discussBean.setUpvote(false);
        discussBean.setUpvoteCount(discussBean.getUpvoteCount() - 1);
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void showCommitDiscussSuccess() {
        changeCommitStatus(false, "");
        etTalkWOrd.setText("");
        mPresenter.getDiscussList(discussType, discussObjId, 0, false);
    }

    @Override
    public void showCommitDiscussDicussSuccess() {
        etTalkWOrd.setText("");
        changeCommitStatus(false, "");
    }

    @Override
    public void showDelCommentSuccess(int pos) {
        mAdapter.remove(pos);
    }

    @Override
    public void showThemeDetail(ThemeBean themeBean) {
        userId = themeBean.userId;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DiscussBean discussBean;
        switch (view.getId()) {
            case R.id.tv_discuss_like:
                String sequenceNBR = mAdapter.getData().get(position).getSequenceNBR();
                if (!view.isSelected()) {
                    mPresenter.like(sequenceNBR, position);
                } else {
                    mPresenter.disLike(sequenceNBR, position);
                }
                break;
            case R.id.iv_discuss_to_talk:
                discussBean = mAdapter.getData().get(position);
                String userInfo = discussBean.getUserInfo();
                DiscussBean.DesignInfoBean designInfoBean = GsonUtil.getGson().fromJson(userInfo, DiscussBean.DesignInfoBean.class);
                String nickName = designInfoBean.getNickName();
                this.selectedItem = discussBean;
                if (100 == designInfoBean.userType && nickName != null) {
                    changeCommitStatus(true, nickName);
                } else if (300 == designInfoBean.userType) {
                    changeCommitStatus(true, "优造中国");
                }
                break;

            case R.id.iv_discuss_all:
                discussBean = mAdapter.getData().get(position);
                DiscussCenterDialogActivity.launch( mContext, discussType,discussObjId, discussBean.getFirstCommentId(), discussBean.getSequenceNBR());
                break;
            case R.id.iv_discuss_del:
                discussBean = mAdapter.getData().get(position);
//                删除评论
                mPresenter.del(discussType, discussBean.getSequenceNBR(), position);
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
            etTalkWOrd.setHint("对 " + nickName + " 评论");
        } else {
            etTalkWOrd.setHint("添加评论");
        }
    }

    public void commitDiscuss(String word) {
        if (StringUtil.isEmpty(word)) {
            ToastUtil.showShort("评论内容不能为空");
            return;
        }
        if (LoginHelper.getUserId().equals(userId)) {
            if (discussType.equals(TYPE_MATERIAL)) {
                ToastUtil.showShort("您不能评论自己的素材");
                return;
            } else if (discussType.equals(TYPE_THEME)) {
                ToastUtil.showShort("您不能评论自己的主题");
                return;
            }

        }
        if (status) {
            mPresenter.commitDiscussDiscuss(discussType, discussObjId, selectedItem.getSequenceNBR(), selectedItem.getFirstCommentId(), word);
        } else {
            mPresenter.commitDiscuss(discussType, discussObjId, word);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        KeyboardUtils.hideSoftInput(this);
        changeCommitStatus(false, "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        switch (event.eventType) {
            case EventBusEvent.DEL_MESSAGE_CENTER:
                mPresenter.getDiscussList(discussType, discussObjId, 0, true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
