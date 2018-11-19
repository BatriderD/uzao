package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;
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
 * Description : 打样 --已完成
 */

public class SampleFinishedActivity extends BaseActivity implements ProductOrderContract.ProductOrderDetailView{

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

    public static void launch(Context context, String orderNo) {
        Intent intent = new Intent(context, SampleFinishedActivity.class);
        intent.putExtra(EXTRA_KEY_PRODUCT_ORDER_DETAIL_ORDER_ID, orderNo);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_order_detail_sample_finished);
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
        mPresenter.getPayInfo(mContext,data);
    }

    /**
     * 查看物流
     */
    @OnClick(R.id.tv_product_order_sample_logistics)
    public void sampleLogistics() {
        mPresenter.sampleLogistics(mContext,data);
    }

    @Override
    public void showProductOrderDetail(ProductOrderDetailBean productOrderDetail) {
        data = productOrderDetail;
        mOrderState.setText("已完成");
        //地址
        mReceivePeople.setText(productOrderDetail.receiverName);
        mReceivePhone.setText(productOrderDetail.receiverMobile);
        mReceiveAddress.setText(productOrderDetail.receiverAddress);

        //订单号
        mOrderType.setText("打样订单编号");

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
        if(StringUtil.isEmpty(productOrderDetail.invoiceTitle)){
            mBillTitle.setText("无");
            mBillNo.setText("无");
            mBillContent.setText("无");
        }else {
            mBillTitle.setText(productOrderDetail.invoiceTitle);
            mBillNo.setText(productOrderDetail.invoiceNumber);
            mBillContent.setText(productOrderDetail.invoiceContent);
        }

        //效果图
        if(productOrderDetail.produceInfos.produce.customDesignModel.effectImages != null) {
            mLlProductEffectImage.setVisibility(View.VISIBLE);
            mGridView.setFocusable(false);
            mGridView.setAdapter(new EffectImageAdapter(mContext,productOrderDetail.produceInfos.produce.customDesignModel.effectImages));
        }else {
            mLlProductEffectImage.setVisibility(View.GONE);
        }

        //生产信息
        //名字
        mProductName.setText(productOrderDetail.produceInfos.produce.customDesignModel.designName);
        //生产类型
        mProductType.setText("打样生产");
        //打样数量
        mSampleNum.setText(productOrderDetail.produceInfos.produce.sampleCount+"个");
        //生产周期
        mProductTimeName.setText("生产周期");
        mProductTime.setText(productOrderDetail.produceInfos.productPeriodic+"天");
    }

    @Override
    public void finishView() {

    }
}
