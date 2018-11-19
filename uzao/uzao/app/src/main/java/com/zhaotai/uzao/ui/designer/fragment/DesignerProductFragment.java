package com.zhaotai.uzao.ui.designer.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.DesignerProductShowAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.designer.contract.DesignerListContract;
import com.zhaotai.uzao.ui.designer.presenter.DesignerListPresenter;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import butterknife.BindView;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 设计师主页-商品列表
 */

public class DesignerProductFragment extends BaseFragment implements DesignerListContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    //设计师id
    private static final String ARG_DESIGNER_ID = "arg_designer_id";

    private DesignerProductShowAdapter mAdapter;
    private String designerId;
    private DesignerListPresenter mPresenter;

    private PageInfo<GoodsBean> data = new PageInfo<>();

    public static DesignerProductFragment newInstance(String designerId) {
        DesignerProductFragment fragment = new DesignerProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DESIGNER_ID, designerId);
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
        mAdapter = new DesignerProductShowAdapter();
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        //空页面
        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_1, "TA还木有创作，先去别的地方玩耍吧", "去逛逛", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                HomeActivity.launch(_mActivity,1);
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new DesignerListPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        designerId = getArguments().getString(ARG_DESIGNER_ID);
        //获取设计师商品列表
        mPresenter.getDesignerProductList(0, designerId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
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
    public void showProduct(PageInfo<GoodsBean> goodsBean) {
        data = goodsBean;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    public void showMaterial(PageInfo<MaterialListBean> materialList) {

    }

    @Override
    public void showTheme(PageInfo<ThemeBean> themeBean) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
        if (goodsBean != null)
            CommodityDetailMallActivity.launch(_mActivity, goodsBean.id);
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getDesignerProductList(start, designerId);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
