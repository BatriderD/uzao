package com.zhaotai.uzao.ui.order.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.RefreshAllOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshWaitPayOrderEvent;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderItemBean;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.ui.order.adapter.CancelOrderAdapter;
import com.zhaotai.uzao.ui.order.contract.OrderContract;
import com.zhaotai.uzao.ui.order.presenter.OrderPresenter;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/6/9
 * Created by LiYou
 * Description : 待付款详情页面
 */

public class WaitPayDetailActivity extends BaseActivity implements OrderContract.WaitPayView,
        View.OnClickListener {


    @BindView(R.id.tv_order_detail_left_btn)
    TextView mLeftBtn;
    @BindView(R.id.tv_order_detail_right_btn)
    TextView mRightBtn;

    @BindView(R.id.ll_order_detail_item)
    LinearLayout mLlitem;
    //发票布局
    @BindView(R.id.ll_invoice)
    LinearLayout mLlInvoice;
    //收货地址
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;

    @BindView(R.id.ll_order_detail)
    LinearLayout mLlDetail;

    private TextView mReceiveName;
    private TextView mReceivePhone;
    private TextView mReceiveAddress;
    private TextView mCreateTime;

    private OrderPresenter orderPresenter;

    private String orderNo;
    private int itemPosition = 0;
    private TextView mOrderNo;
    private TextView mPayTime;
    private TextView mInvoiceType;
    private TextView mInvoiceTitle;
    private TextView mInvoiceContent;
    private TextView mTotalPrice;
    private TextView mCutPrice;
    private TextView mDiscountPrice;
    private TextView mPayPrice;
    private TextView mCountTime;
    private UIBottomSheet uiBottomSheet;
    private CancelOrderAdapter cancelOrderAdapter;

    /**
     * @param orderPosition adapter位置
     * @param fromType      true 全部订单  false 待付款
     */
    public static void launch(Context context, String orderId, int orderPosition, boolean fromType) {
        Intent intent = new Intent(context, WaitPayDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderPosition", orderPosition);
        intent.putExtra("fromType", fromType);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_detail_wait_pay);
        EventBus.getDefault().register(this);
        mTitle.setText("订单详情");
        findViewById(R.id.ll_order_detail_state).setVisibility(View.VISIBLE);
        //倒计时
        mCountTime = (TextView) findViewById(R.id.tv_order_detail_count_down);
        TextView mState = (TextView) findViewById(R.id.order_detail_state);
        mState.setText("待付款");
        mReceiveName = (TextView) findViewById(R.id.tv_order_detail_receive_people);
        mReceivePhone = (TextView) findViewById(R.id.tv_order_detail_receive_phone);
        mReceiveAddress = (TextView) findViewById(R.id.tv_order_detail_receive_address);

        //订单编号
        mOrderNo = (TextView) findViewById(R.id.tv_order_detail_number);
        //创建时间
        mCreateTime = (TextView) findViewById(R.id.tv_order_detail_create_time);

        //发票类型
        mInvoiceType = (TextView) findViewById(R.id.tv_order_detail_invoice_type);
        //发票抬头
        mInvoiceTitle = (TextView) findViewById(R.id.tv_order_detail_invoice_title);
        //发票内容
        mInvoiceContent = (TextView) findViewById(R.id.tv_order_detail_invoice_content);

        //支付方式
        findViewById(R.id.tv_order_detail_pay_way).setVisibility(View.GONE);
        //付款时间
        findViewById(R.id.tv_order_detail_pay_time).setVisibility(View.GONE);

        //商品金额
        mTotalPrice = (TextView) findViewById(R.id.tv_order_detail_total_price);
        //活动立减
        mCutPrice = (TextView) findViewById(R.id.tv_order_detail_cut_price);
        //优惠券
        mDiscountPrice = (TextView) findViewById(R.id.tv_order_detail_discount_price);
        //实付金额
        mPayPrice = (TextView) findViewById(R.id.tv_order_detail_pay_price);

        //复制
        findViewById(R.id.tv_order_detail_copy_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Label", orderNo);
                assert cm != null;
                cm.setPrimaryClip(data);
                ToastUtil.showShort("订单编号已复制到粘贴板");
            }
        });

        mLeftBtn.setText("取消订单");
        mLeftBtn.setBackgroundResource(R.drawable.shape_order_btn);
        mRightBtn.setBackgroundResource(R.drawable.shape_order_btn);
        mRightBtn.setText("付款");
        mRightBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        orderPresenter = new OrderPresenter(this, this);
        String orderId = getIntent().getStringExtra("orderId");
        itemPosition = getIntent().getIntExtra("orderPosition", 0);
        orderPresenter.getOrderDetail(orderId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 显示详情
     */
    @Override
    public void showDetail(OrderDetailBean orderDetailBean) {
        orderNo = orderDetailBean.orderModel.orderNo;
        //订单编号
        mOrderNo.setText(getString(R.string.order_child_num, orderNo));
        //创建时间
        mCreateTime.setText(getString(R.string.create_time, TimeUtils.millis2String(Long.parseLong(orderDetailBean.orderDates.submit))));

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
        if (orderDetailBean.orderModel != null && orderDetailBean.orderModel.payAmount == null) {
            mPayPrice.setText(getString(R.string.account, orderDetailBean.orderModel.totalAmountY));
        } else {
            mPayPrice.setText(getString(R.string.account, orderDetailBean.orderModel.payAmountY));
        }

        switch (orderDetailBean.orderModel.orderType) {
            case "Material":
                mLlInvoice.setVisibility(View.GONE);
                mRlAddress.setVisibility(View.GONE);
                showMaterialList(orderDetailBean);
                break;
            default:
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

                //地址
                mReceiveName.setText(orderDetailBean.orderModel.receiverName);
                mReceivePhone.setText(orderDetailBean.orderModel.receiverMobile);
                mReceiveAddress.setText(orderDetailBean.orderModel.receiverAddress);

                showProductList(orderDetailBean);
                showMaterialList(orderDetailBean);
                break;
        }

        //倒计时
        if (!StringUtil.isEmpty(orderDetailBean.orderModel.remainingTime)) {
            mCountTime.setVisibility(View.VISIBLE);
            orderPresenter.countDown(Integer.parseInt(orderDetailBean.orderModel.remainingTime));
        }
    }

    @SuppressLint("SetTextI18n")
    private void showMaterialList(OrderDetailBean orderDetailBean) {
        for (int i = 0; i < orderDetailBean.orderModel.materialOrderDetailModels.size(); i++) {
            OrderMaterialBean item = orderDetailBean.orderModel.materialOrderDetailModels.get(i);

            View itemView = View.inflate(mContext, R.layout.item_order_goods, null);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_order_goods_image);
            TextView title = (TextView) itemView.findViewById(R.id.tv_order_goods_title);
            TextView price = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
            TextView type = (TextView) itemView.findViewById(R.id.tv_order_goods_properties);
            TextView time = (TextView) itemView.findViewById(R.id.tv_material_time);
            View divider = itemView.findViewById(R.id.tv_order_bottom_divider);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.materialInfo.thumbnail), imageView);
            title.setText(item.materialInfo.sourceMaterialName);
            price.setText(getString(R.string.account, item.materialInfo.priceY));
            if (item.designerInfo != null) {
                type.setText("设计师：" + item.designerInfo.nickName);
            }

            //授权时长
            String periodUnit = "";
            if ("month".equals(item.periodUnit)) {
                periodUnit = "个月";
            } else if ("year".equals(item.periodUnit)) {
                periodUnit = "年";
            } else if ("forever".equals(item.periodUnit)) {
                periodUnit = "永久";
                item.authPeriod = "";
            }
            time.setVisibility(View.VISIBLE);
            time.setText("授权时长:    " + item.authPeriod + periodUnit);

            if (i == orderDetailBean.orderModel.materialOrderDetailModels.size() - 1) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            mLlitem.addView(itemView);
        }
    }

    private void showProductList(OrderDetailBean orderDetailBean) {
        Gson gson = new Gson();
        for (int i = 0; i < orderDetailBean.orderModel.orderPackage.unDelivery.size(); i++) {
            OrderItemBean item = orderDetailBean.orderModel.orderPackage.unDelivery.get(i);
            OrderGoodsDetailBean orderGoodsDetailBean = gson.fromJson(item.detail, OrderGoodsDetailBean.class);

            View itemView = View.inflate(mContext, R.layout.item_order_goods, null);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_order_goods_image);
            TextView title = (TextView) itemView.findViewById(R.id.tv_order_goods_title);
            TextView type = (TextView) itemView.findViewById(R.id.tv_order_goods_properties);
            TextView num = (TextView) itemView.findViewById(R.id.tv_order_goods_num);
            TextView price = (TextView) itemView.findViewById(R.id.tv_order_goods_price);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + orderGoodsDetailBean.pic, imageView);
            title.setText(orderGoodsDetailBean.name);
            type.setText(orderGoodsDetailBean.category);
            num.setText(getString(R.string.goods_number, item.count));
            price.setText(getString(R.string.account, item.unitPriceY));

            mLlitem.addView(itemView);
        }
    }

    /**
     * 取消订单
     */
    @Override
    public void cancelOrder() {
        if (uiBottomSheet != null) {
            uiBottomSheet.dismiss();
        }
        finish();
    }

    /**
     * 更新倒计时
     */
    @Override
    public void updateTime(String time) {
        mCountTime.setText(getString(R.string.count_time, time));
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void showCancelReason(List<DictionaryBean> dictionaryBeanList) {
        if (uiBottomSheet == null) {
            uiBottomSheet = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.view_recycle, null);
            bottomSheetView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            RecyclerView mRecycler = (RecyclerView) bottomSheetView.findViewById(R.id.recycler);
            mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            cancelOrderAdapter = new CancelOrderAdapter();
            cancelOrderAdapter.setNewData(dictionaryBeanList);
            mRecycler.setAdapter(cancelOrderAdapter);
            uiBottomSheet.setContentView(bottomSheetView);
            uiBottomSheet.show();
            cancelOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    DictionaryBean cancelReason = (DictionaryBean) adapter.getItem(position);
                    if (cancelReason != null) {
                        boolean type = getIntent().getBooleanExtra("fromType", true);
                        if (type) {
                            orderPresenter.cancelOrder(orderNo, cancelReason.entryValue);
                        } else {
                            orderPresenter.waitPayDetailCancelOrder(orderNo, cancelReason.entryValue, itemPosition);
                        }
                    }

                }
            });
        } else {
            uiBottomSheet.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_detail_left_btn:
                if (StringUtil.isEmpty(orderNo)) return;
                orderPresenter.getCancelReason();
                break;
            case R.id.tv_order_detail_right_btn:
                if (StringUtil.isEmpty(orderNo)) return;
                //去付款
                ArrayList<String> orderIds = new ArrayList<>();
                orderIds.add(orderNo);
                PayConfirmOrderFromListActivity.launch(mContext, orderIds, getIntent().getBooleanExtra("fromType", true));
                break;
        }
    }

    /**
     * 付款成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshWaitPayOrderEvent refreshWaitPayOrderEvent) {
        finish();
    }

    /**
     * 付款成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshAllOrderEvent refreshWaitPayOrderEvent) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
