package com.zhaotai.uzao.ui.designer.activity;

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
import com.zhaotai.uzao.adapter.DesignerMaterialAdapter;
import com.zhaotai.uzao.adapter.DesignerProductShowAdapter;
import com.zhaotai.uzao.adapter.DesignerThemeAdapter;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.designer.contract.DesignerListContract;
import com.zhaotai.uzao.ui.designer.presenter.DesignerListPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/22
 * Created by LiYou
 * Description : 设计师搜索
 */

public class DesignerSearchActivity extends BaseActivity implements DesignerListContract.View, TextView.OnEditorActionListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.etd_text)
    EditWithDelView mEtSearch;

    @BindView(R.id.recycler_brand)
    RecyclerView mRecycler;

    private DesignerListPresenter mPresenter;
    private DesignerProductShowAdapter mProductAdapter;
    private DesignerMaterialAdapter mMaterialAdapter;
    private DesignerThemeAdapter mThemeAdapter;

    /**
     * 搜索商品
     */
    public static final int PRODUCT = 0;
    /**
     * 搜索素材
     */
    public static final int MATERIAL = 1;
    /**
     * 搜索 主题
     */
    public static final int THEME = 2;

    private PageInfo<GoodsBean> productData = new PageInfo<>();
    private PageInfo<MaterialListBean> materialData = new PageInfo<>();
    private PageInfo<ThemeBean> themeData = new PageInfo<>();

    private int type;
    private String designerId;
    /**
     * 搜索内容
     */
    private String searchWord = "";

    /**
     * 设计师主页搜索
     *
     * @param context    上下文
     * @param designerId 设计师id
     * @param type       类型
     */
    public static void launch(Context context, String designerId, int type) {
        Intent intent = new Intent(context, DesignerSearchActivity.class);
        intent.putExtra("designerId", designerId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_search);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));
        mEtSearch.setOnEditorActionListener(this);
        mPresenter = new DesignerListPresenter(this, this);

        type = getIntent().getIntExtra("type", PRODUCT);
        switch (type) {
            case PRODUCT:
                mProductAdapter = new DesignerProductShowAdapter();
                mProductAdapter.setOnLoadMoreListener(this, mRecycler);
                mRecycler.setAdapter(mProductAdapter);
                mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
                        if (goodsBean != null) {
                            CommodityDetailMallActivity.launch(mContext, goodsBean.id);
                        }
                    }
                });
                break;
            case MATERIAL:
                mMaterialAdapter = new DesignerMaterialAdapter();
                mMaterialAdapter.setOnLoadMoreListener(this, mRecycler);
                mRecycler.setAdapter(mMaterialAdapter);
                mMaterialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        MaterialListBean materialListBean = (MaterialListBean) adapter.getItem(position);
                        if (materialListBean != null) {
                            MaterialDetailActivity.launch(mContext, materialListBean.id);
                        }
                    }
                });
                break;
            case THEME:
                mThemeAdapter = new DesignerThemeAdapter();
                mThemeAdapter.setOnLoadMoreListener(this, mRecycler);
                mRecycler.setAdapter(mThemeAdapter);
                mThemeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        ThemeBean themeBean = (ThemeBean) adapter.getItem(position);
                        if (themeBean != null) {
                            ThemeDetailActivity.launch(mContext, themeBean.id);
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void initData() {
        designerId = getIntent().getStringExtra("designerId");
        doSearch();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            ToastUtil.showShort("输入不能为空");
            return;
        }
        searchWord = mEtSearch.getText().toString();
        showLoading();
        doSearch();
    }

    private void doSearch() {
        switch (type) {
            case PRODUCT:
                mPresenter.getDesignerProductList(0, designerId, searchWord);
                break;
            case MATERIAL:
                mPresenter.getDesignerMaterialList(0, designerId, searchWord);
                break;
            case THEME:
                mPresenter.getDesignerThemeList(0, designerId, searchWord);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
                ToastUtil.showShort("输入不能为空");
            } else {
                searchWord = mEtSearch.getText().toString();
                doSearch();
            }
            return true;
        }
        return false;
    }

    @Override
    public void stopLoadingMore() {
        switch (type) {
            case PRODUCT:
                mProductAdapter.loadMoreComplete();
                break;
            case MATERIAL:
                mMaterialAdapter.loadMoreComplete();
                break;
            case THEME:
                mThemeAdapter.loadMoreComplete();
                break;
        }
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {
        switch (type) {
            case PRODUCT:
                mProductAdapter.loadMoreFail();
                break;
            case MATERIAL:
                mMaterialAdapter.loadMoreFail();
                break;
            case THEME:
                mThemeAdapter.loadMoreFail();
                break;
        }
    }

    @Override
    public void showProduct(PageInfo<GoodsBean> goodsBean) {
        productData = goodsBean;
        if (productData.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mProductAdapter.setNewData(productData.list);
        } else {
            //不是第一页 就刷新
            mProductAdapter.addData(productData.list);
        }
    }

    @Override
    public void showMaterial(PageInfo<MaterialListBean> materialList) {
        materialData = materialList;
        if (materialData.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mMaterialAdapter.setNewData(materialData.list);
        } else {
            //不是第一页 就刷新
            mMaterialAdapter.addData(materialData.list);
        }
    }

    @Override
    public void showTheme(PageInfo<ThemeBean> themeBean) {
        themeData = themeBean;
        if (themeData.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mThemeAdapter.setNewData(themeData.list);
        } else {
            //不是第一页 就刷新
            mThemeAdapter.addData(themeData.list);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        switch (type) {
            case PRODUCT:
                if (productData.hasNextPage) {
                    int start = productData.pageStartRow + productData.pageRecorders;
                    //加载列表数据
                    mPresenter.getDesignerProductList(start, designerId, searchWord);
                } else {
                    mProductAdapter.loadMoreEnd();
                }
                break;
            case MATERIAL:
                if (materialData.hasNextPage) {
                    int start = materialData.pageStartRow + materialData.pageRecorders;
                    //加载列表数据
                    mPresenter.getDesignerMaterialList(start, designerId, searchWord);
                } else {
                    mMaterialAdapter.loadMoreEnd();
                }
                break;
            case THEME:
                if (themeData.hasNextPage) {
                    int start = themeData.pageStartRow + themeData.pageRecorders;
                    //加载列表数据
                    mPresenter.getDesignerThemeList(start, designerId, searchWord);
                } else {
                    mThemeAdapter.loadMoreEnd();
                }
                break;
        }
    }
}
