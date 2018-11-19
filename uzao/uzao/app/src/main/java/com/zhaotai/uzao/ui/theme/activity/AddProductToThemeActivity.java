package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.theme.adapter.MaterialTabAdapter;
import com.zhaotai.uzao.ui.theme.adapter.ProductAddToThemeListAdapter;
import com.zhaotai.uzao.ui.theme.contract.AddProductToThemeContract;
import com.zhaotai.uzao.ui.theme.presenter.AddProductToThemePresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 新增商品到主题列表
 */

public class AddProductToThemeActivity extends BaseActivity implements AddProductToThemeContract.View
        , BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_tab)
    RecyclerView mRecyclerTab;
    @BindView(R.id.recycler_content)
    RecyclerView mRecyclerContent;

    private AddProductToThemePresenter mPresenter;
    private ProductAddToThemeListAdapter mAdapter;
    private PageInfo<GoodsBean> data = new PageInfo<>();
    private List<CategoryBean> tabList;
    private String selectedCategoryCode;
    private MaterialTabAdapter mTabAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddProductToThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_to_theme_list);
        mTitle.setText("商品列表");
        mPresenter = new AddProductToThemePresenter(this, this);
        //tabList
        mRecyclerTab.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTabAdapter = new MaterialTabAdapter();
        mRecyclerTab.setAdapter(mTabAdapter);

        //内容列表
        mRecyclerContent.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProductAddToThemeListAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecyclerContent);
        mAdapter.setOnItemClickListener(this);
        mRecyclerContent.setAdapter(mAdapter);
        mRecyclerContent.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mAdapter.setEmptyView(R.layout.vw_empty);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mPresenter.getTabList();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @OnClick(R.id.iv_material_search)
    public void onSearch() {
        AddProductToThemeSearchActivity.launch(this);
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
        GoodsBean item = mAdapter.getItem(position);
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

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getProductCategoryList(start, false, selectedCategoryCode);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showTabList(List<CategoryBean> categoryBeen) {
        showContent();

        tabList = categoryBeen;
        mTabAdapter.setNewData(tabList);
        mTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < tabList.size(); i++) {
                    tabList.get(i).isChecked = position == i;
                }
                adapter.notifyDataSetChanged();
                selectedCategoryCode = tabList.get(position).categoryCode;
                mPresenter.getProductCategoryList(0, false, selectedCategoryCode);
            }
        });

        if (tabList != null && tabList.size() > 0) {
            //初始的请求列表
            selectedCategoryCode = tabList.get(0).categoryCode;
            //使用筛选过滤器 控制过滤参数
            mPresenter.getProductCategoryList(0, false, selectedCategoryCode);
            mTabAdapter.setselected(0);

        }
    }


    @Override
    public void showProductCategoryList(PageInfo<GoodsBean> list) {
        data = list;
        //下拉刷新
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.ADD_MODULE_TO_THEME:
                finish();
                break;
        }
    }
}




