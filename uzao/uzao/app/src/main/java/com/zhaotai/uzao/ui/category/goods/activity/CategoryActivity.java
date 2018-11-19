package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.CategoryLeftAdapter;
import com.zhaotai.uzao.adapter.CategoryRightAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.ui.category.goods.contract.CateGoryContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CateGoryPresenter;
import com.zhaotai.uzao.ui.search.CommodityAndSearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/5
 * Created by zp
 * Description : 分类列表页面
 */

public class CategoryActivity extends BaseActivity implements CateGoryContract.View {
    @BindView(R.id.category_left_recycler)
    RecyclerView leftRecycler;

    @BindView(R.id.category_right_recycler)
    RecyclerView rightRecycler;

    @BindView(R.id.tv_category_search)
    TextView mHint;

    private CategoryLeftAdapter mLeftAdapter;
    private CategoryRightAdapter mRightAdapter;
    //一级分类
    private List<CategoryBean> mLeftList;
    //记录上一个点击的位置
    private int pressIndex = 0;
    private CateGoryPresenter mPresenter;
    private int pos;

    private static final String EXTRA_KEY_CATEGORY_POSITION = "extra_key_category_position";

    /**
     * 分类页面
     */
    public static void launch(Context context, int pos) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_KEY_CATEGORY_POSITION, pos);
        context.startActivity(intent);
    }

    /**
     * 跳转搜索页面
     */
    @OnClick(R.id.tv_category_search)
    public void goToSearch() {
        CommodityAndSearchActivity.launch(this);
    }

    /**
     * 关闭页面
     */
    @OnClick(R.id.tool_back)
    public void onClickBack() {
        finish();
    }

    @Override
    public void initView() {
        setContentView(R.layout.frag_category);
        //左边列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //数据集合
        mLeftList = new ArrayList<>();
        leftRecycler.setLayoutManager(linearLayoutManager);
        //mLeftAdapter = new CategoryLeftAdapter(mLeftList);
        leftRecycler.setAdapter(mLeftAdapter);
        //右边列表
        GridLayoutManager mGridManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rightRecycler.setLayoutManager(mGridManager);
        mRightAdapter = new CategoryRightAdapter();
        rightRecycler.setAdapter(mRightAdapter);

        mRightAdapter.addHeaderView(View.inflate(this, R.layout.category_header, null));
        mHint.setText(getString(R.string.search_text));
        //一级类目条目点击
        mLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (pressIndex == position) {
                    return;
                } else {
                    mLeftList.get(pressIndex).isChecked = false;
                    mLeftList.get(position).isChecked = true;
                }
                //记录当前点击位置
                pressIndex = position;

                mLeftAdapter.notifyDataSetChanged();
                List<CategoryBean> data = mLeftList.get(position).rightChildren;
                if (data != null && data.size() > 0) {

                    mRightAdapter.setNewData(data);
                } else {
                    //切换二级分类
                    mPresenter.getRightList(mLeftList.get(position).categoryCode, position);
                }
            }
        });
        //二级类目条目点击
        mRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean info = (CategoryBean) adapter.getItem(position);
//                if (info != null)
                    //CategoryListActivity.launch(CategoryActivity.this, info.categoryCode, info.categoryName, info.parentCode);
            }
        });

        mPresenter = new CateGoryPresenter(this, this);
    }


    @Override
    public void initData() {
        pos = getIntent().getIntExtra(EXTRA_KEY_CATEGORY_POSITION, 0);
//        不强制更新左侧列表
        mPresenter.getLeftList(false);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    public void showLeftList(List<CategoryBean> data) {
        if (data.size() > 0) {
            mLeftList = data.get(0).children;
            //循环遍历  第一个被选择
            mLeftList.get(pos).isChecked = true;
            //mLeftAdapter.addData(mLeftList);
            if (mLeftList.size() >= pos) {
                mPresenter.getRightList(mLeftList.get(pos).categoryCode, pos);
                leftRecycler.smoothScrollToPosition(pos);
                pressIndex = pos;
            } else {
                mPresenter.getRightList(mLeftList.get(0).categoryCode, 0);
                leftRecycler.smoothScrollToPosition(0);
                pressIndex = 0;
            }
        }
    }

    @Override
    public void showRightList(List<CategoryBean> data, int position) {
        if (data.size() > 0) {
            mLeftList.get(position).rightChildren = data;
            mRightAdapter.setNewData(data);
        }
    }

}
