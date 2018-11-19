package com.zhaotai.uzao.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainSearchBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.category.material.adapter.MaterialListAdapter;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.designer.adapter.DesignerListAdapter;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.ui.search.contract.CollectionSearchContract;
import com.zhaotai.uzao.ui.search.mode.TabEntity;
import com.zhaotai.uzao.ui.search.presenter.CollectionSearchPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.theme.adapter.NewThemeListAdapter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 搜索页面
 */

public class CollectionSearchActivity extends BaseActivity implements CollectionSearchContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, OnTabSelectListener {

    @BindView(R.id.tl_collection_search)
    CommonTabLayout mTabLayout;

    @BindView(R.id.rl_product_list)
    RecyclerView mRecycler;

    private CollectionSearchPresenter mPresenter;
    private ProductListAdapter mAdapterProduct;
    private MaterialListAdapter mAdapterMaterial;
    private NewThemeListAdapter mAdapterTheme;
    private DesignerListAdapter mAdapterDesigner;
    private PageInfo<GoodsBean> productData;
    private PageInfo<MaterialListBean> materialData;
    private PageInfo<ThemeListBean> themeData;
    private PageInfo<DesignerBean> designerData;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private Map<String, String> params = new HashMap<>();

    private AlertDialog.Builder dialog;

    private static final int TYPE_PRODUCT = 0X01;
    private static final int TYPE_MATERIAL = 0X02;
    private static final int TYPE_THEME = 0X03;
    private static final int TYPE_DESIGNER = 0X04;
    private int categoryType = TYPE_PRODUCT;

    private static final String EXTRA_KEY_SEARCH_COLLECTION_ID = "extra_key_search_collection_id";
    private static final String EXTRA_KEY_SEARCH_TITLE_NAME = "extra_key_search_title_name";

    public static void launch(Context context, String title, String collectionId) {
        Intent intent = new Intent(context, CollectionSearchActivity.class);
        intent.putExtra(EXTRA_KEY_SEARCH_TITLE_NAME, title);
        intent.putExtra(EXTRA_KEY_SEARCH_COLLECTION_ID, collectionId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_collection_search);
        mRecycler.addItemDecoration( new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mTabLayout.setOnTabSelectListener(this);
    }


    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            String collectionId = getIntent().getStringExtra(EXTRA_KEY_SEARCH_COLLECTION_ID);
            String titleName = getIntent().getStringExtra(EXTRA_KEY_SEARCH_TITLE_NAME);
            mTitle.setText(titleName);
            mPresenter = new CollectionSearchPresenter(this, this);
            mPresenter.searchCollection(collectionId);
        }
    }


    @Override
    public void stopLoadingMore() {
        switch (categoryType) {
            case TYPE_PRODUCT:
                if (mAdapterProduct != null)
                    mAdapterProduct.loadMoreComplete();
                break;
            case TYPE_MATERIAL:
                if (mAdapterMaterial != null)
                    mAdapterMaterial.loadMoreComplete();
                break;
            case TYPE_THEME:
                if (mAdapterTheme != null) {
                    mAdapterTheme.loadMoreComplete();
                }
                break;
            case TYPE_DESIGNER:
                if (mAdapterDesigner != null)
                    mAdapterDesigner.loadMoreComplete();
                break;
        }
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {
        switch (categoryType) {
            case TYPE_PRODUCT:
                if (mAdapterProduct != null)
                    mAdapterProduct.loadMoreFail();
                break;
            case TYPE_MATERIAL:
                if (mAdapterMaterial != null)
                    mAdapterMaterial.loadMoreFail();
                break;
            case TYPE_THEME:
                if (mAdapterTheme != null) {
                    mAdapterTheme.loadMoreFail();
                }
                break;
            case TYPE_DESIGNER:
                if (mAdapterDesigner != null) {
                    mAdapterDesigner.loadMoreFail();
                }
                break;
        }
    }

    @Override
    public void onTabSelect(int position) {
        if (mTabEntities.isEmpty()) return;
        TabEntity tab = (TabEntity) mTabEntities.get(position);
        switch (tab.type) {
            case TabEntity.TYPE_PRODUCT://分类栏 商品
                categoryType = TYPE_PRODUCT;
                params.clear();
                params.put("start", "0");
                params.put("needOption", "N");
                params.put("sort", "default_");
                mPresenter.getCommodityList(0, params);
                break;
            case TabEntity.TYPE_MATERIAL://分类栏 素材
                categoryType = TYPE_MATERIAL;
                params.clear();
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", "default_");
                mPresenter.getMaterialList(0, params);
                break;
            case TabEntity.TYPE_THEME://分类栏 主题
                categoryType = TYPE_THEME;
                params.clear();
                params.put("start", "0");
                mPresenter.getThemeList(0, params);
                break;
            case TabEntity.TYPE_DESIGNER://分类栏 设计师
                categoryType = TYPE_DESIGNER;
                params.clear();
                params.put("start", "0");
                mPresenter.getDesignerList(0, params);
                break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    /**
     * 显示全部搜索结果
     */
    @Override
    public void showAllList(MainSearchBean data) {
        params.clear();
        showContent();
        mTabEntities.clear();
        //商品
        if (data.spu != null && data.spu.size() > 0) {
            mTabEntities.add(new TabEntity("商品", TabEntity.TYPE_PRODUCT));
        }
        //素材
        if (data.material != null && data.material.size() > 0) {
            mTabEntities.add(new TabEntity("素材", TabEntity.TYPE_MATERIAL));
        }
        //主题
        if (data.theme != null && data.theme.size() > 0) {
            mTabEntities.add(new TabEntity("主题", TabEntity.TYPE_THEME));
        }
        //设计师
        if (data.designer != null && data.designer.size() > 0) {
            mTabEntities.add(new TabEntity("设计师", TabEntity.TYPE_DESIGNER));
        }
        if (mTabEntities.size() > 1) {
            mTabLayout.setVisibility(View.VISIBLE);
        } else {
            mTabLayout.setVisibility(View.GONE);
        }
        if (mTabEntities.size() > 0) {
            mTabLayout.setTabData(mTabEntities);
            mTabLayout.setCurrentTab(0);
            onTabSelect(0);
        } else {
            showEmpty();
        }
    }

    /**
     * 显示商品数据
     */
    @Override
    public void showCommodityList(PageInfo<GoodsBean> data) {
        if (mAdapterProduct == null) {
            mAdapterProduct = new ProductListAdapter();
            mAdapterProduct.setOnItemClickListener(this);
            mAdapterProduct.setOnLoadMoreListener(this, mRecycler);
        }

        this.productData = data;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
            mRecycler.setAdapter(mAdapterProduct);
            //如果是首页 就设置新数据
            mAdapterProduct.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapterProduct.addData(data.list);
        }
    }

    /**
     * 显示素材数据
     */
    @Override
    public void showMaterialList(PageInfo<MaterialListBean> materialList) {
        if (mAdapterMaterial == null) {
            mAdapterMaterial = new MaterialListAdapter();
            mAdapterMaterial.setOnItemClickListener(this);
            mAdapterMaterial.setOnLoadMoreListener(this, mRecycler);
        }

        this.materialData = materialList;
        if (materialList.currentPage == Constant.CURRENTPAGE_HOME) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
            mRecycler.setAdapter(mAdapterMaterial);
            //如果是首页 就设置新数据
            mAdapterMaterial.setNewData(materialList.list);
        } else {
            //不是第一页 就刷新
            mAdapterMaterial.addData(materialList.list);
        }

    }

    /**
     * 显示主题
     */
    @Override
    public void showThemeList(PageInfo<ThemeListBean> themeList) {
        if (mAdapterTheme == null) {
            mAdapterTheme = new NewThemeListAdapter();
            mAdapterTheme.setOnItemClickListener(this);
            mAdapterTheme.setOnLoadMoreListener(this, mRecycler);
        }
        this.themeData = themeList;
        if (themeData.currentPage == Constant.CURRENTPAGE_HOME) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
            mRecycler.setAdapter(mAdapterTheme);
            //如果是首页 就设置新数据
            mAdapterTheme.setNewData(themeData.list);
        } else {
            //不是第一页 就刷新
            mAdapterTheme.addData(themeData.list);
        }

    }

    /**
     * 显示设计师
     */
    @Override
    public void showDesignerList(PageInfo<DesignerBean> designerList) {
        if (mAdapterDesigner == null) {
            mAdapterDesigner = new DesignerListAdapter();
            mAdapterDesigner.setOnItemClickListener(this);
            mAdapterDesigner.setOnItemChildClickListener(this);
            mAdapterDesigner.setOnLoadMoreListener(this, mRecycler);
        }

        this.designerData = designerList;
        if (designerData.currentPage == Constant.CURRENTPAGE_HOME) {
            mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            mRecycler.setAdapter(mAdapterDesigner);
            //如果是首页 就设置新数据
            mAdapterDesigner.setNewData(designerData.list);
        } else {
            //不是第一页 就刷新
            mAdapterDesigner.addData(designerData.list);
        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (categoryType) {
            case TYPE_PRODUCT:
                GoodsBean itemProduct = (GoodsBean) adapter.getItem(position);
                if (itemProduct != null) {
                    CommodityDetailMallActivity.launch(mContext, itemProduct.id);
                }
                break;
            case TYPE_MATERIAL:
                MaterialListBean itemMaterial = (MaterialListBean) adapter.getItem(position);
                if (itemMaterial != null) {
                    MaterialDetailActivity.launch(mContext, itemMaterial.id);
                }
                break;
            case TYPE_THEME:
                ThemeListBean itemTheme = (ThemeListBean) adapter.getItem(position);
                if (itemTheme != null) {
                    ThemeDetailActivity.launch(mContext, itemTheme.getId());
                }
                break;
            case TYPE_DESIGNER:
                DesignerBean itemDesigner = (DesignerBean) adapter.getItem(position);
                if (itemDesigner != null) {
                    DesignerActivity.launch(mContext, itemDesigner.userId);
                }
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if (!LoginHelper.getLoginStatus()) {
            ToastUtil.showShort("请先登录");
            LoginActivity.launch(this);
        } else {
            if (mAdapterDesigner != null) {
                final DesignerBean designerBean = mAdapterDesigner.getData().get(position);
                String isFavorited = designerBean.data.isFavorited;
                if ("Y".equals(isFavorited)) {
//            关注状态
                    if (dialog == null) {
                        dialog = new AlertDialog.Builder(this);
                        dialog.setMessage("确定不再关注此人?");
                        dialog.setCancelable(false);
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.cancelDesigner(position, designerBean.userId);
                            }
                        });
                    }
                    dialog.show();

                } else {
                    mPresenter.attentionDesigner(position, designerBean.userId);
                }
            }
        }
    }

    @Override
    public void changeDesigner(int pos, boolean status) {
        if (mAdapterDesigner != null) {
            DesignerBean designerBean = mAdapterDesigner.getData().get(pos);
            if (status) {
                designerBean.favoriteCount = designerBean.favoriteCount + 1;
            } else {
                designerBean.favoriteCount = designerBean.favoriteCount - 1;
            }
            designerBean.data.isFavorited = status ? "Y" : "N";
            mAdapterDesigner.notifyItemChanged(pos);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        switch (categoryType) {
            case TYPE_PRODUCT:
                if (productData != null && productData.hasNextPage) {
                    int start = productData.pageStartRow + productData.pageRecorders;
                    //加载列表数据
                    if (params != null && !params.isEmpty()) {
                        params.remove("start");
                        params.put("start", String.valueOf(start));
                        mPresenter.getCommodityList(start, params);
                    }
                } else {
                    if (mAdapterProduct != null)
                        mAdapterProduct.loadMoreEnd();
                }
                break;
            case TYPE_MATERIAL:
                if (materialData != null && materialData.hasNextPage) {
                    int start = materialData.pageStartRow + materialData.pageRecorders;
                    //加载列表数据
                    if (params != null && !params.isEmpty()) {
                        params.remove("start");
                        params.put("start", String.valueOf(start));
                        mPresenter.getMaterialList(start, params);
                    }
                } else {
                    if (mAdapterMaterial != null)
                        mAdapterMaterial.loadMoreEnd();
                }
                break;
            case TYPE_THEME:
                if (themeData != null && themeData.hasNextPage) {
                    int start = themeData.pageStartRow + themeData.pageRecorders;
                    //加载列表数据
                    if (params != null && !params.isEmpty()) {
                        params.remove("start");
                        params.put("start", String.valueOf(start));
                        mPresenter.getThemeList(start, params);
                    }
                } else {
                    if (mAdapterTheme != null)
                        mAdapterTheme.loadMoreEnd();
                }
                break;
            case TYPE_DESIGNER:
                if (designerData != null && designerData.hasNextPage) {
                    int start = designerData.pageStartRow + designerData.pageRecorders;
                    //加载列表数据
                    if (params != null && !params.isEmpty()) {
                        params.remove("start");
                        params.put("start", String.valueOf(start));
                        mPresenter.getDesignerList(start, params);
                    }
                } else {
                    if (mAdapterDesigner != null)
                        mAdapterDesigner.loadMoreEnd();
                }
                break;
        }
    }

}
