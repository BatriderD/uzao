package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.goods.contract.CommodityListContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CommodityPresenter;
import com.zhaotai.uzao.ui.search.CommodityAndSearchActivity;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description :  分类 商品列表
 */

public class CategoryListActivity extends BaseFragmentActivity implements CommodityListContract.View
        , BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.recycler_category_list)
    RecyclerView mRecycler;

    @BindView(R.id.tv_product_tab_sort_comprehensive)
    TextView mTextComprehensive;
    @BindView(R.id.tv_product_tab_price)
    TextView mTextPrice;
    @BindView(R.id.iv_product_tab_price)
    ImageView mImagePrice;
    @BindView(R.id.tv_product_tab_count)
    TextView mTextCount;
    @BindView(R.id.iv_product_tab_count)
    ImageView mImageCount;

    private ProductListAdapter mAdapter;
    private CommodityPresenter mPresenter;
    private PageInfo<GoodsBean> data = new PageInfo<>();
    /**
     * 价格 升序 还是降序
     */
    private boolean sortPrice;//false desc true asc
    /**
     * 销量 升序还是降序
     */
    private boolean sortCount;
    /**
     * tab 切换位置
     */
    private int tabPosition = 1;
    private Map<String, String> params = new HashMap<>();
    private FilterFragment mFilterFragment;
    private boolean isNavigate = true;//是否导航列表
    /**
     * 排序方式
     */
    private String sort = "default_";

    //导航列表
    private static final String EXTRA_KEY_TYPE_NAVIGATE = "extra_key_type_navigate";
    //标签列表
    private static final String EXTRA_KEY_TYPE_TAG = "extra_key_type_tag";


    public static void launchNavigate(Context context, String code, String name) {
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra("type", EXTRA_KEY_TYPE_NAVIGATE);
        intent.putExtra("categoryCode", code);
        intent.putExtra("categoryName", name);
        context.startActivity(intent);
    }

    /**
     * 从标签跳转进来
     *
     * @param context 上下文
     * @param tagCode 标签id
     * @param tagName 标签名称
     */
    public static void launch(Context context, String tagCode, String tagName) {
        Intent intent = new Intent(context, CategoryListActivity.class);
        intent.putExtra("type", EXTRA_KEY_TYPE_TAG);
        intent.putExtra("tagCode", tagCode);
        intent.putExtra("categoryName", tagName);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_category_list);
        mAdapter = new ProductListAdapter();
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);

        mPresenter = new CommodityPresenter(this, this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyView(R.layout.vw_empty,multipleStatusView);
        //取消侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mFilterFragment = FilterFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_filter_drawer, mFilterFragment).commit();
        //监听筛选回调
        mFilterFragment.setOnFilterListener(new OnFilterTagClickListener() {
            @Override
            public void onTagSelect(Map<String, String> paramss) {
                params = paramss;
                params.put("needOption", "N");
                params.put("sort", sort);
                mPresenter.getCategoryList(0, false, params);
            }

            @Override
            public void reset() {
                params.clear();
                changeTab(1);
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", "default_");
                mPresenter.getCategoryList(0, false, params);
            }

            @Override
            public void closeDrawer() {
                //收起软键盘
                KeyboardUtils.hideSoftInput(CategoryListActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });

    }

    @Override
    protected void initData() {
        showLoading();
        String categoryName = getIntent().getStringExtra("categoryName");
        mTitle.setText(categoryName);
        params.clear();
        params.put("needOption", "Y");
        params.put("sort", "default_");

        switch (getIntent().getStringExtra("type")) {
            case EXTRA_KEY_TYPE_NAVIGATE:
                mPresenter.getNavigateList(0, getIntent().getStringExtra("categoryCode"), true);
                break;
            case EXTRA_KEY_TYPE_TAG:
                params.put("tags", getIntent().getStringExtra("tagCode"));
                mPresenter.getCategoryList(0, true, params);
                break;
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 切换tab栏
     *
     * @param position 点击位置
     */
    private void changeTab(int position) {
        switch (position) {
            case 1://综合
                if (tabPosition != 1) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextPrice.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextCount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mImagePrice.setImageResource(R.drawable.ic_price_normal);
                    mImageCount.setImageResource(R.drawable.ic_price_normal);
                }
                break;
            case 2://价格
                if (tabPosition != 2) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextCount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mImageCount.setImageResource(R.drawable.ic_price_normal);
                } else {
                    sortPrice = !sortPrice;
                }

                if (sortPrice) {
                    mImagePrice.setImageResource(R.drawable.ic_price_up);
                } else {
                    mImagePrice.setImageResource(R.drawable.ic_price_down);
                }
                break;
            case 3://销量
                if (tabPosition != 3) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextPrice.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextCount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mImagePrice.setImageResource(R.drawable.ic_price_normal);
                } else {
                    sortCount = !sortCount;
                }

                if (sortCount) {
                    mImageCount.setImageResource(R.drawable.ic_price_up);
                } else {
                    mImageCount.setImageResource(R.drawable.ic_price_down);
                }
                break;
        }
        tabPosition = position;
    }

    /**
     * 综合排序
     */
    @OnClick(R.id.tv_product_tab_sort_comprehensive)
    public void onClickCoprehensive() {
        changeTab(1);
        params.put("needOption", "N");
        params.put("sort", "default_");
        mPresenter.getCategoryList(0, false, params);
        sort = "default_";
    }

    /**
     * 价格
     */
    @OnClick(R.id.rl_product_tab_sort_price)
    public void onClickPrice() {
        changeTab(2);
        params.put("needOption", "N");
        if (sortPrice) {
            params.put("sort", "price-asc");
        } else {
            params.put("sort", "price-desc");
        }
        mPresenter.getCategoryList(0, false, params);
        sort = params.get("sort");
    }

    /**
     * 销量
     */
    @OnClick(R.id.rl_product_tab_sort_count)
    public void onClickCount() {
        changeTab(3);
        params.put("needOption", "N");
        if (sortCount) {
            params.put("sort", "salesCount-asc");
        } else {
            params.put("sort", "salesCount-desc");
        }
        mPresenter.getCategoryList(0, false, params);
        sort = params.get("sort");
    }

    /**
     * 打开筛选
     */
    @OnClick(R.id.rl_product_tab_sort_filter)
    public void onCLickFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    /**
     * 跳转搜索
     */
    @OnClick(R.id.right_btn)
    public void goToSearch() {
        CommodityAndSearchActivity.launch(mContext);
    }

    @Override
    public void showCategoryList(PageInfo<GoodsBean> list) {
        isNavigate = false;
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

    }


    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }


    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            if (isNavigate) {
                mPresenter.getNavigateList(start, getIntent().getStringExtra("categoryCode"), false);
            } else {
                if (params != null && !params.isEmpty()) {
                    params.remove("start");
                    mPresenter.getCategoryList(start, false, params);
                }
            }
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
        if (goodsBean != null) {
            CommodityDetailMallActivity.launch(mContext, goodsBean.id);
        }
    }
}
