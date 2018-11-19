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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.category.material.adapter.MaterialListAdapter;
import com.zhaotai.uzao.ui.search.adapter.AssociateAdapter;
import com.zhaotai.uzao.ui.search.contract.MaterialAndSearchContract;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.ui.search.presenter.MaterialAndSearchPresenter;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 搜索页面 -- 素材
 */

public class MaterialAndSearchActivity extends BaseFragmentActivity implements MaterialAndSearchContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, TextView.OnEditorActionListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.etd_text)
    EditWithDelView mSearchText;

    //搜索
    @BindView(R.id.ll_search_content)
    LinearLayout mSearchContent;

    //结果页面
    @BindView(R.id.ll_product_content)
    LinearLayout mProductContent;

    //联想
    @BindView(R.id.recycler_relate)
    RecyclerView mRecyclerRelate;

    @BindView(R.id.rl_product_list)
    RecyclerView mRecycler;

    @BindView(R.id.tv_material_tab_sort_comprehensive)
    TextView mTextComprehensive;
    @BindView(R.id.tv_material_tab_sort_popular)
    TextView mTabPopular;
    @BindView(R.id.tv_material_tab_sort_sale)
    TextView mTabSale;
    @BindView(R.id.tv_material_tab_sort_time)
    TextView mTabTime;

    private MaterialAndSearchPresenter mPresenter;
    private MaterialListAdapter mAdapter;
    private PageInfo<MaterialListBean> data;
    private String searchWord;
    private int tabPosition = 1;
    private String sort = "default_";

    private Map<String, String> params = new HashMap<>();
    private AssociateAdapter associateAdapter;
    private FilterFragment mFilterFragment;

    //    private String historySearch;//历史搜索记录
    private List<String> historyList;
    private static final String TAG = "MaterialAndSearchActivity";
    private String type;
    private String categoryCode;
    private boolean isNeedAssociate = true; //记录是否需要联想搜索

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MaterialAndSearchActivity.class));
    }

    public static void launch(Context context, String type, String categoryCode) {
        Intent intent = new Intent(context, MaterialAndSearchActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("categoryCode", categoryCode);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_search);

//        mSearchText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSearchContent.setVisibility(View.VISIBLE);
//                mProductContent.setVisibility(View.GONE);
//                mRecyclerRelate.setVisibility(View.GONE);
//                showHistory();
//            }
//        });
        mSearchText.setOnEditorActionListener(this);

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
                    setSearchText(associateBean.word);
                    changeTab(1);
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "Y");
                    params.put("sort", "default_");
                    params.put("categoryCode1", categoryCode);
                    params.put("queryWord", associateBean.word);
                    mPresenter.getMaterialList(0, params);
                    saveHistory(associateBean.word);
                }
            }
        });

//        showHistory();

        //取消侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
                params.put("categoryCode1", categoryCode);
                params.put("queryWord", searchWord);
                mPresenter.getMaterialList(0, params);
            }

            @Override
            public void reset() {
                params.clear();
                params.put("start", "0");
                params.put("needOption", "Y");
                params.put("sort", sort);
                params.put("categoryCode1", categoryCode);
                params.put("queryWord", searchWord);
                mPresenter.getMaterialList(0, params);
            }

            @Override
            public void closeDrawer() {
                KeyboardUtils.hideSoftInput(MaterialAndSearchActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new MaterialAndSearchPresenter(this, this);
            type = getIntent().getStringExtra("type");
            categoryCode = getIntent().getStringExtra("categoryCode");
            if (categoryCode == null) {
                categoryCode = "";
            }
            params.put("categoryCode1", categoryCode);
        }
        doSearch();
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
        //历史搜索HISTORY_SEARCH
        String historySearch = SPUtils.getSharedStringData(GlobalVariable.HISTORY_SEARCH_MATERIAL);
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
                    KeyboardUtils.hideSoftInput(MaterialAndSearchActivity.this);
                    setSearchText(historyList.get(position));
                    changeTab(1);
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "Y");
                    params.put("sort", "default_");
                    params.put("categoryCode1", categoryCode);
                    params.put("queryWord", historyList.get(position));
                    searchWord = historyList.get(position);
                    mPresenter.getMaterialList(0, params);
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
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_SEARCH_MATERIAL, "");
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

    private void changeTab(int position) {
        switch (position) {
            case 1:
                if (tabPosition != 1) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTabPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 2:
                if (tabPosition != 2) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabPopular.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTabSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 3:
                if (tabPosition != 3) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabSale.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTabTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
                break;
            case 4:
                if (tabPosition != 4) {
                    mTextComprehensive.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabPopular.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabSale.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    mTabTime.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                }
                break;
        }
        tabPosition = position;
    }

    /**
     * 综合排序
     */
    @OnClick(R.id.tv_material_tab_sort_comprehensive)
    public void onClickCoprehensive() {
        changeTab(1);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "default_");
        params.put("categoryCode1", categoryCode);
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "default_";
    }

    /**
     * 人气
     */
    @OnClick(R.id.tv_material_tab_sort_popular)
    public void onClickPopular() {
        changeTab(2);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "popular-desc");
        params.put("categoryCode1", categoryCode);
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "popular-desc";
    }

    /**
     * 销量
     */
    @OnClick(R.id.tv_material_tab_sort_sale)
    public void onClickSales() {
        changeTab(3);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "salesCount-desc");
        params.put("categoryCode1", categoryCode);
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "salesCount-desc";
    }

    /**
     * 时间
     */
    @OnClick(R.id.tv_material_tab_sort_time)
    public void onClickTime() {
        changeTab(4);
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("sort", "upDate-desc");
        params.put("categoryCode1", categoryCode);
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
        sort = "upDate-desc";
    }

    /**
     * 筛选
     */
    @OnClick(R.id.tv_material_tab_sort_filter)
    public void onCLickFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    /**
     * 搜索
     */
    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        if (StringUtil.isTrimEmpty(mSearchText.getText().toString())) {
            ToastUtil.showShort("请输入素材名");
            return;
        }
        KeyboardUtils.hideSoftInput(this);
        doSearch();
        saveHistory(searchWord);
    }

    private void doSearch() {
        searchWord = mSearchText.getText().toString();
        changeTab(1);
        params.clear();
        params.put("start", "0");
        params.put("needOption", "Y");
        params.put("sort", "default_");
        params.put("categoryCode1", categoryCode);
        params.put("queryWord", searchWord);
        mPresenter.getMaterialList(0, params);
    }

    public void saveHistory(String word) {
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
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_SEARCH_MATERIAL, historySearch);
    }


    @Override
    public void showMaterialList(PageInfo<MaterialListBean> list) {
        if (mAdapter == null) {
            mAdapter = new MaterialListAdapter();
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnLoadMoreListener(this, mRecycler);
            mAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty, getString(R.string.empty_search));
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
            mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
            mRecycler.setAdapter(mAdapter);
        }

        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.GONE);
        mProductContent.setVisibility(View.VISIBLE);
        mFilterFragment.setBottomOKText("确定(" + list.list.size() + "个素材)");
        this.data = list;
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
     *
     */
    @Override
    public void showAssociateList(List<AssociateBean> associateBeen) {
        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.VISIBLE);
        mProductContent.setVisibility(View.GONE);
        associateAdapter.setNewData(associateBeen);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialListBean item = (MaterialListBean) adapter.getItem(position);
        if ("editor".equals(type)) {
            if (item.data != null) {
                AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
                addEditorMaterialBean.thumbnail = item.pic;
                addEditorMaterialBean.sourceMaterialId = item.id;
                addEditorMaterialBean.fileMime = item.data.fileMime;
                addEditorMaterialBean.resizeScale = item.data.resizeScale;
                EventBus.getDefault().post(addEditorMaterialBean);
            } else {
                ToastUtil.showShort("素材信息错误");
            }
            finish();

        } else {
            if (item != null)
                MaterialDetailActivity.launch(mContext, item.id);
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
                mPresenter.getMaterialList(start, params);
            }
        } else {
            if (mAdapter != null)
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
