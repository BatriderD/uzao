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
import com.zhaotai.uzao.ui.theme.adapter.CollectionProductToThemeAdapter;
import com.zhaotai.uzao.ui.theme.presenter.AddCollectionProductThemeSearchPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description ：添加收藏的素材到主题设置界面
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class AddCollectionProductToThemeSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private CollectionProductToThemeAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddCollectionProductToThemeSearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 设置adapter
     * @return adapter
     */
    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new CollectionProductToThemeAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    /**
     * 设置presenter
     * @return presenter
     */
    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new AddCollectionProductThemeSearchPresenter(this, this);
    }

    /**
     * 创建搜索参数
     * @param searchWord 搜索的关键字
     * @return 搜索参数
     */
    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("spuName", searchWord);
        return params;
    }

    /**
     * 分割线
     * @return 分割线控制类
     */
    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
    }

    /**
     * 点击事件
     * @param adapter adapter
     * @param view 点击的view
     * @param position 下标
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsBean item = mAdapter.getItem(position);
        //添加商品到主题模块
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        contentModel.setEntityType("spu");
        contentModel.setEntityId(item.spuId);
        contentModel.setEntityName(item.spuName);
        contentModel.setEntityPic(item.thumbnail);
        contentModel.setEntityPriceY(item.priceY);
        contentModel.setBuyCounts("0");
        contentModel.setViewCounts("0");
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        finish();
    }
}
