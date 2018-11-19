package com.zhaotai.uzao.ui.brand.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.DesignerThemeAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.brand.contract.BrandListContract;
import com.zhaotai.uzao.ui.brand.presenter.BrandListPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeListActivity;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import butterknife.BindView;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :品牌主页--主题
 */

public class BrandThemeFragment extends BaseFragment implements BrandListContract.View, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    //品牌id
    private static final String ARG_BRAND_ID = "arg_brand_id";

    private DesignerThemeAdapter mAdapter;
    private BrandListPresenter mPresenter;
    private String brandId;

    private PageInfo<ThemeBean> data = new PageInfo<>();

    public static BrandThemeFragment newInstance(String designerId) {
        BrandThemeFragment fragment = new BrandThemeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_BRAND_ID, designerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_designer_product;
    }

    @Override
    public void initView() {
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));

    }

    @Override
    public void initPresenter() {
        mPresenter = new BrandListPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        brandId = getArguments().getString(ARG_BRAND_ID);
        //获取品牌主题
        mPresenter.getBrandThemeList(0, brandId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void stopLoadingMore() {
        if (mAdapter != null)
            mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {
        if (mAdapter != null)
            mAdapter.loadMoreFail();
    }

    @Override
    public void showProduct(PageInfo<GoodsBean> goodsBean) {

    }

    @Override
    public void showMaterial(PageInfo<MaterialListBean> materialList) {

    }

    @Override
    public void showTheme(PageInfo<ThemeBean> themeBean) {
        if (mAdapter == null) {
            mAdapter = new DesignerThemeAdapter();
            mRecycler.setAdapter(mAdapter);
            mRecycler.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));
            mAdapter.setOnLoadMoreListener(this, mRecycler);
            mAdapter.setOnItemClickListener(this);
            mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_1, "还在准备中,敬请期待", getString(R.string.empty_btn), new BtnOnClickListener() {
                @Override
                public void btnOnClickListener() {
                    ThemeListActivity.launch(_mActivity);
                }
            });
        }
        data = themeBean;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getBrandMaterialList(start, brandId);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ThemeBean themeBean = (ThemeBean) adapter.getItem(position);
        //主题详情
        if (themeBean != null)
            ThemeDetailActivity.launch(_mActivity, themeBean.id);
    }
}
