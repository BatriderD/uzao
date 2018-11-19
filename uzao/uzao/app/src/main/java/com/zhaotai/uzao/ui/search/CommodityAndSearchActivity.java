package com.zhaotai.uzao.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.search.adapter.AssociateAdapter;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.ui.search.contract.CommodityAndSearchContract;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.ui.search.presenter.CommodityAndSearchPresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
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
 * Description : 搜索页面
 */

public class CommodityAndSearchActivity extends BaseFragmentActivity implements CommodityAndSearchContract.View, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, TextView.OnEditorActionListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.etd_text)
    EditWithDelView mSearchText;
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

    private CommodityAndSearchPresenter mPresenter;
    private ProductListAdapter mAdapter;
    private PageInfo<GoodsBean> data;
    private String searchWord;
    private boolean sortPrice;//false desc true asc
    private boolean sortCount;
    private int tabPosition = 1;
    private boolean isNeedAssociate = true; //记录是否需要联想搜索
    private Map<String, String> params = new HashMap<>();

    //    private String historySearch;//历史搜索记录
    private List<String> historyList;
    private AssociateAdapter associateAdapter;
    private FilterFragment mFilterFragment;
    private String sort = "default_";

    private static final String EXTRA_KEY_SEARCH_PRESET_WORD = "extra_key_search_preset_word";

    public static void launch(Context context) {
        context.startActivity(new Intent(context, CommodityAndSearchActivity.class));
    }

    public static void launch(Context context, String searchWord) {
        Intent intent = new Intent(context, CommodityAndSearchActivity.class);
        intent.putExtra(EXTRA_KEY_SEARCH_PRESET_WORD, searchWord);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        mAdapter = new ProductListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);

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
                    mSearchContent.setVisibility(View.GONE);
                    mRecyclerRelate.setVisibility(View.GONE);
                    mProductContent.setVisibility(View.VISIBLE);
                    mFilterFragment.setPriceReset();
                    setSearchText(associateBean.word);
                    changeTab(1);
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "Y");
                    params.put("sort", "default_");
                    params.put("queryWord", associateBean.word);
                    mPresenter.getCommodityList(0, params);
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
                } else if (isNeedAssociate){
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
                mPresenter.getCommodityList(0, params);
            }

            @Override
            public void reset() {
                params.clear();
                changeTab(1);
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", "default_");
                params.put("queryWord", searchWord);
                mPresenter.getCommodityList(0, params);
            }

            @Override
            public void closeDrawer() {
                KeyboardUtils.hideSoftInput(CommodityAndSearchActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    /**
     * 填写搜索框文字
     */
    public void setSearchText(String searchText) {
        isNeedAssociate = false;
        mSearchText.setText(searchText);
        mSearchText.setSelection(searchText.length());
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
                    KeyboardUtils.hideSoftInput(CommodityAndSearchActivity.this);
                    mSearchText.setText(historyList.get(position));
                    mSearchText.setSelection(mSearchText.getText().toString().length());
                    mFilterFragment.setPriceReset();
                    setSearchText(historyList.get(position));
                    changeTab(1);
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "Y");
                    params.put("sort", "default_");
                    params.put("queryWord", historyList.get(position));
                    searchWord = historyList.get(position);
                    mPresenter.getCommodityList(0, params);
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
        mFilterFragment.setPriceReset();
        changeTab(1);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("sort", "default_");
        params.put("queryWord", searchWord);
        mPresenter.getCommodityList(0, params);
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
            mPresenter = new CommodityAndSearchPresenter(this, this);
        }
    }

    private void changeTab(int position) {
        switch (position) {
            case 1:
                sort = "default_";
                if (tabPosition != 1) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTextPrice.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTextCount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mImagePrice.setImageResource(R.drawable.ic_price_normal);
                    mImageCount.setImageResource(R.drawable.ic_price_normal);
                }
                break;
            case 2:
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
            case 3:
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
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("queryWord", searchWord);
        params.put("sort", "default_");
        mPresenter.getCommodityList(0, params);
        sort = "default_";
    }

    /**
     * 价格
     */
    @OnClick(R.id.rl_product_tab_sort_price)
    public void onClickPrice() {
        changeTab(2);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("queryWord", searchWord);
        if (sortPrice) {
            params.put("sort", "price-asc");
        } else {
            params.put("sort", "price-desc");
        }
        mPresenter.getCommodityList(0, params);
        sort = params.get("sort");
    }

    /**
     * 销量
     */
    @OnClick(R.id.rl_product_tab_sort_count)
    public void onClickCount() {
        changeTab(3);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("queryWord", searchWord);
        if (sortCount) {
            params.put("sort", "salesCount-asc");
        } else {
            params.put("sort", "salesCount-desc");
        }
        mPresenter.getCommodityList(0, params);
        sort = params.get("sort");
    }

    /**
     * 筛选
     */
    @OnClick(R.id.rl_product_tab_sort_filter)
    public void onCLickFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
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
    public void showCommodityList(PageInfo<GoodsBean> data) {
        isNeedAssociate = true;
        if (data.totalRows == 0) {
            showEmpty(getString(R.string.empty_search));
        } else {
            showContent();
        }
        mFilterFragment.setBottomOKText("确定(" + data.list.size() + "个商品)");
        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.GONE);
        mProductContent.setVisibility(View.VISIBLE);
        this.data = data;
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


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean item = (GoodsBean) adapter.getItem(position);
        if (item != null) {
            CommodityDetailMallActivity.launch(mContext, item.id);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            if (params != null && !params.isEmpty()) {
                params.remove("start");
                params.put("start", String.valueOf(start));
                mPresenter.getCommodityList(start, params);
            }
        } else {
            mAdapter.loadMoreEnd();
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
