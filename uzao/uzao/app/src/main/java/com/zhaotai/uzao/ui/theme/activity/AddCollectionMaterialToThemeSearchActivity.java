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
import com.zhaotai.uzao.ui.theme.adapter.CollectionMaterialToThemeAdapter;
import com.zhaotai.uzao.ui.theme.presenter.AddCollectionMaterialThemeSearchPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * description ：添加收藏的素材到主题设置界面
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class AddCollectionMaterialToThemeSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener {

    private CollectionMaterialToThemeAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddCollectionMaterialToThemeSearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 设置adapter
     * @return adapter
     */
    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new CollectionMaterialToThemeAdapter();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new AddCollectionMaterialThemeSearchPresenter(this, this);
    }

    /**
     * 设置搜索搜索关键词
     * @param searchWord 搜索的关键字
     * @return 搜索字段HashMap
     */
    @Override
    protected HashMap<String, String> goSearch(String searchWord) {
        params.put("materialName", searchWord);
        return params;
    }


    /**
     * 设置分割线
     * @return 分割线控制类
     */
    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
    }

    /**
     * item 点击事件
     * @param adapter adapter
     * @param view 点击的view
     * @param position 位置
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialListBean item = mAdapter.getItem(position);
        //添加素材到主题模块
        ThemeModuleBean.ThemeContentModel contentModel = new ThemeModuleBean.ThemeContentModel();
        contentModel.setEntityType("material");
        contentModel.setEntityId(item.materialId);
        contentModel.setEntityName(item.sourceMaterialName);
        contentModel.setEntityPic(item.thumbnail);
        contentModel.setEntityPriceY(item.priceY);
        contentModel.setBuyCounts(item.salesCount);
        contentModel.setViewCounts(item.viewCount);
        EventBus.getDefault().post(new EventBean<>(contentModel, EventBusEvent.ADD_MODULE_TO_THEME));
        finish();
    }
}
