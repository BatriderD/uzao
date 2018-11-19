package com.zhaotai.uzao.ui.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.search.SearchCommonActivity;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description :  搜索主fragment
 */

public class SearchCommonMainFragment extends BaseFragment {

    private static final String MODEL_TYPE = "Type";
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    private ArrayList historyList;

    private int type = -1;
    private TagAdapter<String> tagAdapter;


    public static Fragment newInstance(int type) {
        SearchCommonMainFragment fragment = new SearchCommonMainFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(MODEL_TYPE, type);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.frag_search_main;
    }

    @Override
    public void initView() {


    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(MODEL_TYPE);
        initHistoryData();
        tagAdapter = new TagAdapter<String>(new ArrayList(historyList)) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_history_search,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                KeyboardUtils.hideSoftInput(getActivity());
                String item = (String) mFlowLayout.getAdapter().getItem(position);
                addSearchWord(item);
                SearchCommonActivity activity = (SearchCommonActivity) getActivity();
                activity.goSearchPage(item);
                return true;
            }
        });
        reFlashHistoryData();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 删除历史记录
     */
    @OnClick(R.id.iv_delete_history)
    public void onClickDeleteHistory() {
        historyList.clear();
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH, "");
        //其实就是刷新
        reFlashHistoryData();

    }

    /**
     * 刷新历史记录
     */
    private void reFlashHistoryData() {
        if (historyList == null && historyList.size() == 0) {
            mFlowLayout.setVisibility(View.GONE);
        } else {
            mFlowLayout.setVisibility(View.VISIBLE);
            tagAdapter = new TagAdapter<String>(new ArrayList(historyList)) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_history_search,
                            mFlowLayout, false);
                    tv.setText(s);
                    return tv;
                }
            };
            mFlowLayout.setAdapter(tagAdapter);
        }

    }

    /**
     * 增加搜索字段
     *
     * @param searchWord 搜索文字
     */
    public void addSearchWord(String searchWord) {
        int oldPosition = -1;
        for (int i = 0; i < historyList.size(); i++) {
            if (searchWord.equals(historyList.get(i))) {
                oldPosition = i;
                break;
            }
        }
        if (oldPosition >= 0) {
            historyList.remove(oldPosition);
        }
        historyList.add(0, searchWord);
        if (historyList.size() > 20) {
            historyList.remove(20);
        }
        String historySearch = GsonUtil.t2Json2(historyList);
        SPUtils.setSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH, historySearch);
        reFlashHistoryData();
    }

    /**
     * 初始化当前类型的历史记录字段
     */
    private void initHistoryData() {
        String historySearch = "";
        switch (type) {
            case 1:
                historySearch = SPUtils.getSharedStringData(GlobalVariable.HISTORY_CATCH_WORD_SEARCH);
                break;
            case 2:
                historySearch = SPUtils.getSharedStringData(GlobalVariable.HISTORY_MY_MATERIAL_SEARCH);
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
    }
}

