package com.zhaotai.uzao.ui.category.goods.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.category.goods.contract.DesignProductListContract;
import com.zhaotai.uzao.ui.category.goods.presenter.DesignProductListPresenter;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.ui.search.DesignProductAndSearchActivity;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.LoadingDialog;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/11
 * Created by LiYou
 * Description : 可定制商品
 */

public class DesignProductListActivity extends BaseActivity implements DesignProductListContract.View, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.tool_bar_right_img)
    ImageView mSearch;

    private DesignProductListPresenter mPresenter;
    private ProductListAdapter mAdapter;
    private PageInfo<GoodsBean> data;
    private MaterialDetailBean materialDetailBean;
    private String type = EXTRA_KEY_TYPE_NORMAL;

    //类型
    private static final String EXTRA_KEY_TYPE = "extra_key_type";
    //带素材可定制商品
    private static final String EXTRA_KEY_TYPE_WITH_MATERIAL = "extra_key_type_with_material";
    //普通定制商品
    private static final String EXTRA_KEY_TYPE_NORMAL = "extra_key_type_normal";
    //场景定制商品
    private static final String EXTRA_KEY_TYPE_THEME = "extra_key_type_theme";

    /**
     * 可定制商品列表
     *
     * @param context            上下文
     * @param materialDetailBean 素材信息
     */
    public static void launch(Context context, MaterialDetailBean materialDetailBean) {
        Intent intent = new Intent(context, DesignProductListActivity.class);
        intent.putExtra("materialDetailBean", materialDetailBean);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_WITH_MATERIAL);
        context.startActivity(intent);
    }

    /**
     * 可定制商品列表
     *
     * @param context 上下文
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, DesignProductListActivity.class);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_NORMAL);
        context.startActivity(intent);
    }

    /**
     * 可定制商品列表
     *
     * @param context 上下文
     */
    public static void launchAddToTheme(Context context) {
        Intent intent = new Intent(context, DesignProductListActivity.class);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_THEME);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_design_product);
        mTitle.setText("可定制商品");
        mSearch.setVisibility(View.VISIBLE);
        mSearch.setImageResource(R.drawable.icon_search_black);
        mAdapter = new ProductListAdapter();
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        materialDetailBean = (MaterialDetailBean) getIntent().getSerializableExtra("materialDetailBean");
        type = getIntent().getStringExtra(EXTRA_KEY_TYPE);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
                if (goodsBean == null) return;
                switch (type) {
                    case EXTRA_KEY_TYPE_NORMAL:
                    case EXTRA_KEY_TYPE_WITH_MATERIAL:
                        //判断是否带素材 带素材到编辑器 不带素材到商品详情
                        if (materialDetailBean == null) {
                            CommodityDetailMallActivity.launch(mContext, goodsBean.id);
                        } else {
                            if (goodsBean.spuType.equals("design")) {
                                //设计商品
                                String isTemplate;
                                if ("design".equals(goodsBean.customizeType)) {
                                    isTemplate = "N";
                                } else {
                                    isTemplate = "Y";
                                }
                                EditorActivity.launch2DWhitMaterial(mContext, goodsBean.mkuId, goodsBean.id, isTemplate, materialDetailBean);
                            } else {
                                //获取mkuId
                                mPresenter.checkIsNeedSku(goodsBean.id, materialDetailBean);
                            }
                        }
                        break;
                    case EXTRA_KEY_TYPE_THEME:
                        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
                        //增加进入新增的进入主题
                        contentModel.setEntityType("spu");
                        contentModel.setEntityId(goodsBean.id);
                        contentModel.setEntityName(goodsBean.spuName);
                        contentModel.setEntityPic(goodsBean.pic);
                        contentModel.setEntityPriceY(goodsBean.priceY);
                        contentModel.setViewCounts(goodsBean.viewCount);
                        contentModel.setBuyCounts(goodsBean.salesCount);
                        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
                        finish();
                        break;
                }
            }
        });

        mSwipe.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new DesignProductListPresenter(this, this);
        showLoading();
        switch (type) {
            case EXTRA_KEY_TYPE_NORMAL:
            case EXTRA_KEY_TYPE_WITH_MATERIAL:
                mPresenter.getDesignProductList(0, true);
                break;
            case EXTRA_KEY_TYPE_THEME:
                mPresenter.getAllDesignProductList(0, true);
                break;
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.tool_bar_right_img)
    public void onClickSearch() {
        DesignProductAndSearchActivity.launch(mContext, materialDetailBean);
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
    public void showDesignProductList(PageInfo<GoodsBean> list) {
        data = list;
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
    public void showProgress() {
        LoadingDialog.showDialogForLoading((Activity) mContext);
    }

    @Override
    public void stopProgress() {
        LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            switch (type) {
                case EXTRA_KEY_TYPE_NORMAL:
                case EXTRA_KEY_TYPE_WITH_MATERIAL:
                    mPresenter.getDesignProductList(start, false);
                    break;
                case EXTRA_KEY_TYPE_THEME:
                    mPresenter.getAllDesignProductList(start, false);
                    break;
            }
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getDesignProductList(0, false);
    }
}
