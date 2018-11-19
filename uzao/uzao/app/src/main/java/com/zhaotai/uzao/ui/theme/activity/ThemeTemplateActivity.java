package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.TemplateJsonBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.bean.ThemeTemplateBean;
import com.zhaotai.uzao.ui.theme.adapter.ThemeTemplateAdapter;
import com.zhaotai.uzao.ui.theme.contract.ThemeTemplateContract;
import com.zhaotai.uzao.ui.theme.presenter.ThemeTemplatePresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 我的主题列表页面
 * author : ZP
 * date: 2018/1/23 0023.
 */

public class ThemeTemplateActivity extends BaseActivity implements ThemeTemplateContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private static final String THEME_ID = "themeId";
    private static final String MODULE = "module";
    private static final String TEMPLATE_JSON = "TemplateJsonBean";
    @BindView(R.id.right_btn)
    public Button btnRight;

    @BindView(R.id.fl_bottom)
    public FrameLayout flBottom;

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private ThemeTemplateAdapter mAdapter;
    private ThemeTemplatePresenter mPresenter;
    private PageInfo<ThemeTemplateBean> data;
    private String themeId;
    //主题的模块数据
    private ArrayList<ThemeModuleBean> themeModuleBeanArrayList;
    //主题的数据json的实体类
    private TemplateJsonBean templateJsonBean;
    //主题的数据json的实体类
    private String json;

    public static void launch(Context context, TemplateJsonBean jsonBean, String themeId, ArrayList<ThemeModuleBean> upSaveModuleList) {

        Intent intent = new Intent(context, ThemeTemplateActivity.class);
        intent.putExtra(THEME_ID, themeId);
        intent.putExtra(MODULE, upSaveModuleList);
        intent.putExtra(TEMPLATE_JSON, jsonBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_theme);
        mTitle.setText("选择模板");
        btnRight.setText("保存设置");
        btnRight.setVisibility(View.VISIBLE);
        flBottom.setVisibility(View.GONE);

        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ThemeTemplateAdapter();
        mRecycler.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);


        mPresenter = new ThemeTemplatePresenter(this, this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        themeId = intent.getStringExtra(THEME_ID);
        //获得从上页面来过来的数据
        themeModuleBeanArrayList = (ArrayList<ThemeModuleBean>) intent.getSerializableExtra(MODULE);
        templateJsonBean = (TemplateJsonBean) intent.getSerializableExtra(TEMPLATE_JSON);
        json = GsonUtil.t2Json2(templateJsonBean);
        //获取主题模板类表数据
        mPresenter.getThemeTemplateList(0, true);
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
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getThemeTemplateList(start, false);
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
        mPresenter.getThemeTemplateList(0, false);
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


    @OnClick(R.id.right_btn)
    public void onSetTemplate() {
        int selected = mAdapter.getSelected();
        if (selected != -1 && selected <= mAdapter.getData().size()) {
            ThemeTemplateBean item = mAdapter.getItem(selected);
            mPresenter.saveThemeAll(themeId, item.getSequenceNBR(), json, themeModuleBeanArrayList);
        } else {
            ToastUtil.showShort("请选择一个模板");
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setselected(position);
        mAdapter.notifyItemChanged(position);
    }


    @Override
    public void showThemeTemplateList(PageInfo<ThemeTemplateBean> data) {
        this.data = data;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void showSaveSuccess() {
        EventBus.getDefault().post(new EventBean<>(null, EventBusEvent.SAVE_THEME_TEMPLATE_SUCCESS));
        finish();
    }

    @Override
    public void getPreviewUrlSuccess(int position, String url) {
        PreviewThemeActivity.launch(this, position, url);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_theme_template_preview:
                System.out.println("预览json" + json);
                mPresenter.getPreviewTheme(themeId, position, mAdapter.getItem(position).getSequenceNBR(), json);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            int pos = data.getIntExtra("pos", -1);
            if (pos != -1) {
                mAdapter.setselected(pos);
                mAdapter.notifyItemChanged(pos);
            }
        }
    }
}
