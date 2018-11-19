package com.zhaotai.uzao.ui.person.collection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.person.collection.adapter.CollectionProductListAdapter;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionProductSearchContract;
import com.zhaotai.uzao.ui.person.collection.presenter.CollectionProductSearchPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/11/22
 * Created by LiYou
 * Description : 收藏的商品 搜索
 */

public class CollectProductSearchActivity extends BaseActivity implements CollectionProductSearchContract.View, TextView.OnEditorActionListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.etd_text)
    EditWithDelView mEtSearch;

    @BindView(R.id.recycler_collect)
    RecyclerView mRecycler;

    private CollectionProductListAdapter mAdapter;
    private CollectionProductSearchPresenter mPresenter;
    private PageInfo<GoodsBean> data = new PageInfo<>();
    private String searchWord = "";

    public static void launch(Context context) {
        context.startActivity(new Intent(context, CollectProductSearchActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_collect_product);

        mAdapter = new CollectionProductListAdapter();

        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);
        mEtSearch.setOnEditorActionListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
                if (goodsBean != null) {
                    //点击跳转商品详情
                    CommodityDetailMallActivity.launch(CollectProductSearchActivity.this, goodsBean.spuId);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new CollectionProductSearchPresenter(this, this);
        }
        mPresenter.getCollectProductList(0, searchWord);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            ToastUtil.showShort("请填写商品名字");
            return;
        }
        showLoading();
        searchWord = mEtSearch.getText().toString().trim();
        mPresenter.getCollectProductList(0, searchWord);
    }

    @Override
    public void showCollectionProductList(PageInfo<GoodsBean> list) {
        data = list;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
                ToastUtil.showShort("请填写商品名字");
            } else {
                searchWord = mEtSearch.getText().toString().trim();
                showLoading();
                mPresenter.getCollectProductList(0, searchWord);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCollectProductList(start, searchWord);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
