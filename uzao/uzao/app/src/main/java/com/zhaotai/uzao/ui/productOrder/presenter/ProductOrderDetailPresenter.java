package com.zhaotai.uzao.ui.productOrder.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.produceOrder.AllProduceOrderReceiveRemitEvent;
import com.zhaotai.uzao.bean.EventBean.produceOrder.AllSampleProduceReceiveEvent;
import com.zhaotai.uzao.bean.EventBean.produceOrder.WaitHandleProduceOrderReceiveRemitEvent;
import com.zhaotai.uzao.bean.EventBean.produceOrder.WaitHandleSampleProduceReceiveEvent;
import com.zhaotai.uzao.bean.PayCallBackBean;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;
import com.zhaotai.uzao.ui.productOrder.activity.ProducePayInfoActivity;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description : 生产订单详情
 */

public class ProductOrderDetailPresenter extends ProductOrderContract.ProductOrderDetailPresenter {

    private ProductOrderContract.ProductOrderDetailView view;
    private UITipDialog tipDialog;

    public ProductOrderDetailPresenter(Context context, ProductOrderContract.ProductOrderDetailView view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 订单详情
     *
     * @param orderNo 订单id
     */
    @Override
    public void getProductOrderDetail(String orderNo) {
        Api.getDefault().getProductOrderDetail(orderNo)
                .compose(RxHandleResult.<ProductOrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<ProductOrderDetailBean>(mContext) {
                    @Override
                    public void _onNext(ProductOrderDetailBean s) {
                        view.showProductOrderDetail(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 确认收货 -- 打样的确认收货
     *
     * @param orderNo 订单id
     * @param type    sampling（打样收货）、produce（大货样收货）
     */
    @Override
    public void receiveSampleProduct(String orderNo, String type, final int position, final boolean source, final String orderStatus) {
        Api.getDefault().receiveSampleProduce(orderNo, type)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext) {
                    @Override
                    public void _onNext(Object s) {
                        if (source) {
                            AllSampleProduceReceiveEvent event = new AllSampleProduceReceiveEvent(position, orderStatus);
                            EventBus.getDefault().post(event);
                        } else {
                            WaitHandleSampleProduceReceiveEvent event = new WaitHandleSampleProduceReceiveEvent(position, orderStatus);
                            EventBus.getDefault().post(event);
                        }
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void receiveProduct(String orderNo, final int position, final boolean source, final String orderStatus) {
        Api.getDefault().receiveProduce(orderNo)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext) {
                    @Override
                    public void _onNext(Object s) {
                        if (source) {//全部订单
                            AllSampleProduceReceiveEvent event = new AllSampleProduceReceiveEvent(position, orderStatus);
                            EventBus.getDefault().post(event);
                        } else {//待处理
                            WaitHandleSampleProduceReceiveEvent event = new WaitHandleSampleProduceReceiveEvent(position, orderStatus);
                            EventBus.getDefault().post(event);
                        }
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void goToIM() {

        final Map<String, String> map = new ArrayMap<>();
        map.put(Field.PHONE, LoginHelper.getLoginId());
        map.put(Field.USERNAME,LoginHelper.getUserName());

        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONObject jsonObject = SafeJson.parseObj(result);
                    int resultCode = SafeJson.safeInt(jsonObject, "error");
                    if (resultCode == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            mContext.startActivity(new Intent(mContext, KF5ChatActivity.class));
                            Log.i("kf5测试", MD5Utils.GetMD5Code("kf5_ticket_" + SPUtils.getUserId()));
                        }
                    } else if (resultCode == 10050) {
                        createImUser(mContext, map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {

            }
        });
    }

    /**
     * 获取支付信息
     */
    @Override
    public void getPayInfo(Context context, ProductOrderDetailBean data) {
        for (int i = 0; i < data.tradeInfos.size(); i++) {
            if ("pay".equals(data.tradeInfos.get(i).payType)) {
                ProducePayInfoActivity.launch(context, data.tradeInfos.get(i).tradeNo, data.tradeInfos.get(i).tradeChannelName, data.orderDates.pay);
            }
        }
    }

    /**
     * 首款支付信息
     */
    @Override
    public void firstPayInfo(Context context, ProductOrderDetailBean data) {
        for (int i = 0; i < data.tradeInfos.size(); i++) {
            if ("firstPay".equals(data.tradeInfos.get(i).payType)) {
                ProducePayInfoActivity.launch(context, data.tradeInfos.get(i).tradeNo, data.tradeInfos.get(i).tradeChannelName, data.orderDates.firstPay);
            }
        }
    }

    /**
     * 尾款支付信息
     */
    @Override
    public void lastPayInfo(Context context, ProductOrderDetailBean data) {
        for (int i = 0; i < data.tradeInfos.size(); i++) {
            if ("lastPay".equals(data.tradeInfos.get(i).payType)) {
                ProducePayInfoActivity.launch(context, data.tradeInfos.get(i).tradeNo, data.tradeInfos.get(i).tradeChannelName, data.orderDates.firstPay);
            }
        }
    }

    /**
     * 样品物流信息
     */
    @Override
    public void sampleLogistics(Context context, ProductOrderDetailBean data) {
        for (int i = 0; i < data.transportInfos.size(); i++) {
            if ("sampling_designer".equals(data.transportInfos.get(i).transportType)) {
                LogisticsActivity.launch(context, LogisticsActivity.TYPE_TRANSPORT, data.transportInfos.get(i).transportId);
            }
        }
    }

    /**
     * 大货样品物流信息
     */
    @Override
    public void sampleProduceLogistics(Context context, ProductOrderDetailBean data) {
        for (int i = 0; i < data.transportInfos.size(); i++) {
            if ("produce_designer".equals(data.transportInfos.get(i).transportType)) {
                LogisticsActivity.launch(context, LogisticsActivity.TYPE_TRANSPORT, data.transportInfos.get(i).transportId);
            }
        }
    }

    /**
     * 物流信息
     *
     * @param orderNo 订单id
     */
    @Override
    public void logistics(Context context, String orderNo) {
        LogisticsActivity.launch(context, LogisticsActivity.TYPE_PRODUCE_ID, orderNo);
    }

    /**
     * 汇款回调
     * @param tradeId  交易号
     * @param orderStatus   订单状态
     * @param position   adapter位置
     * @param source       true全部订单 false 待处理订单
     */
    @Override
    public void confirmCallback(final String tradeId, final String orderStatus, final int position, final boolean source) {
        AlertDialog alert = new AlertDialog.Builder(mContext)
                .setMessage("您确定已汇款?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Api.getDefault().callbackPay(tradeId,"")
                                .compose(RxHandleResult.<PayCallBackBean>handleResult())
                                .subscribe(new RxSubscriber<PayCallBackBean>(mContext, false) {
                                    @Override
                                    public void _onNext(PayCallBackBean s) {
                                        if(source){
                                            //全部订单
                                            EventBus.getDefault().post(new AllProduceOrderReceiveRemitEvent(position,orderStatus));
                                        }else {
                                            EventBus.getDefault().post(new WaitHandleProduceOrderReceiveRemitEvent(position,orderStatus));
                                        }
                                        view.finishView();
                                    }

                                    @Override
                                    public void _onError(String message) {
                                    }
                                });
                    }
                }).create();
        alert.show();
    }

    @Override
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
    }

    @Override
    public void stopProgress() {
        if(tipDialog != null)
            tipDialog.dismiss();
    }

    private void createImUser(final Context context, Map<String, String> map) {
        //用户不存在
        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result1) {
                final JSONObject jsonObject = SafeJson.parseObj(result1);
                int resultCode1 = SafeJson.safeInt(jsonObject, "error");
                try {
                    if (resultCode1 == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            context.startActivity(new Intent(context, KF5ChatActivity.class));
                            Log.i("kf5测试", MD5Utils.GetMD5Code("kf5_ticket_" + SPUtils.getUserId()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("kf5测试", "创建用户成功" + result1);
            }

            @Override
            public void onFailure(String result) {
                Log.i("kf5测试", "创建用户失败" + result);
            }
        });
    }
}
