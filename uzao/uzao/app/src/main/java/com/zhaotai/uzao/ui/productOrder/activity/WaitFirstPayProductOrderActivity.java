package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.ui.productOrder.adapter.EffectImageAdapter;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;
import com.zhaotai.uzao.ui.productOrder.presenter.ProductOrderDetailPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.view.NestGridView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description :  待付收款订单详情页
 */

public class WaitFirstPayProductOrderActivity extends BaseActivity implements ProductOrderContract.ProductOrderDetailView {
    //订单状态
    @BindView(R.id.tv_product_order_detail_state)
    TextView mOrderState;
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

    //订单金额
    @BindView(R.id.tv_product_order_detail_price)
    TextView mOrderPrice;

    //待付首款
    @BindView(R.id.rl_product_order_detail_first_pay)
    RelativeLayout mRlFirst_pay;
    @BindView(R.id.tv_product_order_detail_first_pay)
    TextView mFirstPay;

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
    @BindView(R.id.rl_product_order_detail_sample_num)
    RelativeLayout mRlSampleNum;
    @BindView(R.id.tv_product_order_detail_sample_num)
    TextView mSampleNum;
    @BindView(R.id.tv_product_order_detail_produce_num)
    TextView mProduceNum;
    @BindView(R.id.tv_product_order_detail_product_time_text)
    TextView mProductTimeName;
    @BindView(R.id.tv_product_order_detail_product_time)
    TextView mProductTime;

    //底部按钮
    @BindView(R.id.tv_product_order_bottom_third_button)
    TextView mThirdBtn;

    @BindView(R.id.tv_product_order_bottom_reselect)
    TextView mReSelectBtn;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightBtn;

    private ProductOrderDetailPresenter mPresenter;
    private ProductOrderDetailBean data;

    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID = "extra_key_product_order_detail_order_id";
    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE = "extra_key_product_order_detail_order_type";
    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_POSITION = "extra_key_product_order_detail_order_position";
    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_SOURCE = "extra_key_product_order_detail_order_source";
    private String orderId;
    private String orderType;

    /**
     *
     * @param context  上下文
     * @param orderNo   orderId
     * @param orderType 订单类型
     * @param position  adapter 位置
     * @param source    true 全部订单 false 待处理
     */
    public static void launch(Context context, String orderNo, String orderType,int position,boolean source) {
        Intent intent = new Intent(context, WaitFirstPayProductOrderActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID, orderNo);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE, orderType);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_POSITION, position);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_SOURCE, source);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_order_detail_wait_first_pay);
        mTitle.setText("订单详情");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.service);
        findViewById(R.id.tv_product_order_bottom_first_button).setVisibility(View.GONE);
        findViewById(R.id.tv_product_order_bottom_second_button).setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mPresenter = new ProductOrderDetailPresenter(mContext, this);
        orderId = getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID);
        orderType = getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE);
        mPresenter.getProductOrderDetail(orderId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.tool_bar_right_img)
    public void goToIm() {
        mPresenter.goToIM();
    }

    @OnClick(R.id.tv_product_order_bottom_third_button)
    public void thirdBtn() {
        //判断是否现金支付
        if (OrderStatus.CASH.equals(data.extend2)) {
            //确认汇款
            int position = getIntent().getIntExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_POSITION,0);
            boolean source = getIntent().getBooleanExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_SOURCE,true);
            mPresenter.confirmCallback(data.extend3,data.orderStatus,position,source);
        } else {
            //付款
            goPay();
        }
    }

    @OnClick(R.id.tv_product_order_bottom_reselect)
    public void reSelect() {
        goPay();
    }

    /**
     * 付款
     */
    private void goPay(){
        if(OrderStatus.SAMPLING.equals(orderType)){
            ProductOrderSelectPayWayActivity.launch(this,orderId,"pay");
        }else {
            ProductOrderSelectPayWayActivity.launch(this,orderId,"firstPay");
        }
    }

    @Override
    public void showProductOrderDetail(ProductOrderDetailBean productOrderDetail) {
        data = productOrderDetail;
        //判断是否现金支付
        if (OrderStatus.CASH.equals(productOrderDetail.extend2)) {
            //已获取验证码
            mReSelectBtn.setVisibility(View.VISIBLE);
            mReSelectBtn.setText("重新选择支付方式");
            mThirdBtn.setText("确认汇款");
        } else {
            mReSelectBtn.setVisibility(View.GONE);
            mThirdBtn.setText("付款");
        }
        //地址
        mReceivePeople.setText(productOrderDetail.receiverName);
        mReceivePhone.setText(productOrderDetail.receiverMobile);
        mReceiveAddress.setText(productOrderDetail.receiverAddress);

        mOrderNo.setText(getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID));

        //创建时间
        mCreateTime.setText(TimeUtils.millis2String(Long.parseLong(productOrderDetail.orderDates.submit)));

        //订单金额
        String price ;
        if("collectFreight".equals(productOrderDetail.freightType)){
            price = getString(R.string.account,productOrderDetail.orderPrices.totalY);
        }else  {
            price = getString(R.string.account,productOrderDetail.orderPrices.totalY)
                    +getString(R.string.include_freight, productOrderDetail.orderPrices.freightY);
        }
        mOrderPrice.setText(price);

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

        //生产周期
        mProductTimeName.setText("生产周期");
        mProductTime.setText(productOrderDetail.produceInfos.productPeriodic+"天");

        switch (orderType) {
            case OrderStatus.SAMPLING:
                mOrderState.setText("待付款");
                mOrderType.setText("打样订单编号");
                //打样数量
                mSampleNum.setText(productOrderDetail.produceInfos.produce.sampleCount + "个");
                findViewById(R.id.rl_product_order_detail_produce_num).setVisibility(View.GONE);
                //生产类型
                mProductType.setText("打样生产");
                //待付首款
                mRlFirst_pay.setVisibility(View.GONE);
                break;
            case OrderStatus.SAMPLING_PRODUCE:
                mOrderState.setText("待付首款");
                mOrderType.setText("批量生产订单编号");
                //打样数量
                mSampleNum.setText(productOrderDetail.produceInfos.produce.sampleCount + "个");
                //生产类型
                mProductType.setText("批量生产");
                //生产数量
                mProduceNum.setText(productOrderDetail.produceInfos.produce.produceCount + "个");
                //待付首款
                mRlFirst_pay.setVisibility(View.VISIBLE);
                mFirstPay.setText(getString(R.string.account, productOrderDetail.orderPrices.firstPayY));
                break;
            case OrderStatus.PRODUCE:
                mOrderState.setText("待付首款");
                mOrderType.setText("批量生产订单编号");
                //生产类型
                mProductType.setText("批量生产");
                mRlSampleNum.setVisibility(View.GONE);
                //生产数量
                mProduceNum.setText(productOrderDetail.produceInfos.produce.produceCount + "个");
                //待付首款
                mRlFirst_pay.setVisibility(View.VISIBLE);
                mFirstPay.setText(getString(R.string.account, productOrderDetail.orderPrices.firstPayY));
                break;
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_OK){
            finishView();
        }
    }
}
