package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.theme.adapter.MySceneAdapter;
import com.zhaotai.uzao.ui.theme.contract.MySceneContract;
import com.zhaotai.uzao.ui.theme.presenter.MyScenePresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 我管理的场景列表页面
 * author : ZP
 * date: 2018/1/23 0023.
 */

public class MySceneActivity extends BaseActivity implements MySceneContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    //右上角管理，取消按钮
    @BindView(R.id.right_btn)
    public Button btnRight;

    @BindView(R.id.ll_theme_manager)
    public LinearLayout llManager;

    @BindView(R.id.tv_theme_all)
    public TextView tv_theme_all;

    @BindView(R.id.tv_theme_del)
    public TextView tv_theme_del;
    //新增按钮
    @BindView(R.id.tv_add_theme)
    public TextView tv_add_theme;


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private MySceneAdapter mAdapter;
    private MyScenePresenter mPresenter;
    private PageInfo<ThemeBean> data;
    //标识页面是管理状态（可删除主题） 还是普通状态
    private boolean showManager = false;
    //第一次进入 走正常的initData 以后这个页面重新显示就刷新。
    private boolean isFirst = true;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MySceneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_theme);

        mTitle.setText("场景管理");
        btnRight.setText(R.string.manage);
        btnRight.setVisibility(View.GONE);
        //设置刷新
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //设置关闭动画 避免刷新闪动
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        //初始化我的主题Adapter
        mAdapter = new MySceneAdapter();
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mPresenter = new MyScenePresenter(this, this);
        //设置空白页面的按钮点击事件
        mAdapter.setEmptyStateView(this, R.drawable.ic_empty_scene, getString(R.string.not_scene_manager));
        tv_add_theme.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        showLoading();
        //请求我的主题列表
        mPresenter.getMyManagerSceneList(0, true);
    }

    /**
     * 有状态页面
     *
     * @return true 有状态页面 false 没有状态页面
     */
    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载新的列表数据
            mPresenter.getMyManagerSceneList(start, false);
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
        mPresenter.getMyManagerSceneList(0, false);
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


//    /**
//     * 管理按钮点击
//     */
//    @OnClick(R.id.right_btn)
//    public void onManager() {
//        if (!showManager) {
//            //清除adapter所有的选中状态
//            btnRight.setText(R.string.cancel);
//            mAdapter.setShowManager(true);
//            llManager.setVisibility(View.VISIBLE);
//
//        } else {
//            btnRight.setText(R.string.manage);
//            mAdapter.setShowManager(false);
//            llManager.setVisibility(View.GONE);
//        }
//        mAdapter.notifyDataSetChanged();
//        showManager = !showManager;
//    }

    /**
     * 全选按钮，选中页面显示的所有主题
     */
    @OnClick(R.id.tv_theme_all)
    public void onSelectAll() {
//        adapter选中状态
        tv_theme_all.setSelected(!tv_theme_all.isSelected());
        List<ThemeBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(tv_theme_all.isSelected());
        }
        mAdapter.notifyDataSetChanged();

    }


    /**
     * 删除主题按钮，删除选中的主题。
     */
    @OnClick(R.id.tv_theme_del)
    public void onDelete() {
//        adapter选中状态
        List<ThemeBean> data = mAdapter.getData();
        ArrayList<String> themeIds = new ArrayList<>();
        for (ThemeBean bean : data) {
            if (bean.isSelected()) {
                themeIds.add(bean.sequenceNBR);
            }
        }
        //删除的异常判断
        if (themeIds.size() > 0) {
            mPresenter.delTheme(themeIds);
        } else {
            ToastUtil.showShort("请至少选择一个主题");
        }
    }


    /**
     * 列表的点击状态，在编辑模式下选中主题，在普通模式下进入主题详情或者编辑主题。
     *
     * @param adapter  adapter
     * @param view     选中view
     * @param position 选中位置
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (showManager) {
//            记录当前条目 未做
//            选中当前条目
            ThemeBean themeBean = mAdapter.getData().get(position);
            themeBean.setSelected(!themeBean.isSelected());
            mAdapter.notifyItemChanged(position);
        } else {
//          进入当前主题 进入主题详情
            ThemeBean themeBean = mAdapter.getData().get(position);
            if (StringUtil.isEmpty(themeBean.wapUrl)) {
                ThemeBean item = mAdapter.getItem(position);
                if (item != null) {
                    EditThemeActivity.launch(mContext, item.sequenceNBR);
                }
            } else {
                SceneManagerActivity.launch(this, themeBean, false);
            }

        }
    }

    @Override
    public void showMyThemeList(PageInfo<ThemeBean> data) {
        if (isFirst) {
            isFirst = false;
        }
        this.data = data;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
//        if (mAdapter.getData().size() > 0) {
//            btnRight.setEnabled(true);
//            btnRight.setTextColor(ContextCompat.getColor(this, R.color.black));
//        } else {
//            btnRight.setEnabled(false);
//            btnRight.setTextColor(ContextCompat.getColor(this, R.color.no_enable));
//
//            btnRight.setText(R.string.manage);
//            mAdapter.setShowManager(false);
//            llManager.setVisibility(View.GONE);
//            tv_add_theme.setVisibility(View.VISIBLE);
//            mAdapter.notifyDataSetChanged();
//            showManager = false;
//        }
    }

    @Override
    public void showDelSuccess() {
        mPresenter.getMyManagerSceneList(0, false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_my_scene_editor_manager:
                ThemeBean item = mAdapter.getItem(position);
                if (item != null) {
//                    管理页面
                    SceneManagerActivity.launch(mContext, item, true);
                }
                break;
        }
    }

    /**
     * 重新进入页面 ，就刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            mPresenter.getMyManagerSceneList(0, false);
        }
    }

}
