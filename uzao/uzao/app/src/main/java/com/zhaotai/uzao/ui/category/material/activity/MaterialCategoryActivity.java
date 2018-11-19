package com.zhaotai.uzao.ui.category.material.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MultiMaterialCategoryBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.material.adapter.MaterialCategoryAdapter;
import com.zhaotai.uzao.ui.category.material.contract.MaterialCategoryContract;
import com.zhaotai.uzao.ui.category.material.presenter.MaterialCategoryPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材分类
 */

public class MaterialCategoryActivity extends BaseActivity implements MaterialCategoryContract.View, OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MaterialCategoryPresenter mPresenter;
    private MaterialCategoryAdapter mAdapter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MaterialCategoryActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_category);
        mTitle.setText("素材分类");
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 6));
        mPresenter = new MaterialCategoryPresenter(mContext, this);
    }

    @Override
    protected void initData() {
        autoRefresh();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 自动刷新
     */
    @Override
    public void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getData();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    /**
     * 绑定数据
     *
     * @param data 多布局数据
     */
    @Override
    public void bindData(final List<MultiMaterialCategoryBean> data) {
        if (mAdapter == null) {
            mAdapter = new MaterialCategoryAdapter(data);
            mAdapter.setOnItemChildClickListener(this);
            mRecycler.setAdapter(mAdapter);
            mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    switch (data.get(position).getItemType()) {
                        case MultiMaterialCategoryBean.TYPE_CATEGORY:
                            return 2;
                        case MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL:
                            return 3;
                        default:
                            return 6;
                    }
                }
            });
        } else {
            mAdapter.setNewData(data);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        MultiMaterialCategoryBean item = (MultiMaterialCategoryBean) adapter.getItem(position);
        if (item == null) return;
        switch (view.getId()) {
            case R.id.iv_main_custom_category_pic://分类
                MaterialCategoryListActivity.launch(mContext, (ArrayList<CategoryBean>) item.children, item.categoryName);
                break;
            case R.id.iv_main_custom_recommend_spu_pic://素材
                MaterialDetailActivity.launch(mContext, item.sequenceNBR);
                break;
        }
    }
}
