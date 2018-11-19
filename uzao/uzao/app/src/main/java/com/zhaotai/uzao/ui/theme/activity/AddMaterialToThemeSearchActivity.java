package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.ui.theme.adapter.MaterialAddToThemeListAdapter;
import com.zhaotai.uzao.ui.theme.presenter.AddThemeToThemeSearchPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description: 新增素材到主题的搜索页面
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class AddMaterialToThemeSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private MaterialAddToThemeListAdapter mAdapter;

    public static void launch(Context context, String categoryCode1) {
        Intent intent = new Intent(context, AddMaterialToThemeSearchActivity.class);
        intent.putExtra("categoryCode1", categoryCode1);
        context.startActivity(intent);
    }

    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new MaterialAddToThemeListAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new AddThemeToThemeSearchPresenter(this, this);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
    }
    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("categoryCode1", getIntent().getStringExtra("categoryCode1"));
        params.put("materialName", searchWord);
        return params;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialListBean item = mAdapter.getItem(position);
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        contentModel.setEntityType("material");
        contentModel.setEntityId(item.id);
        contentModel.setEntityName(item.materialName);
        contentModel.setEntityPic(item.pic);
        contentModel.setEntityPriceY(item.priceY);
        contentModel.setBuyCounts(item.salesCount);
        contentModel.setViewCounts(item.viewCount);
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        finish();
    }
}
