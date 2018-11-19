package com.zhaotai.uzao.ui.design.catchWord.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.design.catchWord.adapter.CatchContentAdapter;
import com.zhaotai.uzao.ui.design.catchWord.contract.SearchResultCatchWordContract;
import com.zhaotai.uzao.ui.design.catchWord.presenter.SearchResultCatchWordPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description:流行词列表页
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class SearchResultCatchWordListActivity extends BaseActivity implements SearchResultCatchWordContract.View, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rc_catch_word)
    public RecyclerView rc_content;

    @BindView(R.id.etd_text)
    public EditText etd_text;

    private SearchResultCatchWordPresenter mPresenter;
    private CatchContentAdapter contentAdapter;
    private PageInfo<CatchWordBean> data;
    private String keyWord;

    public static void launch(Context context, String keyWord) {
        Intent intent = new Intent(context, SearchResultCatchWordListActivity.class);
        intent.putExtra("keyWord", keyWord);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_result_catch_word);


        rc_content.setLayoutManager(new GridLayoutManager(mContext, 2));
        contentAdapter = new CatchContentAdapter();
        contentAdapter.setOnLoadMoreListener(this, rc_content);
        rc_content.setAdapter(contentAdapter);
        contentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mPresenter = new SearchResultCatchWordPresenter(this, this);
    }

    @Override
    protected void initData() {
        keyWord = getIntent().getStringExtra("keyWord");
        etd_text.setText(keyWord);
        etd_text.setSelection(keyWord.length());
        mPresenter.getSearchList(keyWord, 0);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showSearchList(PageInfo<CatchWordBean> catchWordBeanPageInfo) {
        data = catchWordBeanPageInfo;
        if (catchWordBeanPageInfo.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            List<CatchWordBean> list = catchWordBeanPageInfo.list;
            contentAdapter.setNewData(list);
        } else {
//            不是第一页 就刷新
            contentAdapter.addData(catchWordBeanPageInfo.list);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            mPresenter.getSearchList(keyWord, start);
            //加载列表数据
        } else {
            contentAdapter.loadMoreEnd();
        }
    }


    @OnClick({R.id.tool_back, R.id.etd_text})
    public void goBack() {
        finish();
    }

    @OnClick(R.id.tv_tool_bar_search)
    public void doSearch() {
        String word = etd_text.getText().toString();
        mPresenter.getSearchList(word, 0);
    }
}
