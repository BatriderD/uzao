package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.bean.ThemeListBean;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.ui.theme.adapter.NewThemeListAdapter;
import com.zhaotai.uzao.ui.theme.presenter.ThemeListSearchPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.HashMap;

/**
 * description ：添加收藏的素材到主题设置界面
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class ThemeListSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private NewThemeListAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ThemeListSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new NewThemeListAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    public boolean doDefaultSearch() {
        return true;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new ThemeListSearchPresenter(this, this);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("queryWord", searchWord);
        return params;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ThemeListBean item = (ThemeListBean) adapter.getItem(position);
        ThemeDetailActivity.launch(this, item.getId());
    }
}
