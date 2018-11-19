package com.zhaotai.uzao.ui.search;

import android.content.Context;
import android.content.Intent;
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
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AssociateBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;
import com.zhaotai.uzao.ui.search.adapter.AssociateAdapter;
import com.zhaotai.uzao.ui.search.adapter.ProductListAdapter;
import com.zhaotai.uzao.ui.search.contract.DesignProductAndSearchContract;
import com.zhaotai.uzao.ui.search.presenter.DesignProductAndSearchPresenter;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;
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
 * Description : 搜索页面 -- 可定制商品
 */

public class DesignProductAndSearchActivity extends BaseActivity implements DesignProductAndSearchContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, TextView.OnEditorActionListener {

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

    private DesignProductAndSearchPresenter mPresenter;
    private ProductListAdapter mAdapter;
    private PageInfo<GoodsBean> data;
    private String searchWord;

    private Map<String, String> params = new HashMap<>();

    private AssociateAdapter associateAdapter;

    //    private String historySearch;//历史搜索记录
    private List<String> historyList;
    private static final String TAG = "DesignProductAndSearchActivity";
    private MaterialDetailBean materialDetailBean;

    public static void launch(Context context, MaterialDetailBean materialDetailBean) {
        Intent intent = new Intent(context, DesignProductAndSearchActivity.class);
        intent.putExtra("materialDetailBean", materialDetailBean);
        context.startActivity(intent);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, DesignProductAndSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_search);
        mAdapter = new ProductListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);

        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchContent.setVisibility(View.VISIBLE);
                mProductContent.setVisibility(View.GONE);
                mRecyclerRelate.setVisibility(View.GONE);
                showHistory();
            }
        });
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
                } else {
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
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "N");
                    params.put("queryWord", associateBean.word);
                    mPresenter.getDesignProductList(0, params);
                    saveHistory(associateBean.word);
                }
            }
        });

        showHistory();
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
                    KeyboardUtils.hideSoftInput(DesignProductAndSearchActivity.this);
                    mSearchText.setText(historyList.get(position));
                    mSearchText.setSelection(historyList.get(position).length());
                    params.clear();
                    params.put("start", "0");
                    params.put("needOption", "N");
                    params.put("queryWord", historyList.get(position));
                    searchWord = historyList.get(position);
                    mPresenter.getDesignProductList(0, params);
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
            ToastUtil.showShort("请输入素材名");
            return;
        }
        KeyboardUtils.hideSoftInput(this);

        searchWord = mSearchText.getText().toString();
        params.clear();
        params.put("start", "0");
        params.put("needOption", "N");
        params.put("queryWord", searchWord);
        mPresenter.getDesignProductList(0, params);
        saveHistory(searchWord);
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
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_SEARCH, historySearch);
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
            mPresenter = new DesignProductAndSearchPresenter(this, this);
            materialDetailBean = (MaterialDetailBean) getIntent().getSerializableExtra("materialDetailBean");
        }
    }

    @Override
    public void showDesignProductList(PageInfo<GoodsBean> list) {
        mSearchContent.setVisibility(View.GONE);
        mRecyclerRelate.setVisibility(View.GONE);
        mProductContent.setVisibility(View.VISIBLE);
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
    public void showProgress() {
        showLoadingDialog();
    }

    @Override
    public void stopProgress() {
        disMisLoadingDialog();
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
        if (goodsBean != null) {
            if (materialDetailBean == null) {
                CommodityDetailMallActivity.launch(mContext, goodsBean.id);
            } else {
                if (goodsBean.spuType.equals("design")) {
                    EditorActivity.launch2DWhitMaterial(mContext, goodsBean.mkuId, goodsBean.id, "N", materialDetailBean);
                } else {
                    mPresenter.checkIsNeedSku(goodsBean.id, materialDetailBean, goodsBean.customizeType);
                }
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            params.clear();
            params.put("start", String.valueOf(start));
            params.put("needOption", "N");
            params.put("queryWord", searchWord);
            mPresenter.getDesignProductList(start, params);
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
