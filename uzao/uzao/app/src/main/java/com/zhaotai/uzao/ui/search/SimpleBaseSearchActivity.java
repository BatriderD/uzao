package com.zhaotai.uzao.ui.search;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/22
 * Created by LiYou
 * Description : 简单基础搜索封装类
 */

public abstract class SimpleBaseSearchActivity extends BaseActivity implements SimpleBaseSearchContract.View, TextView.OnEditorActionListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_brand)
    public RecyclerView mRecycler;
    public SimpleBaseSearchPresenter mPresenter;
    protected HashMap<String, String> params = new HashMap<>();
    @BindView(R.id.etd_text)
    EditWithDelView mEtSearch;
    public PageInfo data = new PageInfo<>();
    private BaseQuickAdapter mAdapter;
    private String searchWord;
    private boolean defaultSearch = false;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_search);

        mRecycler.setLayoutManager(getLayoutManager());
        mRecycler.addItemDecoration(getItemDecoration());
        mAdapter = setAdapter();
        mRecycler.setAdapter(mAdapter);
        mEtSearch.setOnEditorActionListener(this);
        mAdapter.bindToRecyclerView(mRecycler);
        mPresenter = setPresenter();
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(2, 10, false);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mContext, 2);
    }


    @Override
    public void initData() {
        //初始化一个默认参数
        params.put("start", "0");
        params.put("length", "10");
        if (doDefaultSearch()) {
            getCommodityList(0);
        }
    }

    public boolean doDefaultSearch() {
        return false;
    }

    protected abstract BaseQuickAdapter setAdapter();

    protected abstract SimpleBaseSearchPresenter setPresenter();


    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 搜索按钮相应
     */
    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            ToastUtil.showShort("搜索内容不能为空");
            return;
        }
        searchWord = mEtSearch.getText().toString();
        params = goSearch(searchWord);
        //搜索数据
        getCommodityList(0);
    }

    /**
     * 搜索内容
     *
     * @param searchWord 搜索的关键字
     */
    protected abstract HashMap<String, String> goSearch(String searchWord);

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
                ToastUtil.showShort("搜索内容不能为空");
            } else {
                searchWord = mEtSearch.getText().toString();
                params = goSearch(searchWord);
                getCommodityList(0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void loadingMoreFail() {
        mAdapter.loadMoreFail();
    }

    /**
     * 展示搜索列表
     *
     * @param goodsBean
     */
    @Override
    public void showCommodityList(PageInfo goodsBean) {
        KeyboardUtils.hideSoftInput(this);
        data = goodsBean;
        if (goodsBean.totalRows <= 0) {
            showEmpty(getString(R.string.empty_search));
        } else {
            showContent();
        }
        if (goodsBean.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(goodsBean.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(goodsBean.list);
        }

    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            params.put("start", String.valueOf(start));
            //加载列表数据
            getCommodityList(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 搜索具体列表数据
     *
     * @param start 起始位置
     */
    public void getCommodityList(int start) {
        mPresenter.getCommodityList(start, params);
    }
}
