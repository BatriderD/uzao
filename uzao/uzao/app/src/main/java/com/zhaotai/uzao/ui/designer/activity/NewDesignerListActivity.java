package com.zhaotai.uzao.ui.designer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.designer.adapter.DesignerListAdapter;
import com.zhaotai.uzao.ui.designer.contract.NewDesignerListContract;
import com.zhaotai.uzao.ui.designer.presenter.NewDesignerListPresenter;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/5
 * Created by LiYou
 * Description : 我的设计
 */

public class NewDesignerListActivity extends BaseActivity implements NewDesignerListContract.View, BaseQuickAdapter.RequestLoadMoreListener,
        OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private DesignerListAdapter mAdapter;
    private NewDesignerListPresenter mPresenter;
    private PageInfo data;
    private AlertDialog.Builder dialog;
    private String type;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, NewDesignerListActivity.class));
    }

    public static void launchAddTheme(Context context) {
        Intent intent = new Intent(context, NewDesignerListActivity.class);
        intent.putExtra("TYPE", "ADD_THEME");
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_design_list);
        mTitle.setText("设计师");

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DesignerListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mPresenter = new NewDesignerListPresenter(this, this);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setEmptyStateView(this, R.mipmap.ic_status_empty_design, "没有任何设计师，快去关注吧");
        SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration((int) PixelUtil.dp2px(3), (int) PixelUtil.dp2px(3));
        mRecyclerView.addItemDecoration(simpleDividerItemDecoration);
    }

    @Override
    protected void initData() {
        showLoading();
        type = getIntent().getStringExtra("TYPE");
        mPresenter.getDesignerList(0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
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
            mPresenter.getDesignerList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getDesignerList(0, false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if (!LoginHelper.getLoginStatus()) {
            ToastUtil.showShort("请先登录");
            LoginActivity.launch(this);
            return;
        }
        final DesignerBean designerBean = mAdapter.getData().get(position);
        if (designerBean.data == null) {
            return;
        }
        String isFavorited = designerBean.data.isFavorited;
        if (designerBean.data != null && "Y".equals(isFavorited)) {
//            关注状态
            if (dialog == null) {
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("确定不再关注此人?");
                dialog.setCancelable(false);
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.cancelDesigner(position, designerBean.userId);
                    }
                });
            }
            dialog.show();

        } else {
            mPresenter.attentionDesigner(position, designerBean.userId);
        }
    }

    @Override
    public void showDesignList(PageInfo<DesignerBean> data) {
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
    public void changeDesigner(int pos, boolean b) {
        DesignerBean designerBean = mAdapter.getData().get(pos);
        if (b) {
            designerBean.favoriteCount = designerBean.favoriteCount + 1;
        } else {
            designerBean.favoriteCount = designerBean.favoriteCount - 1;
        }
        designerBean.data.isFavorited = b ? "Y" : "N";
        mAdapter.notifyItemChanged(pos);

        EventBus.getDefault().post(new EventMessage(EventBusEvent.REFRESH_ATTENTION));
    }

    @OnClick(R.id.iv_new_designer_search)
    public void onSearch() {
        NewDesignerListSearchActivity.launch(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage info) {
        if (EventBusEvent.REFRESH_ATTENTION.equals(info.eventType)) {
            initData();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DesignerBean designerBean = mAdapter.getData().get(position);
        if ("ADD_THEME".equals(type)) {
            ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
            //增加进入新增的进入主题
            contentModel.setEntityType(ThemeModuleBean.TYPE_DESIGNER);
            contentModel.setEntityId(designerBean.userId);
            contentModel.setEntityName(designerBean.nickName);
            contentModel.setEntityPic(designerBean.avatar);
            EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
            finish();
        } else {
            DesignerActivity.launch(this, designerBean.userId);
        }

    }
}
