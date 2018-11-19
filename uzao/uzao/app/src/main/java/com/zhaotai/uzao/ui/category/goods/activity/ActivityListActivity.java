package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.ActivityGoodsListAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.goods.contract.CommodityListContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CommodityPresenter;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.utils.KeyboardUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description :  活动 商品列表
 */

public class ActivityListActivity extends BaseFragmentActivity implements CommodityListContract.View, OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_category_list)
    RecyclerView mRecycler;
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActivityGoodsListAdapter mAdapter;
    private CommodityPresenter mPresenter;
    private PageInfo<GoodsBean> data = new PageInfo<>();
    private Map<String, String> params = new HashMap<>();
    private FilterFragment mFilterFragment;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ActivityListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_activity_list);
        mTitle.setText("活动商品列表");
        mAdapter = new ActivityGoodsListAdapter(mContext);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter(mAdapter);

        mSwipe.setOnRefreshListener(this);
        mPresenter = new CommodityPresenter(this, this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);

        //取消侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mFilterFragment = FilterFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_filter_drawer, mFilterFragment).commit();

        mFilterFragment.setOnFilterListener(new OnFilterTagClickListener() {
            //tag点击
            @Override
            public void onTagSelect(Map<String, String> paramss) {
                params = paramss;
                params.put("start", "0");
                params.put("needOption", "N");
                mPresenter.getActivityList(0, false, params);
            }
            //重置
            @Override
            public void reset() {
                params.clear();
                params.put("start", "0");
                params.put("needOption", "Y");
                mPresenter.getActivityList(0, false, params);
            }
            //关闭
            @Override
            public void closeDrawer() {
                KeyboardUtils.hideSoftInput(ActivityListActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    protected void initData() {
        showLoading();
        params.put("start", "0");
        params.put("needOption", "Y");//需要筛选
        mPresenter.getActivityList(0, true, params);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 筛选
     */
    @OnClick(R.id.right_btn)
    public void onClickFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void showCategoryList(PageInfo<GoodsBean> list) {
        data = list;
        mFilterFragment.setBottomOKText("确定(" + list.list.size() + "个商品)");
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
    public void showNavigateList(PageInfo<GoodsBean> list) {

    }

    /**
     * 显示筛选
     */
    @Override
    public void showFilter(ProductOptionBean opt) {
        mFilterFragment.showFilter(opt);
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        mPresenter.getActivityList(0, false, params);
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            params.put("start", start + "");
            params.put("needOption", "N");
            mPresenter.getActivityList(start, false, params);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean item = (GoodsBean) adapter.getItem(position);
        if (item == null) return;
        CommodityDetailMallActivity.launch(mContext, item.spuId);
    }
}
