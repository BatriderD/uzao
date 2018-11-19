package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.BaseResult;
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
 * Time: 2017/8/23
 * Created by LiYou
 * Description : 关闭订单
 */

public class ProduceOrderClosedActivity extends BaseActivity implements ProductOrderContract.ProductOrderDetailView {

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

    //关闭时间
    @BindView(R.id.tv_product_order_detail_close_time)
    TextView mCloseTime;
    //关闭理由
    @BindView(R.id.tv_product_order_detail_close_reason)
    TextView mCloseReason;

    //创建时间
    @BindView(R.id.tv_product_order_detail_create_time)
    TextView mCreateTime;

    //订单金额
    @BindView(R.id.tv_product_order_detail_price)
    TextView mOrderPrice;

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
    @BindView(R.id.tv_product_order_detail_product_time_text)
    TextView mProductTimeName;
    @BindView(R.id.tv_product_order_detail_sample_num)
    TextView mSampleNum;
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

    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID = "extra_key_product_order_detail_order_id";
    private static final String EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE = "extra_key_product_order_detail_order_type";

    /**
     *
     * @param context 上下文
     * @param orderNo 订单id
     */
    public static void launch(Context context, String orderNo,String orderType) {
        Intent intent = new Intent(context, ProduceOrderClosedActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID, orderNo);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE, orderType);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_order_detail_closed);
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


    @Override
    public void showProductOrderDetail(ProductOrderDetailBean productOrderDetail) {
        String orderType = getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_TYPE);
        mOrderState.setText("已关闭");
        //地址
        mReceivePeople.setText(productOrderDetail.receiverName);
        mReceivePhone.setText(productOrderDetail.receiverMobile);
        mReceiveAddress.setText(productOrderDetail.receiverAddress);

        //订单号
        switch (orderType){
            case OrderStatus.SAMPLING:
                mOrderType.setText("打样订单编号");
                break;
            default:
                mOrderType.setText("批量生产订单编号");
                break;
        }

        mOrderNo.setText(getIntent().getStringExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID));
        //关闭时间
        mCloseTime.setText(TimeUtils.millis2String(Long.parseLong(productOrderDetail.orderDates.close)));
        //关闭理由
        mCloseReason.setText(productOrderDetail.closeReason);

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

        switch (orderType){
            case OrderStatus.SAMPLING:
                //生产类型
                mProductType.setText("打样生产");
                findViewById(R.id.rl_product_order_detail_produce_num).setVisibility(View.GONE);
                mSampleNum.setText(productOrderDetail.sampleCount + "个");
                break;
            case OrderStatus.SAMPLING_PRODUCE:
                mProductType.setText("批量生产");
                mSampleNum.setText(productOrderDetail.sampleCount + "个");
                mProduceNum.setText(productOrderDetail.produceInfos.produce.produceCount + "个");
                break;
            case OrderStatus.PRODUCE:
                mProductType.setText("批量生产");
                findViewById(R.id.rl_product_order_detail_sample_num).setVisibility(View.GONE);
                mProduceNum.setText(productOrderDetail.produceInfos.produce.produceCount + "个");
                break;
        }

        //生产周期
        mProductTimeName.setText("生产周期");
        mProductTime.setText(productOrderDetail.produceInfos.productPeriodic);
    }

    @Override
    public void finishView() {
        finish();
    }
}
