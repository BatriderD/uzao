package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.richeditor.RichEditor;
import com.zhaotai.uzao.ui.discuss.activity.DiscussCenterDialogActivity;
import com.zhaotai.uzao.ui.discuss.adapter.DiscussListAdapter;
import com.zhaotai.uzao.ui.theme.contract.PosterDetailContract;
import com.zhaotai.uzao.ui.theme.presenter.PostDetailPresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PostDetailActivity extends BaseActivity implements PosterDetailContract.View, OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    private static String POST_ID = "POST_ID";
    private static String POST_CONTENT = "POST_CONTENT";
    private static String THEME_ID = "THEME_ID";
    private PostDetailPresenter mPresenter;
    private String postId;

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.et_talk_word)
    EditText etTalkWOrd;
    private DiscussListAdapter mAdapter;
    private String postContent;
    private PageInfo<DiscussBean> data;
    private boolean status = false;
    private DiscussBean selectedItem;
    private String discussType = "posts";
    private TextView tvHeadCount;
    private String themeId;

    public static void launch(Context context, String postId, String postContent, String themeId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(POST_ID, postId);
        intent.putExtra(POST_CONTENT, postContent);
        intent.putExtra(THEME_ID, themeId);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_post_detail);
        mTitle.setText("帖子详情");
        Intent intent = getIntent();
        postId = intent.getStringExtra(POST_ID);
        postContent = intent.getStringExtra(POST_CONTENT);
        themeId = intent.getStringExtra(THEME_ID);

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DiscussListAdapter(1);
        mAdapter.setLikeDelVisable(false);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        LinearLayout headView = (LinearLayout) View.inflate(this, R.layout.head_post, null);
        tvHeadCount = (TextView) headView.findViewById(R.id.tv_count);
        RichEditor rEditor = (RichEditor) headView.findViewById(R.id.editor);
        rEditor.setHtml(postContent);
        mAdapter.addHeaderView(headView);
        mPresenter = new PostDetailPresenter(this, this);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getDiscussList("posts", postId, 0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public boolean hasTitle() {
        return true;
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getDiscussList("posts", postId, 0, false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DiscussBean discussBean;
        switch (view.getId()) {
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
                DiscussCenterDialogActivity.launch(mContext, discussType, postId, discussBean.getFirstCommentId(), discussBean.getSequenceNBR());
                break;
        }
    }

    @Override
    public void showMaterialDiscussList(PageInfo<DiscussBean> pageInfo) {
        showContent();
        data = pageInfo;
        if (pageInfo.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            tvHeadCount.setText("(" + pageInfo.totalRows + ")");
            mAdapter.setNewData(pageInfo.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(pageInfo.list);
        }
        if (pageInfo.currentPage == pageInfo.totalPages) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getDiscussList(discussType, postId, start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        KeyboardUtils.hideSoftInput(this);
        changeCommitStatus(false, "");
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

    @OnClick(R.id.tv_publish)
    public void publishDiscuss() {
        String word = etTalkWOrd.getText().toString();
        KeyboardUtils.hideSoftInput(this);
        commitDiscuss(word, themeId);
    }

    public void commitDiscuss(String word, String themeId) {
        if (StringUtil.isEmpty(word)) {
            ToastUtil.showShort("评论内容不能为空");
            return;
        }
        if (status) {
            mPresenter.commitDiscussDiscuss(discussType, postId, selectedItem.getSequenceNBR(), selectedItem.getFirstCommentId(), word, themeId);
        } else {
            mPresenter.commitDiscuss(discussType, postId, word, themeId);
        }
    }

    @Override
    public void showCommitDiscussSuccess() {
        changeCommitStatus(false, "");
        etTalkWOrd.setText("");
        mPresenter.getDiscussList(discussType, postId, 0, false);
    }

    @Override
    public void showCommitDiscussDicussSuccess() {
        etTalkWOrd.setText("");
        changeCommitStatus(false, "");
    }
}
