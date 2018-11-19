package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.RecommendDesignProductAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.activity.DesignProductListActivity;
import com.zhaotai.uzao.ui.design.activity.EditorActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description : 素材支付结果
 */

public class MaterialSuccessPayResultActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.right_btn)
    Button mRightBtn;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private String price;
    private RecommendDesignProductAdapter mAdapter;
    private TextView mPrice;
    private MaterialDetailBean materialDetailBean;

    public static void launch(Context context, String price, MaterialDetailBean materialDetailBean) {
        Intent intent = new Intent(context, MaterialSuccessPayResultActivity.class);
        intent.putExtra("price", price);
        intent.putExtra("materialDetailBean", materialDetailBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_success_pay_result);
        mTitle.setText("支付结果");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("完成");

        mAdapter = new RecommendDesignProductAdapter();
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setAdapter(mAdapter);

        View headView = getLayoutInflater().inflate(R.layout.head_material_pay_success, null);
        mAdapter.addHeaderView(headView);
        mAdapter.setOnItemChildClickListener(this);
        mPrice = (TextView) headView.findViewById(R.id.tv_pay_price);

        headView.findViewById(R.id.tv_goto_design).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesignProductListActivity.launch(mContext, materialDetailBean);
            }
        });
    }

    @Override
    protected void initData() {
        price = getIntent().getStringExtra("price");
        mPrice.setText(price);
        materialDetailBean = (MaterialDetailBean) getIntent().getSerializableExtra("materialDetailBean");
        if (materialDetailBean != null) {
            Api.getDefault().getRecommendDesignProduct(materialDetailBean.sequenceNBR)
                    .compose(RxHandleResult.<List<GoodsBean>>handleResult())
                    .subscribe(new RxSubscriber<List<GoodsBean>>(mContext, true) {
                        @Override
                        public void _onNext(List<GoodsBean> goodsBean) {
                            mAdapter.addData(goodsBean);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        }

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        finish();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_go_to_design:
                final GoodsBean goodsBean = (GoodsBean) adapter.getItem(position);
                if (goodsBean != null) {
                    //调商品详情
                    Api.getDefault().getGoodsDetail(goodsBean.sequenceNBR, "all")
                            .compose(RxHandleResult.<GoodsDetailMallBean>handleResult())
                            .subscribe(new RxSubscriber<GoodsDetailMallBean>(mContext, false) {
                                @Override
                                public void _onNext(GoodsDetailMallBean goodsDetail) {
                                    if ("design".equals(goodsDetail.basicInfo.spuModel.spuType)) {
                                        EditorActivity.launch2DWhitMaterial(mContext, goodsDetail.basicInfo.mkuId, goodsBean.sequenceNBR, goodsDetail.basicInfo.isTemplate, materialDetailBean);
                                    } else {
                                        checkIsNeedSku(goodsBean.sequenceNBR, materialDetailBean, goodsDetail);
                                    }
                                }

                                @Override
                                public void _onError(String message) {
                                }
                            });
                }
                break;
        }
    }

    /**
     * @param spuId              商品id
     * @param materialDetailBean 素材详情
     * @param goodsDetail        商品详情
     */
    private void checkIsNeedSku(final String spuId, final MaterialDetailBean materialDetailBean, final GoodsDetailMallBean goodsDetail) {
        showLoadingDialog();
        //判断 sku是否大于1
        Api.getDefault().checkMkuCountIsSingle(spuId)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(mContext, false) {
                    @Override
                    public void _onNext(final Boolean s) {
                        if (s && goodsDetail.skus.size() > 0) {
                            getMkuId(goodsDetail.skus.get(0).sequenceNBR, spuId, materialDetailBean, goodsDetail);
                        } else {
                            disMisLoadingDialog();
                            EditorActivity.launch2DWhitMaterial(mContext, spuId, "N", goodsDetail, materialDetailBean);
                        }

                    }

                    @Override
                    public void _onError(String message) {
                        disMisLoadingDialog();
                    }
                });
    }

    /**
     * 获取mkuid
     */
    private void getMkuId(String skuId, final String spuId, final MaterialDetailBean materialDetailBean, final GoodsDetailMallBean goodsDetail) {
        Api.getDefault().getMkuId(skuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, false) {
                    @Override
                    public void _onNext(String mkuId) {
                        disMisLoadingDialog();
                        EditorActivity.launch2DWhitMaterial(mContext, mkuId, spuId, "N", materialDetailBean);

                    }

                    @Override
                    public void _onError(String message) {
                        disMisLoadingDialog();
                    }
                });
    }
}
