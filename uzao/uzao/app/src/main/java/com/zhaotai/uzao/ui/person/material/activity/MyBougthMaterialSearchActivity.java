package com.zhaotai.uzao.ui.person.material.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.material.activity.MaterialCategoryActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.material.adapter.MyMaterialAdapter;
import com.zhaotai.uzao.ui.person.material.contract.BoughtSearchSearchView;
import com.zhaotai.uzao.ui.person.material.presenter.MyMaterialBoughtSearchPresenter;
import com.zhaotai.uzao.ui.search.SimpleBaseSearchActivity;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.decoration.GridSpacingItemDecoration;

import java.util.HashMap;

/**
 * description: 添加商品到主题搜索列表
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class MyBougthMaterialSearchActivity extends SimpleBaseSearchActivity implements BaseQuickAdapter.OnItemClickListener, BoughtSearchSearchView {

    private MyMaterialAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyBougthMaterialSearchActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected BaseQuickAdapter setAdapter() {
        mAdapter = new MyMaterialAdapter();
        mAdapter = new MyMaterialAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                MyMaterialBean myMaterialBean = mAdapter.getData().get(position);
                switch (id) {
                    case R.id.tv_material_del:
                        //删除素材
                        if (mPresenter instanceof MyMaterialBoughtSearchPresenter) {
                            MyMaterialBoughtSearchPresenter presenter = (MyMaterialBoughtSearchPresenter) mPresenter;
                            presenter.delMaterial(myMaterialBean.getSequenceNBR(), position);
                        }
                        break;
                    case R.id.tv_material_buy_again:
                        //重新购买 跳转页面
                        MaterialDetailActivity.launch(MyBougthMaterialSearchActivity.this, myMaterialBean.getSourceMaterialId());
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyStateView(mContext, R.mipmap.ic_state_empty_1, "还没有任何素材，快快去购买哦", getString(R.string.empty_btn), new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                MaterialCategoryActivity.launch(mContext);
            }
        });
        return mAdapter;
    }

    @Override
    protected SimpleBaseSearchPresenter setPresenter() {
        return new MyMaterialBoughtSearchPresenter(this, this);
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
        params.put("name", searchWord);
        return params;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(2, 10, false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MaterialDetailActivity.launch(this, mAdapter.getData().get(position).getSourceMaterialId());
    }

    @Override
    public void showDelSuccess(int position) {
        mAdapter.remove(position);
        ToastUtil.showShort("删除成功");
    }
}
