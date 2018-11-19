package com.zhaotai.uzao.ui.person.material.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.material.adapter.MyUploadMaterialAdapter;
import com.zhaotai.uzao.ui.person.material.presenter.MyMaterialUploadSearchPresenter;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import java.util.HashMap;

/**
 * description: 添加商品到主题搜索列表
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MyUpLoadMaterialSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private BaseRecyclerAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyUpLoadMaterialSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new MyUploadMaterialAdapter();
        mAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty_1, "还没有上传任何素材，快快去设计吧");
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new MyMaterialUploadSearchPresenter(this, this);
    }

    @Override
    public boolean doDefaultSearch() {
        return true;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("materialName", searchWord);
        return params;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(2, 10, false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MyUploadMaterialBean item = (MyUploadMaterialBean) mAdapter.getItem(position);
        MaterialDetailActivity.launch(this, item.sequenceNBR);

    }

}
