package com.zhaotai.uzao.ui.search.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.material.adapter.MyMaterialAdapter;
import com.zhaotai.uzao.ui.search.contract.MyMaterialBoughtFragmentContract;
import com.zhaotai.uzao.ui.search.presenter.MyMaterialBoughtFragmentPresenter;

import butterknife.BindView;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description : 搜索商品列表
 */

public class MyMaterialBoughtFragment extends BaseSearchFragment implements MyMaterialBoughtFragmentContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    private static final String KEY_WORD = "keyWord";
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;


    private PageInfo<MyMaterialBean> data;
    private MyMaterialAdapter myMaterialAdapter;
    private MyMaterialBoughtFragmentPresenter mPresenter;
    private String searchKeyWord;

    public static MyMaterialBoughtFragment newInstance() {
        return new MyMaterialBoughtFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.view_recycle_view;
    }

    @Override
    public void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myMaterialAdapter = new MyMaterialAdapter();

        mRecycler.setAdapter(myMaterialAdapter);
        //上拉加载更多
        myMaterialAdapter.setOnLoadMoreListener(this, mRecycler);
        //下拉刷新
        mSwipe.setOnRefreshListener(this);
        //上拉加载更多
        myMaterialAdapter.setOnLoadMoreListener(this, mRecycler);
        //不显示刷新头
        myMaterialAdapter.setOnItemClickListener(this);
        mPresenter = new MyMaterialBoughtFragmentPresenter(_mActivity, this);
        Log.e("xxx", "initView: 我已经初始化了");
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getMyBoughtMaterial(start, searchKeyWord);
        } else {
            myMaterialAdapter.loadMoreEnd();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        刷新页面
        mPresenter.getMyBoughtMaterial(0, searchKeyWord);
    }

    /**
     * 停止加载更多状态
     */
    @Override
    public void stopLoadingMore() {
        myMaterialAdapter.loadMoreComplete();
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
        myMaterialAdapter.loadMoreFail();
    }

    @Override
    public void showMaterialList(PageInfo<MyMaterialBean> data) {
        showContent();
        this.data = data;
        if (data.totalRows == 0) {
            showEmpty();
        }
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            myMaterialAdapter.setNewData(data.list);
        } else {
//            不是第一页 就刷新
            myMaterialAdapter.addData(data.list);
        }
    }

    @Override
    public void searchWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
        mPresenter.getMyBoughtMaterial(0, searchKeyWord);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialDetailActivity.launch(getActivity(), myMaterialAdapter.getData().get(position).getSourceMaterialId());
    }
}
