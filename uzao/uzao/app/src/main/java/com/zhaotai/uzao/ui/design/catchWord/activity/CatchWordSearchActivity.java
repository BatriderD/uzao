package com.zhaotai.uzao.ui.design.catchWord.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.design.catchWord.contract.CatchWordSearchContract;
import com.zhaotai.uzao.ui.design.catchWord.presenter.CatchWordSearchPresenter;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 搜索页面
 */

public class CatchWordSearchActivity extends BaseActivity implements CatchWordSearchContract.View, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, TextView.OnEditorActionListener {

    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    @BindView(R.id.etd_text)
    EditWithDelView mSearchText;

    private ArrayList historyList;

    private CatchWordSearchPresenter mPresenter;
    private String searchWord;
    private Map<String, String> params;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, CatchWordSearchActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_catch_word);

        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });
        mSearchText.setOnEditorActionListener(this);

        showHistory();
    }

    /**
     * 显示历史记录
     */
    public void showHistory() {
        int type = getIntent().getIntExtra("type", -1);
        String historySearch = "";
        switch (type) {
            case 1:
                historySearch = SPUtils.getSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH);
                break;
        }
        //历史搜索
        try {
            historyList = GsonUtil.getGson().fromJson(historySearch, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            Log.d("搜索", "搜索历史为空");
        }

        if (historyList == null) historyList = new ArrayList<>();
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
                    KeyboardUtils.hideSoftInput(CatchWordSearchActivity.this);
                    String item = (String) mFlowLayout.getAdapter().getItem(position);
                    goSearchPage(item);
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
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH, "");
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
        this.searchWord = mSearchText.getText().toString();
        if (StringUtil.isTrimEmpty(searchWord)) {
            ToastUtil.showShort("请输入关键字");
            return;
        }
        KeyboardUtils.hideSoftInput(this);

        refrashData();
        goSearchPage(searchWord);
    }

    //根据类型跳转到相应页面
    private void goSearchPage(String keyWord) {

        int type = getIntent().getIntExtra("type", -1);

        switch (type) {
            case 1:
                SearchResultCatchWordListActivity.launch(mContext, keyWord);
                break;
        }
    }

    /**
     * 刷新数据
     */
    private void refrashData() {
        int oldPosition = -1;
        for (int i = 0; i < historyList.size(); i++) {
            if (this.searchWord.equals(historyList.get(i))) {
                oldPosition = i;
            }
        }
        if (oldPosition >= 0) {
            historyList.remove(oldPosition);
        }
        historyList.add(0, this.searchWord);
        if (historyList.size() > 20) {
            historyList.remove(20);
        }
        String historySearch = GsonUtil.t2Json2(historyList);
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH, historySearch);
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
            mPresenter = new CatchWordSearchPresenter(this, this);
        }
    }


    @Override
    public void stopLoadingMore() {

    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {

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
