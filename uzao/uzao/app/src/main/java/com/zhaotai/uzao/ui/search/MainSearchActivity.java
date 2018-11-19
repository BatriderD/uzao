package com.zhaotai.uzao.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MainSearchBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.category.material.adapter.MaterialListAdapter;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.designer.adapter.DesignerListAdapter;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.search.adapter.AssociateAdapter;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.ui.search.contract.MainSearchContract;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.ui.search.mode.TabEntity;
import com.zhaotai.uzao.ui.search.presenter.MainSearchPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.theme.adapter.NewThemeListAdapter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 主搜索页面
 */

public class MainSearchActivity extends BaseFragmentActivity implements MainSearchContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, TextView.OnEditorActionListener, BaseQuickAdapter.OnItemChildClickListener,
        OnTabSelectListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.tl_main_search)
    CommonTabLayout mTabLayout;

    @BindView(R.id.ll_tab_product)
    LinearLayout mLlTabProduct;
    @BindView(R.id.ll_tab_material)
    LinearLayout mLlTabMaterial;

    @BindView(R.id.etd_text)
    EditWithDelView mSearchText;

    //商品筛选tab
    @BindView(R.id.tv_product_tab_sort_comprehensive)
    TextView mTextProductComprehensive;
    @BindView(R.id.tv_product_tab_price)
    TextView mTextProductPrice;
    @BindView(R.id.iv_product_tab_price)
    ImageView mImageProductPrice;
    @BindView(R.id.tv_product_tab_count)
    TextView mTextProductCount;
    @BindView(R.id.iv_product_tab_count)
    ImageView mImageProductCount;

    //素材筛选tab
    @BindView(R.id.tv_material_tab_sort_comprehensive)
    TextView mTextMaterialComprehensive;
    @BindView(R.id.tv_material_tab_sort_popular)
    TextView mTextMaterialPopular;
    @BindView(R.id.tv_material_tab_sort_sale)
    TextView mTextMaterialSale;
    @BindView(R.id.tv_material_tab_sort_time)
    TextView mTextMaterialTime;

    @BindView(R.id.recycler_relate)
    RecyclerView mRecyclerRelate;

    //搜索
    @BindView(R.id.ll_search_content)
    LinearLayout mSearchContent;

    //结果页面
    @BindView(R.id.ll_product_content)
    LinearLayout mProductContent;

    @BindView(R.id.rl_product_list)
    RecyclerView mRecycler;

    private MainSearchPresenter mPresenter;
    private ProductListAdapter mAdapterProduct;
    private MaterialListAdapter mAdapterMaterial;
    private NewThemeListAdapter mAdapterTheme;
    private DesignerListAdapter mAdapterDesigner;
    private PageInfo<GoodsBean> productData;
    private PageInfo<MaterialListBean> materialData;
    private PageInfo<ThemeListBean> themeData;
    private PageInfo<DesignerBean> designerData;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String searchWord;
    private boolean sortPrice;//false desc true asc
    private boolean sortCount;
    private boolean isNeedAssociate = true; //记录是否需要联想搜索
    private int tabProductPosition = 1;
    private int tabMaterialPosition = 1;
    private Map<String, String> params = new HashMap<>();

    //    private String historySearch;//历史搜索记录
    private List<String> historyList;
    private AssociateAdapter associateAdapter;
    private AlertDialog.Builder dialog;
    private String sort = "default_";//默认排序

    private static final int TYPE_PRODUCT = 0X01;
    private static final int TYPE_MATERIAL = 0X02;
    private static final int TYPE_THEME = 0X03;
    private static final int TYPE_DESIGNER = 0X04;
    private int categoryType = TYPE_PRODUCT;

    private static final String EXTRA_KEY_SEARCH_PRESET_WORD = "extra_key_search_preset_word";
    private FilterFragment mFilterFragment;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainSearchActivity.class));
    }

    public static void launch(Context context, String searchWord) {
        Intent intent = new Intent(context, MainSearchActivity.class);
        intent.putExtra(EXTRA_KEY_SEARCH_PRESET_WORD, searchWord);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_search);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        //当点击搜索框时 弹出历史记录
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContent.setVisibility(View.VISIBLE);
                mRecyclerRelate.setVisibility(View.GONE);
                mProductContent.setVisibility(View.GONE);
                showHistory();
            }
        });
        mSearchText.setOnEditorActionListener(this);
        //取消侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        associateAdapter = new AssociateAdapter();
        mRecyclerRelate.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerRelate.setAdapter(associateAdapter);
        associateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AssociateBean associateBean = (AssociateBean) adapter.getItem(position);
                if (associateBean != null) {
                    searchWord = associateBean.word;
                    setSearchText(associateBean.word);
                    mPresenter.searchAll(associateBean.word);
                    saveHistory(associateBean.word);
                }
            }
        });

        showHistory();

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtil.isEmpty(s)) {
                    mSearchContent.setVisibility(View.VISIBLE);
                    mRecyclerRelate.setVisibility(View.GONE);
                    mProductContent.setVisibility(View.GONE);
                } else if (isNeedAssociate) {
                    mSearchContent.setVisibility(View.GONE);
                    mRecyclerRelate.setVisibility(View.VISIBLE);
                    mProductContent.setVisibility(View.GONE);
                    mPresenter.getAssociate(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTabLayout.setOnTabSelectListener(this);
        mFilterFragment = FilterFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_filter_drawer, mFilterFragment).commit();
        mFilterFragment.setOnFilterListener(new OnFilterTagClickListener() {
            @Override
            public void onTagSelect(Map<String, String> paramss) {
                params = paramss;
                params.put("start", "0");
                params.put("needOption", "N");
                params.put("sort", sort);
                params.put("queryWord", searchWord);
                switch (categoryType) {
                    case TYPE_PRODUCT:
                        mPresenter.getCommodityList(0, params);
                        break;
                    case TYPE_MATERIAL:
                        mPresenter.getMaterialList(0, params);
                        break;
                }
            }

            @Override
            public void reset() {

            }

            @Override
            public void closeDrawer() {
                KeyboardUtils.hideSoftInput(MainSearchActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    /**
     * 显示历史记录
     */
    public void showHistory() {
        //历史搜索
        String historySearch = SPUtils.getSharedStringData(GlobalVariable.HISTORY_SEARCH);
        try {
            historyList = GsonUtil.getGson().fromJson(historySearch, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            Log.d("搜索", "搜索历史为空");
        }

        if (historyList == null) {
            historyList = new ArrayList<>();
            mSearchContent.setVisibility(View.GONE);
        }
        if (historyList.size() > 0) {
            mFlowLayout.setAdapter(new TagAdapter<String>(historyList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_history_search,
                            mFlowLayout, false);
                    tv.setText(s);
                    return tv;
                }
            });
            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    KeyboardUtils.hideSoftInput(MainSearchActivity.this);
                    setSearchText(historyList.get(position));
                    searchWord = historyList.get(position);
                    mPresenter.searchAll(searchWord);
                    return true;
                }
            });
        }
    }

    /**
     * 删除历史记录
     */
    @OnClick(R.id.iv_delete_history)
    public void onClickDeleteHistory() {
        historyList.clear();
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_SEARCH, "");
        mFlowLayout.setAdapter(new TagAdapter<String>(new ArrayList<String>()) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_history_search,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }


    /**
     * 搜索
     */
    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isTrimEmpty(mSearchText.getText().toString())) {
            ToastUtil.showShort("请输入商品名");
            return;
        }
        KeyboardUtils.hideSoftInput(this);

        searchWord = mSearchText.getText().toString();

        mPresenter.searchAll(searchWord);
        saveHistory(searchWord);
    }

    /**
     * 保存历史
     */
    private void saveHistory(String word) {
        int oldPosition = -1;
        for (int i = 0; i < historyList.size(); i++) {
            if (word.equals(historyList.get(i))) {
                oldPosition = i;
            }
        }
        if (oldPosition >= 0) {
            historyList.remove(oldPosition);
        }
        historyList.add(0, word);
        if (historyList.size() > 20) {
            historyList.remove(20);
        }
        String historySearch = GsonUtil.t2Json2(historyList);
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_SEARCH, historySearch);
    }

    /**
     * 填写搜索框文字
     */
    public void setSearchText(String searchText) {
        isNeedAssociate = false;
        mSearchText.setText(searchText);
        mSearchText.setSelection(searchText.length());
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
            String stringExtra = getIntent().getStringExtra(EXTRA_KEY_SEARCH_PRESET_WORD);
            if (!StringUtil.isEmpty(stringExtra)) {
                mSearchText.setHint(stringExtra);
            }
            mPresenter = new MainSearchPresenter(this, this);
        }
    }

    /**
     * 商品的筛选tab 切换颜色
     *
     * @param position 已点击位置
     */
    private void changeProductTab(int position) {
        switch (position) {
            case 1:
                if (tabProductPosition != 1) {
                    mTextProductComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextProductCount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mImageProductPrice.setImageResource(R.drawable.ic_price_normal);
                    mImageProductCount.setImageResource(R.drawable.ic_price_normal);
                }
                break;
            case 2:
                if (tabProductPosition != 2) {
                    mTextProductComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextProductCount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mImageProductCount.setImageResource(R.drawable.ic_price_normal);
                } else {
                    sortPrice = !sortPrice;
                }

                if (sortPrice) {
                    mImageProductPrice.setImageResource(R.drawable.ic_price_up);
                } else {
                    mImageProductPrice.setImageResource(R.drawable.ic_price_down);
                }
                break;
            case 3:
                if (tabProductPosition != 3) {
                    mTextProductComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextProductCount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mImageProductPrice.setImageResource(R.drawable.ic_price_normal);
                } else {
                    sortCount = !sortCount;
                }

                if (sortCount) {
                    mImageProductCount.setImageResource(R.drawable.ic_price_up);
                } else {
                    mImageProductCount.setImageResource(R.drawable.ic_price_down);
                }
                break;
        }
        tabProductPosition = position;
    }

    /**
     * 素材的筛选tab 颜色
     *
     * @param position 点击位置
     */
    private void changeMaterialTab(int position) {
        switch (position) {
            case 1:
                if (tabMaterialPosition != 1) {
                    mTextMaterialComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextMaterialPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 2:
                if (tabMaterialPosition != 2) {
                    mTextMaterialComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialPopular.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextMaterialSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 3:
                if (tabMaterialPosition != 3) {
                    mTextMaterialComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialSale.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextMaterialTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 4:
                if (tabMaterialPosition != 4) {
                    mTextMaterialComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextMaterialTime.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                }
                break;
        }
        tabMaterialPosition = position;
    }

    /**
     * 综合排序--商品
     */
    @OnClick(R.id.tv_product_tab_sort_comprehensive)
    public void onClickProductCoprehensive() {
        changeProductTab(1);
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("queryWord", searchWord);
        params.put("sort", "default_");
        mPresenter.getCommodityList(0, params);
        sort = "default_";
    }

    /**
     * 价格--商品
     */
    @OnClick(R.id.rl_product_tab_sort_price)
    public void onClickProductPrice() {
        changeProductTab(2);
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("queryWord", searchWord);
        if (sortPrice) {
            sort = "price-asc";
            params.put("sort", "price-asc");
        } else {
            sort = "price-desc";
            params.put("sort", "price-desc");
        }
        mPresenter.getCommodityList(0, params);
    }

    /**
     * 销量--商品
     */
    @OnClick(R.id.rl_product_tab_sort_count)
    public void onClickProductCount() {
        changeProductTab(3);
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("queryWord", searchWord);
        if (sortCount) {
            sort = "salesCount-asc";
            params.put("sort", "salesCount-asc");
        } else {
            sort = "salesCount-desc";
            params.put("sort", "salesCount-desc");
        }
        mPresenter.getCommodityList(0, params);
    }

    /**
     * 商品筛选
     */
    @OnClick(R.id.rl_product_tab_sort_filter)
    public void onCLickProductFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    /**
     * 综合排序--素材
     */
    @OnClick(R.id.tv_material_tab_sort_comprehensive)
    public void onClickMaterialComprehensive() {
        changeMaterialTab(1);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "default_");
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "default_";
    }

    /**
     * 人气--素材
     */
    @OnClick(R.id.tv_material_tab_sort_popular)
    public void onClickMaterialPopular() {
        changeMaterialTab(2);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "popular-desc");
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "popular-desc";
    }

    /**
     * 销量--素材
     */
    @OnClick(R.id.tv_material_tab_sort_sale)
    public void onClickMaterialSales() {
        changeMaterialTab(3);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "salesCount-desc");
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "salesCount-desc";
    }

    /**
     * 时间--素材
     */
    @OnClick(R.id.tv_material_tab_sort_time)
    public void onClickMaterialTime() {
        changeMaterialTab(4);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "upDate-desc");
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "upDate-desc";
    }

    /**
     * 商品筛选
     */
    @OnClick(R.id.tv_material_tab_sort_filter)
    public void onCLickMaterialFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
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
                sort = "default_";
                selectCategoryTab(0);
                changeProductTab(1);
                mFilterFragment.setPriceReset();
                params.clear();
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", "default_");
                params.put("queryWord", searchWord);
                mPresenter.getCommodityList(0, params);
                break;
            case TabEntity.TYPE_MATERIAL://分类栏 素材
                sort = "default_";
                selectCategoryTab(1);
                changeMaterialTab(1);
                params.clear();
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", "default_");
                params.put("queryWord", searchWord);
                mPresenter.getMaterialList(0, params);
                break;
            case TabEntity.TYPE_THEME://分类栏 主题
                selectCategoryTab(2);
                params.clear();
                params.put("start", "0");
                params.put("queryWord", searchWord);
                mPresenter.getThemeList(0, params);
                break;
            case TabEntity.TYPE_DESIGNER://分类栏 设计师
                selectCategoryTab(3);
                params.clear();
                params.put("start", "0");
                params.put("queryWord", searchWord);
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
        isNeedAssociate = true;
        params.clear();
        showContent();
        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.GONE);
        mProductContent.setVisibility(View.VISIBLE);
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
        if (mTabEntities.isEmpty()) {
            showEmpty();
        } else {
            mTabLayout.setTabData(mTabEntities);
            mTabLayout.setCurrentTab(0);
            onTabSelect(0);
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

        mFilterFragment.setBottomOKText("确定(" + data.list.size() + "个商品)");
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
        mFilterFragment.setBottomOKText("确定(" + materialData.list.size() + "个素材)");
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

    /**
     * 显示筛选
     */
    @Override
    public void showFilter(ProductOptionBean opt) {
        mFilterFragment.showFilter(opt);
    }

    /**
     * 关键字联想
     */
    @Override
    public void showAssociateList(List<AssociateBean> associateList) {
        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.VISIBLE);
        mProductContent.setVisibility(View.GONE);
        associateAdapter.setNewData(associateList);
    }

    /**
     * 分类
     *
     * @param position 位置
     */
    @Override
    public void selectCategoryTab(int position) {
        switch (position) {
            case 0:
                categoryType = TYPE_PRODUCT;
                mLlTabProduct.setVisibility(View.VISIBLE);
                mLlTabMaterial.setVisibility(View.GONE);
                break;
            case 1:
                categoryType = TYPE_MATERIAL;
                mLlTabProduct.setVisibility(View.GONE);
                mLlTabMaterial.setVisibility(View.VISIBLE);
                break;
            case 2:
                categoryType = TYPE_THEME;
                mLlTabProduct.setVisibility(View.GONE);
                mLlTabMaterial.setVisibility(View.GONE);
                break;
            case 3:
                categoryType = TYPE_DESIGNER;
                mLlTabProduct.setVisibility(View.GONE);
                mLlTabMaterial.setVisibility(View.GONE);
                break;
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch();
            return true;
        }
        return false;
    }

}
