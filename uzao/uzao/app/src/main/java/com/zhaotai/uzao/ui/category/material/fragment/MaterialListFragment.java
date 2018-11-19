package com.zhaotai.uzao.ui.category.material.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.ui.category.material.activity.MaterialCategoryListActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.category.material.adapter.MaterialListAdapter;
import com.zhaotai.uzao.ui.category.material.contract.MaterialCategoryListContract;
import com.zhaotai.uzao.ui.category.material.presenter.MaterialCategoryListPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Time: 2018/8/30 0030
 * Created by LiYou
 * Description :
 */
public class MaterialListFragment extends BaseFragment implements MaterialCategoryListContract.View
        , BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MaterialCategoryListPresenter mPresenter;
    private MaterialListAdapter mAdapter;

    private PageInfo<MaterialListBean> data = new PageInfo<>();
    private Map<String, String> params = new HashMap<>();
    private String secondCode = "";

    public static MaterialListFragment newInstance(String categoryCode) {
        MaterialListFragment fragment = new MaterialListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryCode", categoryCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.view_recycle_view;
    }

    @Override
    public void initView() {
        mAdapter = new MaterialListAdapter();
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2, LinearLayoutManager.VERTICAL, false));
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new MaterialCategoryListPresenter(this, _mActivity);
        }
    }

    @Override
    public void initData() {
        mSwipe.setOnRefreshListener(this);
        secondCode = getArguments().getString("categoryCode");
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("sort", "default_");
        if (secondCode == null) {
            secondCode = "";
        }
        params.put("categoryCode2", secondCode);
        showLoading();
        mPresenter.getMaterialCategoryList(0, true, params);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showMaterialCategoryList(PageInfo<MaterialListBean> list) {
        data = list;
        ((MaterialCategoryListActivity) _mActivity).showFilterBottomOkText(list.list.size());
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
    public void showFilter(ProductOptionBean opt) {
        ((MaterialCategoryListActivity) _mActivity).showFilter(opt, secondCode);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialListBean item = (MaterialListBean) adapter.getItem(position);
        if (item != null)
            MaterialDetailActivity.launch(_mActivity, item.id);
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            params.put("start", String.valueOf(start));
            params.put("needOption", "N");
            params.put("sort", "default_");
            params.put("categoryCode2", secondCode);
            //加载列表数据
            mPresenter.getMaterialCategoryList(start, false, params);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("sort", "default_");
        params.put("categoryCode2", secondCode);
        mPresenter.getMaterialCategoryList(0, false, params);
    }

    public void filterTagSelect(Map<String, String> params) {
        this.params = params;
        this.params.put("start", "0");
        this.params.put("needOption", "N");
        this.params.put("sort", "default_");
        this.params.put("categoryCode2", secondCode);
        mPresenter.getMaterialCategoryList(0, false, params);
    }

    public void filterReset() {
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("sort", "default_");
        params.put("categoryCode2", secondCode);
        mPresenter.getMaterialCategoryList(0, false, params);
    }
}
