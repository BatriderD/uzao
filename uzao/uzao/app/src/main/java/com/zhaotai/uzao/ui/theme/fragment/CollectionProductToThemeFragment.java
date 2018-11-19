package com.zhaotai.uzao.ui.theme.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.theme.adapter.CollectionProductToThemeAdapter;
import com.zhaotai.uzao.ui.theme.contract.CollectionToThemeContract;
import com.zhaotai.uzao.ui.theme.presenter.CollectionToThemePresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;

/**
 * description:从收藏的商品选择到主题模块
 * author : ZP
 * date: 2018/1/31 0031.
 */

public class CollectionProductToThemeFragment extends BaseFragment implements CollectionToThemeContract.ProductView, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private HashMap<String, String> params = new HashMap();

    private CollectionToThemePresenter mPresenter;

    private CollectionProductToThemeAdapter mAdapter;
    private PageInfo<GoodsBean> data;

    public static CollectionProductToThemeFragment newInstance() {
        return new CollectionProductToThemeFragment();
    }


    @Override
    protected int layoutId() {
        return R.layout.layout_no_swipe;
    }

    @Override
    public void initView() {
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        //内容列表
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CollectionProductToThemeAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mAdapter.setEmptyView(R.layout.vw_empty);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CollectionToThemePresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        mPresenter.getCollectProductList(0, true, params);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @Override
    public void showProductList(PageInfo<GoodsBean> goodsBean) {
        data = goodsBean;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    /**
     * 列表元素点击事件 封装点击的数据 发送eventBus到主题模块数据
     * @param adapter adapter
     * @param view 点击的view
     * @param position 位置
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean item = mAdapter.getItem(position);
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        contentModel.setEntityType("spu");
        contentModel.setEntityId(item.spuId);
        contentModel.setEntityName(item.spuName);
        contentModel.setEntityPic(item.thumbnail);
        contentModel.setEntityPriceY(item.priceY);
        contentModel.setBuyCounts("0");
        contentModel.setViewCounts("0");
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        getActivity().finish();
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
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCollectProductList(start, false, params);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
