package com.zhaotai.uzao.ui.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.ui.shopping.adapter.SimilarProductAdapter;
import com.zhaotai.uzao.ui.shopping.contract.SimilarProductContract;
import com.zhaotai.uzao.ui.shopping.presenter.SimilarProductPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/11/22
 * Created by LiYou
 * Description : 相似商品
 */

public class SimilarProductActivity extends BaseActivity implements SimilarProductContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.right_btn)
    Button mRight;

    private SimilarProductAdapter mAdapter;
    private SimilarProductPresenter mPresenter;

    private static final String EXTRA_KEY_SPU_ID = "extra_key_spu_id";
    private String spuId;

    public static void launch(Context context, String spuId) {
        Intent intent = new Intent(context, SimilarProductActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_similar_product);
        mTitle.setText("相似商品");
        mAdapter = new SimilarProductAdapter();
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new SimilarProductPresenter(this, this);
        }
        spuId = getIntent().getStringExtra(EXTRA_KEY_SPU_ID);

        showLoading();
        mPresenter.getSimilarProduct(true, spuId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        mPresenter.getSimilarProduct(false, spuId);
    }

    @Override
    public void showSimilarProductList(List<GoodsBean> data) {
        mAdapter.setNewData(data);
    }

}
