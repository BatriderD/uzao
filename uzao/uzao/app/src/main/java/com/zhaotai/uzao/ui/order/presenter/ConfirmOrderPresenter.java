package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.CreateOrderBean;
import com.zhaotai.uzao.bean.EventBean.RefreshCartInfo;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.PayConfirmOrderActivity;
import com.zhaotai.uzao.ui.order.contract.ConfirmOrderContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Time: 2017/5/31
 * Created by LiYou
 * Description :
 */

public class ConfirmOrderPresenter extends ConfirmOrderContract.Presenter {

    private ConfirmOrderContract.View view;

    public ConfirmOrderPresenter(Context context, ConfirmOrderContract.View view) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取地址
     */
    @Override
    public void getAddress() {
        Api.getDefault().getAddressData()
                .compose(RxHandleResult.<List<AddressBean>>handleResult())
                .subscribe(new RxSubscriber<List<AddressBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<AddressBean> addressBeen) {
                        if (addressBeen.size() > 0) {
                            view.showDefaultAddress(addressBeen.get(0));
                        } else {
                            view.showAddAddress();
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 创建订单
     */
    @Override
    public void createOrder(CreateOrderBean orderInfo) {
        if (!checkData(orderInfo)) return;

        Api.getDefault().createOrder(orderInfo)
                .compose(RxHandleResult.<List<OrderGoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<OrderGoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<OrderGoodsBean> orderGoodsBean) {
                        ArrayList<String> orderIds = new ArrayList<>();
                        for (int i = 0; i < orderGoodsBean.size(); i++) {
                            orderIds.add(orderGoodsBean.get(i).sequenceNBR);
                        }
                        PayConfirmOrderActivity.launch(mContext, orderIds);
                        //刷新购物车
                        EventBus.getDefault().post(new RefreshCartInfo());
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                        view.createOrderFail();
                    }
                });
    }

    /**
     * 创建设计订单
     */
    public void createDesignOrder(CreateOrderBean orderInfo) {
        if (!checkData(orderInfo)) return;

        Api.getDefault().createDesignOrder(orderInfo)
                .compose(RxHandleResult.<List<OrderGoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<OrderGoodsBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<OrderGoodsBean> orderGoodsBean) {
                        ArrayList<String> orderIds = new ArrayList<>();
                        for (int i = 0; i < orderGoodsBean.size(); i++) {
                            orderIds.add(orderGoodsBean.get(i).sequenceNBR);
                        }
                        PayConfirmOrderActivity.launch(mContext, orderIds);
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                        view.createOrderFail();
                    }
                });
    }


    /**
     * 检查数据完整性
     */
    @Override
    public boolean checkData(CreateOrderBean orderInfo) {
        if (StringUtil.isEmpty(orderInfo.receiverName)) {
            ToastUtil.showShort("请填写收货地址");
            return false;
        }
        return true;
    }

}
