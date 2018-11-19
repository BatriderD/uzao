package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.productOrder.adapter.BatchAddressAdapter;
import com.zhaotai.uzao.ui.productOrder.adapter.EffectImageAdapter;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;
import com.zhaotai.uzao.ui.productOrder.presenter.ProductOrderDetailPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.view.NestGridView;
import com.zhaotai.uzao.view.NestListView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description : 打样大货 -- 待收尾款
 */

public class SamplingProduceWaitReceiveLastPayActivity extends BaseActivity implements ProductOrderContract.ProductOrderDetailView {

    //订单状态
    @BindView(R.id.tv_product_order_detail_state)
    TextView mOrderState;
    //分批地址
    @BindView(R.id.lv_product_order_detail_batch_address)
    NestListView mListView;
    //地址
    @BindView(R.id.tv_product_order_detail_receive_people)
    TextView mReceivePeople;
    @BindView(R.id.tv_product_order_detail_receive_phone)
    TextView mReceivePhone;
    @BindView(R.id.tv_product_order_detail_receive_address)
    TextView mReceiveAddress;
    //订单号
    @BindView(R.id.tv_product_order_detail_order_id_text)
    TextView mOrderType;
    @BindView(R.id.tv_product_order_detail_order_id)
    TextView mOrderNo;

    //创建时间
    @BindView(R.id.tv_product_order_detail_create_time)
    TextView mCreateTime;

    //样品确认时间
    @BindView(R.id.tv_product_order_detail_sampling_receive_time)
    TextView mSamplingReceiveTime;

    //大货样确认时间
    @BindView(R.id.tv_product_order_detail_produce_receive_time)
    TextView mProduceReceiveTime;

    //样品物流信息
    @BindView(R.id.tv_product_order_sampling_logistics)
    TextView mBtnSamplingLogistics;

    //大货样物流信息
    @BindView(R.id.tv_product_order_produce_logistics)
    TextView mBtnProduceLogistics;

    //订单金额
    @BindView(R.id.tv_product_order_detail_price)
    TextView mOrderPrice;

    //待付首款
    @BindView(R.id.tv_product_order_detail_first_pay)
    TextView mFirstPay;

    //待付尾款
    @BindView(R.id.tv_product_order_detail_wait_last_price)
    TextView mLastPay;

    //发票信息
    @BindView(R.id.tv_product_order_detail_bill_title)
    TextView mBillTitle;
    @BindView(R.id.tv_product_order_detail_bill_number)
    TextView mBillNo;
    @BindView(R.id.tv_product_order_detail_bill_content)
    TextView mBillContent;

    //效果图
    @BindView(R.id.ll_product_effect_image)
    LinearLayout mLlProductEffectImage;
    @BindView(R.id.gv_product_order_detail_grid_view)
    NestGridView mGridView;

    //生产信息
    @BindView(R.id.tv_product_order_detail_product_name)
    TextView mProductName;
    @BindView(R.id.tv_product_order_detail_product_type)
    TextView mProductType;
    @BindView(R.id.tv_product_order_detail_sample_num)
    TextView mSampleNum;
    @BindView(R.id.tv_product_order_detail_product_time_text)
    TextView mProductTimeName;
    @BindView(R.id.tv_product_order_detail_produce_num)
    TextView mProduceNum;
    @BindView(R.id.tv_product_order_detail_product_time)
    TextView mProductTime;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightBtn;
    private String orderId;

    @OnClick(R.id.tool_bar_right_img)
    public void goToIm() {
        mPresenter.goToIM();
    }

    private ProductOrderDetailPresenter mPresenter;
    private ProductOrderDetailBean data;

    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID = "extra_key_product_order_detail_order_id";

    /**
     * @param context 上下文
     * @param orderNo 订单id
     */
    public static void launch(Context context, String orderNo) {
        Intent intent = new Intent(context, SamplingProduceWaitReceiveLastPayActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID, orderNo);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_order_detail_sampling_produce_wait_receive_last_pay);
        mTitle.setText("订单详情");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.service);

    }

    @Override
    protected void initData() {
        mPresenter = new ProductOrderDetailPresenter(mContext, this);
        orderId = getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID);
        mPresenter.getProductOrderDetail(orderId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 支付明细
     */
    @OnClick(R.id.tv_product_order_pay_info)
    public void payInfo() {
        mPresenter.firstPayInfo(mContext,data);
    }

    /**
     * 尾款支付明细
     */
    @OnClick(R.id.tv_product_order_detail_wait_last_price)
    public void lastPayInfo() {
        mPresenter.lastPayInfo(mContext,data);
    }

    /**
     * 样品物流信息
     */
    @OnClick(R.id.tv_product_order_sampling_logistics)
    public void samplingLogistics() {
        mPresenter.sampleLogistics(mContext,data);
    }

    /**
     * 大货样物流信息
     */
    @OnClick(R.id.tv_product_order_produce_logistics)
    public void produceSamplingLogistics() {
        mPresenter.sampleProduceLogistics(mContext,data);
    }

    @Override
    public void showProductOrderDetail(ProductOrderDetailBean productOrderDetail) {
        data = productOrderDetail;
        mOrderState.setText("待收尾款");

        //分批地址
        if (productOrderDetail.batchAddress != null && productOrderDetail.batchAddress.size() > 0) {
            mListView.setFocusable(false);
            mListView.setAdapter(new BatchAddressAdapter(mContext, productOrderDetail.batchAddress, false));
        }

        //地址
        mReceivePeople.setText(productOrderDetail.receiverName);
        mReceivePhone.setText(productOrderDetail.receiverMobile);
        mReceiveAddress.setText(productOrderDetail.receiverAddress);

        //订单号
        mOrderType.setText("批量生产订单编号");

        mOrderNo.setText(getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID));

        //创建时间
        mCreateTime.setText(TimeUtils.millis2String(Long.parseLong(productOrderDetail.orderDates.submit)));

        //样品确认时间
        mSamplingReceiveTime.setText(TimeUtils.millis2String(Long.parseLong(productOrderDetail.orderDates.samplingConfirmReceive)));

        //大货样确认时间
        mProduceReceiveTime.setText(TimeUtils.millis2String(Long.parseLong(productOrderDetail.orderDates.produceConfirmReceive)));

        //订单金额
        String price ;
        if("collectFreight".equals(productOrderDetail.freightType)){
            price = getString(R.string.account,productOrderDetail.orderPrices.totalY);
        }else  {
            price = getString(R.string.account,productOrderDetail.orderPrices.totalY)
                    +getString(R.string.include_freight, productOrderDetail.orderPrices.freightY);
        }
        mOrderPrice.setText(price);

        //已付首款
        mFirstPay.setText(getString(R.string.account, productOrderDetail.orderPrices.firstPayY));
        //待付尾款
        mLastPay.setText(getString(R.string.account, productOrderDetail.orderPrices.lastPayY));

        //发票信息
        if (StringUtil.isEmpty(productOrderDetail.invoiceTitle)) {
            mBillTitle.setText("无");
            mBillNo.setText("无");
            mBillContent.setText("无");
        } else {
            mBillTitle.setText(productOrderDetail.invoiceTitle);
            mBillNo.setText(productOrderDetail.invoiceNumber);
            mBillContent.setText(productOrderDetail.invoiceContent);
        }

        //效果图
        if (productOrderDetail.produceInfos.produce.customDesignModel.effectImages != null) {
            mLlProductEffectImage.setVisibility(View.VISIBLE);
            mGridView.setFocusable(false);
            mGridView.setAdapter(new EffectImageAdapter(mContext, productOrderDetail.produceInfos.produce.customDesignModel.effectImages));
        } else {
            mLlProductEffectImage.setVisibility(View.GONE);
        }

        //生产信息
        //名字
        mProductName.setText(productOrderDetail.produceInfos.produce.customDesignModel.designName);
        //生产类型
        mProductType.setText("批量生产");
        //打样数量
        mSampleNum.setText(productOrderDetail.sampleCount + "个");
        //生产数量
        mProduceNum.setText(productOrderDetail.produceInfos.produce.produceCount + "个");
        //生产周期
        mProductTimeName.setText("生产周期");
        mProductTime.setText(productOrderDetail.produceInfos.productPeriodic + "天");
    }

    @Override
    public void finishView() {
        finish();
    }
}
