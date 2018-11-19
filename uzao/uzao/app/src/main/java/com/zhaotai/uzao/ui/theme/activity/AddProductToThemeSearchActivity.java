package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.ui.theme.adapter.ProductAddToThemeListAdapter;
import com.zhaotai.uzao.ui.theme.presenter.AddProductToThemeSearchPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description: 添加商品到主题搜索列表
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class AddProductToThemeSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private ProductAddToThemeListAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddProductToThemeSearchActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new ProductAddToThemeListAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new AddProductToThemeSearchPresenter(this, this);
    }

    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("spuName", searchWord);
        return params;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean item = mAdapter.getItem(position);
        //通知素材列表
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        contentModel.setEntityType("spu");
        contentModel.setEntityId(item.id);
        contentModel.setEntityName(item.spuName);
        contentModel.setEntityPic(item.pic);
        contentModel.setEntityPriceY(item.priceY);
        contentModel.setViewCounts(item.viewCount);
        contentModel.setBuyCounts(item.salesCount);
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        finish();
    }

}
