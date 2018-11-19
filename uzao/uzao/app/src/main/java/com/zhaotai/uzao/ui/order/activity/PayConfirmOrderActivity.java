package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.PayConfirmOrderAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.EventBean.ModifyAddressInfo;
import com.zhaotai.uzao.bean.PayOrderBean;
import com.zhaotai.uzao.bean.PayOrderDetailBean;
import com.zhaotai.uzao.ui.order.contract.PayConfirmOrderContract;
import com.zhaotai.uzao.ui.order.presenter.PayConfirmOrderPresenter;
import com.zhaotai.uzao.ui.person.address.AddressFromOrderActivity;
import com.zhaotai.uzao.ui.person.discount.activity.SelectedCouponActivity;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/7/26
 * Created by LiYou
 * Description : 支付确认订单
 */

public class PayConfirmOrderActivity extends BaseActivity implements PayConfirmOrderContract.View, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private TextView activity;

    private PayConfirmOrderPresenter mPresenter;
    private PayConfirmOrderAdapter mAdapter;
    private TextView coupons;//优惠券
    private TextView mAccount;
    //需支付
    @BindView(R.id.tv_pay_order_confirm_need_pay_num)
    TextView mPayMoney;
    private TextView mTVPayWay;//支付方式

    //支付方式  1微信  2 支付宝
    private int PAY_WAY = 2;
    private String payWay = "alipay";
    private int goodsNum;
    private ArrayList<String> orderIds;//订单id集合
    private ImageView mPayImage;
    private int discountPosition = -1;//优惠券位置
    //private int discountPrice; // 优惠价格
    private String discountId;  //优惠券id
    private String payPrice;//支付价格
    private UITipDialog tipDialog;

    /**
     * @param context 上下文
     * @param orders  订单id
     */
    public static void launch(Context context, ArrayList<String> orders) {
        Intent intent = new Intent(context, PayConfirmOrderActivity.class);
        intent.putExtra("orderIds", orders);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pay_confirm_order);
        mTitle.setText("支付确认");
        mPresenter = new PayConfirmOrderPresenter(this, this);
        mAdapter = new PayConfirmOrderAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initData() {
        orderIds = (ArrayList<String>) getIntent().getExtras().get("orderIds");
        mPresenter.getOrderDetail(orderIds);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showDefaultAddress(AddressBean addressBeen) {
        PayOrderDetailBean payOrderDetailBean = mAdapter.getItem(addressBeen.position);
        if (payOrderDetailBean != null) {
            payOrderDetailBean.receiverAddress = addressBeen.addrAlias;
            payOrderDetailBean.receiverMobile = addressBeen.recieverPhone;
            payOrderDetailBean.receiverName = addressBeen.recieverName;
            mAdapter.notifyItemChanged(addressBeen.position);

            //修改订单收货地址
            mPresenter.modifyOrderAddress(addressBeen, payOrderDetailBean.sequenceNBR);
        }
    }

    @Override
    public void showAddAddress() {

    }

    @Override
    public void showOrderDetail(PayOrderBean payOrderBean) {
        payPrice = payOrderBean.payPrice;
        mAdapter.addData(payOrderBean.orderList);
        mPresenter.getCoupons(payOrderBean.canUseCouponPrice);
        //底部布局
        View footView = View.inflate(this, R.layout.foot_pay_confirm_order, null);
        //活动优惠
        activity = (TextView) footView.findViewById(R.id.tv_pay_confirm_order_activity_cut);
        //优惠券
        coupons = (TextView) footView.findViewById(R.id.tv_pay_confirm_coupons);
        coupons.setOnClickListener(this);
        //支付方式选择
        mTVPayWay = (TextView) footView.findViewById(R.id.tv_pay_confirm_pay_way);
        mPayImage = (ImageView) footView.findViewById(R.id.tv_pay_confirm_pay_icon);
        mTVPayWay.setOnClickListener(this);
        //小计
        mAccount = (TextView) footView.findViewById(R.id.tv_pay_confirm_order_account);
        goodsNum = 0;
        for (int i = 0; i < payOrderBean.orderList.size(); i++) {
            for (int j = 0; j < payOrderBean.orderList.get(i).orderDetailModels.size(); j++) {
                goodsNum += 1;
            }
        }
        mAccount.setText("共" + goodsNum + "件商品，小计¥" + DecimalUtil.getPrice(payOrderBean.payPrice));

        mAdapter.addFooterView(footView);
        activity.setText("减" + DecimalUtil.getPrice(payOrderBean.activityPrice) + "元");
        mPayMoney.setText(getString(R.string.need_pay_num, DecimalUtil.getPrice(payOrderBean.payPrice)));
    }

    @Override
    public void createOrderFail() {

    }

    /**
     * 优惠券
     */
    @Override
    public void showDiscount(List<DiscountCouponBean> discountCouponList) {
        if (discountCouponList.size() > 0) {
            coupons.setText("有可用的优惠券");
        } else {
            coupons.setText("无可用的优惠券");
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在发起支付")
                .create();
        tipDialog.show();
    }

    @Override
    public void stopProgress() {
        tipDialog.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            //更新列表
            AddressBean addressBean = (AddressBean) data.getExtras().get("addaddress");
            showDefaultAddress(addressBean);
        }

        switch (resultCode) {
            case 2://微信
                PAY_WAY = 1;
                payWay = "wx";
                mTVPayWay.setText(R.string.pay_way_wx);
                mPayImage.setImageResource(R.drawable.wx);
                break;
            case 3://支付宝
                PAY_WAY = 2;
                payWay = "alipay";
                mTVPayWay.setText(R.string.pay_way_zfb);
                mPayImage.setImageResource(R.drawable.zfb);
                break;
        }
        //回调选择购物券 -- 使用优惠券
        if (resultCode == 4) {
            discountId = data.getStringExtra("discountId");
            discountPosition = data.getIntExtra("discountSelectPosition", 0);
            //优惠价格
            int discountPrice = data.getIntExtra("discountPrice", 0);
            coupons.setText("减" + DecimalUtil.getPrice(String.valueOf(discountPrice)) + "元");
            int newPrice = Integer.parseInt(payPrice) - discountPrice;
            mPayMoney.setText(getString(R.string.need_pay_num, DecimalUtil.getPrice(String.valueOf(newPrice))));
        }
        //不使用优惠券
        if (resultCode == 5) {
            discountId = "";
            coupons.setText("有可用的优惠券");
            mPayMoney.setText(getString(R.string.need_pay_num, String.valueOf(payPrice)));
        }

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                LogUtils.logd("支付错误信息,errorMsg:" + errorMsg + "---- extraMsg:" + extraMsg);
                mPresenter.callback();
                if ("success".equals(result)) {
                    PayResultActivity.launch(mContext, "success", String.valueOf(goodsNum), (mPayMoney.getText().toString()).substring(5), orderIds);
                } else {
                    PayResultActivity.launch(mContext, "fail", String.valueOf(goodsNum), (mPayMoney.getText().toString()).substring(5), orderIds);
                }
            }
        }
    }

    /**
     * 支付
     */
    @OnClick(R.id.tv_pay_order_confirm_pay)
    public void goToPay() {
        mPresenter.getPayInfo(payWay, orderIds, discountId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //支付方式
            case R.id.tv_pay_confirm_pay_way:
                Intent intent = new Intent(mContext, SelectPayWayActivity.class);
                intent.putExtra("payWay", PAY_WAY);
                startActivityForResult(intent, 123);
                break;
            //优惠券
            case R.id.tv_pay_confirm_coupons:
                SelectedCouponActivity.launch(this, discountPosition, payPrice);
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            //修改地址
            case R.id.ll_pay_confirm_address:
                AddressFromOrderActivity.launch(PayConfirmOrderActivity.this, position);
                break;
        }
    }

    /**
     * 接收地址
     *
     * @param addressBean 地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddressBean addressBean) {
        showDefaultAddress(addressBean);
    }

    /**
     * 接收添加地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModifyAddressInfo<AddressBean> addressBean) {
        showDefaultAddress(addressBean.data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
