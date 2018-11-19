package com.zhaotai.uzao.ui.order.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderDetailMultiBean;
import com.zhaotai.uzao.ui.order.adapter.OrderMultiDetailAdapter;
import com.zhaotai.uzao.ui.order.presenter.OrderDetailPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2018/7/16 0016
 * Created by LiYou
 * Description :订单详情
 */
public class BaseOrderDetailActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private TextView mReceiveName;
    private TextView mReceivePhone;
    private TextView mReceiveAddress;
    private TextView mCreateTime;

    public OrderMultiDetailAdapter mAdapter;
    public OrderDetailPresenter mPresenter;

    public String orderNo;
    private TextView mOrderNo;
    private TextView mPayTime;
    private TextView mInvoiceType;
    private TextView mInvoiceTitle;
    private TextView mInvoiceContent;
    private TextView mTotalPrice;
    private TextView mCutPrice;
    private TextView mDiscountPrice;
    private TextView mPayPrice;
    private TextView mPayWay;
    public RelativeLayout mRlBottom;
    public List<OrderDetailMultiBean> mMultiData = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_detail);
        mTitle.setText("订单详情");
        //头布局
        View headView = View.inflate(this, R.layout.head_order_detail, null);
        mReceiveName = (TextView) headView.findViewById(R.id.tv_order_detail_receive_people);
        mReceivePhone = (TextView) headView.findViewById(R.id.tv_order_detail_receive_phone);
        mReceiveAddress = (TextView) headView.findViewById(R.id.tv_order_detail_receive_address);
        //脚布局
        View footView = View.inflate(this, R.layout.foot_order_detail, null);
        //订单编号
        mOrderNo = (TextView) footView.findViewById(R.id.tv_order_detail_number);
        //创建时间
        mCreateTime = (TextView) footView.findViewById(R.id.tv_order_detail_create_time);

        //发票类型
        mInvoiceType = (TextView) footView.findViewById(R.id.tv_order_detail_invoice_type);
        //发票抬头
        mInvoiceTitle = (TextView) footView.findViewById(R.id.tv_order_detail_invoice_title);
        //发票内容
        mInvoiceContent = (TextView) footView.findViewById(R.id.tv_order_detail_invoice_content);

        //支付方式
        mPayWay = (TextView) footView.findViewById(R.id.tv_order_detail_pay_way);
        //付款时间
        mPayTime = (TextView) footView.findViewById(R.id.tv_order_detail_pay_time);

        //商品金额
        mTotalPrice = (TextView) footView.findViewById(R.id.tv_order_detail_total_price);
        //活动立减
        mCutPrice = (TextView) footView.findViewById(R.id.tv_order_detail_cut_price);
        //优惠券
        mDiscountPrice = (TextView) footView.findViewById(R.id.tv_order_detail_discount_price);
        //实付金额
        mPayPrice = (TextView) footView.findViewById(R.id.tv_order_detail_pay_price);
        //复制
        footView.findViewById(R.id.tv_order_detail_copy_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Label", orderNo);
                assert cm != null;
                cm.setPrimaryClip(data);
                ToastUtil.showShort("订单编号已复制到粘贴板");
            }
        });

        mRlBottom = (RelativeLayout) findViewById(R.id.order_detail_foot);
        mRlBottom.setVisibility(View.GONE);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderMultiDetailAdapter(mMultiData);
        mAdapter.addHeaderView(headView);
        mAdapter.addFooterView(footView);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
    }

    public void initDetail(OrderDetailBean orderDetailBean) {
        orderNo = orderDetailBean.orderModel.orderNo;
        //订单编号
        mOrderNo.setText(getString(R.string.order_child_num, orderNo));
        //创建时间
        mCreateTime.setText(getString(R.string.create_time, TimeUtils.millis2String(Long.parseLong(orderDetailBean.orderDates.submit))));
        //付款时间
        if (orderDetailBean.orderDates != null && orderDetailBean.orderDates.pay != null) {
            mPayTime.setText(getString(R.string.pay_time, TimeUtils.millis2String(Long.parseLong(orderDetailBean.orderDates.pay))));
        } else {
            mPayTime.setVisibility(View.GONE);
        }

        //发票类型
        if (orderDetailBean.orderModel.orderInvoiceModel == null) {
            //无发票
            mInvoiceType.setText("无");
            mInvoiceTitle.setText("无");
            mInvoiceContent.setText("无");
        } else {
            //有发票
            mInvoiceType.setText("普通纸质发票");
            if (StringUtil.isEmpty(orderDetailBean.orderModel.orderInvoiceModel.invoiceTitle)) {
                mInvoiceTitle.setText(orderDetailBean.orderModel.orderInvoiceModel.userName);
            } else {
                mInvoiceTitle.setText(orderDetailBean.orderModel.orderInvoiceModel.invoiceTitle);
            }
            mInvoiceContent.setText(orderDetailBean.orderModel.orderInvoiceModel.invoiceContent);
        }

        //支付方式
        if (orderDetailBean.tradeInfo != null && orderDetailBean.tradeInfo.tradeChannel != null) {
            mPayWay.setText(getString(R.string.order_detail_pay_way, orderDetailBean.tradeInfo.tradeChannel));
        } else {
            mPayWay.setText(getString(R.string.order_detail_pay_way, "未选择"));
        }

        //商品合计
        if (orderDetailBean.orderModel != null) {
            mTotalPrice.setText(getString(R.string.product_total_price, orderDetailBean.orderModel.totalAmountY));
        }
        //活动立减
        if (orderDetailBean.orderModel != null)
            mCutPrice.setText(getString(R.string.order_detail_cut_price, orderDetailBean.orderModel.activityAmountY.isEmpty() ? "0.00" : orderDetailBean.orderModel.activityAmountY));
        //优惠券
        if (orderDetailBean.orderModel != null)
            mDiscountPrice.setText(getString(R.string.order_detail_discount_price, orderDetailBean.orderModel.couponAmountY.isEmpty() ? "0.00" : orderDetailBean.orderModel.couponAmountY));
        //实付金额
        if (orderDetailBean.orderModel != null) {
            if (orderDetailBean.orderModel.payAmount != null) {
                mPayPrice.setText(getString(R.string.account, orderDetailBean.orderModel.payAmountY));
            } else {
                mPayPrice.setText(getString(R.string.account, orderDetailBean.orderModel.totalAmountY));
            }
        }

        //地址
        mReceiveName.setText(orderDetailBean.orderModel.receiverName);
        mReceivePhone.setText(orderDetailBean.orderModel.receiverMobile);
        mReceiveAddress.setText(orderDetailBean.orderModel.receiverAddress);

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

}
