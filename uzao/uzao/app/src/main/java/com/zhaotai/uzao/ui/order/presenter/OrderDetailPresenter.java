package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderDetailMultiBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderItemBean;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.bean.PackageBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.order.contract.OrderDetailContract;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/7/16 0016
 * Created by LiYou
 * Description :
 */
public class OrderDetailPresenter extends OrderDetailContract.Presenter {

    private OrderDetailContract.View view;
    private Gson gson;

    public OrderDetailPresenter(Context context, OrderDetailContract.View view) {
        this.view = view;
        mContext = context;
        gson = new Gson();
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单Id
     */
    @Override
    public void getOrderDetail(String orderId) {
        Api.getDefault().getOrderDetail(orderId)
                .compose(RxHandleResult.<OrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<OrderDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(OrderDetailBean orderDetailBean) {
                        view.showDetail(orderDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 确认收货
     *
     * @param packageOrderId 订单Id
     */
    public void confirmReceiveGoods(String packageOrderId) {
        OrderItemBean info = new OrderItemBean();
        info.orderNo = packageOrderId;
        Api.getDefault().confirmOrder(info)
                .compose(RxHandleResult.<OrderBean>handleResult())
                .subscribe(new RxSubscriber<OrderBean>(mContext, true) {
                    @Override
                    public void _onNext(OrderBean s) {
                        ToastUtil.showShort("收货成功");
                        //通知收货成功
                        EventBus.getDefault().post(new RefreshOrderEvent());
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 构造多布局数据
     */
    public List<OrderDetailMultiBean> constructMultiData(OrderBean order) {
        List<OrderDetailMultiBean> multiList = new ArrayList<>();
        if (order.orderPackage != null) {
            switch (order.orderStatus) {
                case OrderStatus.WAIT_APPROVE://待审核
                    OrderDetailMultiBean o12 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION_VIEW);
                    multiList.add(o12);
                    if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                        for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                            OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                            OrderDetailMultiBean o2 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                            o2.pic = detailBean.pic;
                            o2.category = detailBean.category;
                            o2.name = detailBean.name;
                            o2.price = unDelivery.unitPriceY;
                            o2.count = unDelivery.count;
                            multiList.add(o2);
                        }
                    }
                    break;
                case OrderStatus.WAIT_HANDLE:
                case OrderStatus.WAIT_DELIVERY://待发货
                    if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                        //1#添加待发货
                        OrderDetailMultiBean o1 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION);
                        o1.state = OrderStatus.WAIT_DELIVERY;
                        o1.packageNum = "未发货商品";
                        multiList.add(o1);
                        for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                            OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                            OrderDetailMultiBean o2 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                            o2.pic = detailBean.pic;
                            o2.category = detailBean.category;
                            o2.name = detailBean.name;
                            o2.price = unDelivery.unitPriceY;
                            o2.count = unDelivery.count;
                            multiList.add(o2);
                        }
                    }

                    //2# 添加包裹
                    if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0) {
                        for (PackageBean packageBean : order.orderPackage.packageBig) {
                            OrderDetailMultiBean o3 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION);
                            o3.state = packageBean.orderStatus;
                            o3.isCommend = packageBean.isComment;
                            o3.packageNum = "包裹" + packageBean.packageNum;
                            multiList.add(o3);
                            for (OrderItemBean item : packageBean.skus) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(item.detail, OrderGoodsDetailBean.class);
                                OrderDetailMultiBean o4 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                                o4.pic = detailBean.pic;
                                o4.category = detailBean.category;
                                o4.name = detailBean.name;
                                o4.price = item.unitPriceY;
                                o4.count = item.count;
                                multiList.add(o4);
                            }
                        }
                    }
                    break;
                case OrderStatus.WAIT_RECEIVE://待收货
                    //1# 添加包裹
                    if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0) {
                        for (PackageBean packageBean : order.orderPackage.packageBig) {
                            OrderDetailMultiBean o3 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION_WAIT_RECEIVE);
                            o3.state = packageBean.orderStatus;
                            o3.isCommend = packageBean.isComment;
                            o3.packageNum = "包裹" + packageBean.packageNum;
                            o3.remainingReceiveTime = packageBean.remainingReceiveTime;
                            multiList.add(o3);
                            for (OrderItemBean item : packageBean.skus) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(item.detail, OrderGoodsDetailBean.class);
                                OrderDetailMultiBean o4 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                                o4.pic = detailBean.pic;
                                o4.category = detailBean.category;
                                o4.name = detailBean.name;
                                o4.price = item.unitPriceY;
                                o4.count = item.count;
                                multiList.add(o4);
                            }
                            OrderDetailMultiBean o7 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_BOTTOM);
                            o7.state = packageBean.orderStatus;
                            o7.packageOrderNo = packageBean.orderNo;
                            o7.packageNum = "包裹" + packageBean.packageNum;
                            o7.isCommend = packageBean.isComment;
                            multiList.add(o7);
                        }
                    }
                    //2#添加待发货
                    if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                        OrderDetailMultiBean o8 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION);
                        o8.state = OrderStatus.WAIT_DELIVERY;
                        o8.packageNum = "未发货商品";
                        multiList.add(o8);
                        for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                            OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                            OrderDetailMultiBean o2 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                            o2.pic = detailBean.pic;
                            o2.category = detailBean.category;
                            o2.name = detailBean.name;
                            o2.price = unDelivery.unitPriceY;
                            o2.count = unDelivery.count;
                            multiList.add(o2);
                        }
                    }
                    break;
                case OrderStatus.FINISHED://已完成
                    //添加包裹
                    if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0) {
                        for (PackageBean packageBean : order.orderPackage.packageBig) {
                            OrderDetailMultiBean o5 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION);
                            o5.state = packageBean.orderStatus;
                            o5.isCommend = packageBean.isComment;
                            o5.packageNum = "包裹" + packageBean.packageNum;
                            multiList.add(o5);
                            for (OrderItemBean item : packageBean.skus) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(item.detail, OrderGoodsDetailBean.class);
                                OrderDetailMultiBean o6 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                                o6.pic = detailBean.pic;
                                o6.category = detailBean.category;
                                o6.name = detailBean.name;
                                o6.price = item.unitPriceY;
                                o6.count = item.count;
                                multiList.add(o6);
                            }
                            OrderDetailMultiBean o7 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_BOTTOM);
                            o7.state = packageBean.orderStatus;
                            o7.isCommend = packageBean.isComment;
                            o7.packageOrderNo = packageBean.orderNo;
                            multiList.add(o7);
                        }
                    }
                    break;
                case OrderStatus.CANCELED: //已关闭
                case OrderStatus.UN_APPROVE:
                case OrderStatus.CLOSED:
                case OrderStatus.WAIT_REFUND:
                    OrderDetailMultiBean o13 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION_VIEW);
                    multiList.add(o13);

                    if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0) {
                        for (PackageBean packageBean : order.orderPackage.packageBig) {
                            OrderDetailMultiBean o5 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION);
                            o5.state = packageBean.orderStatus;
                            o5.isCommend = packageBean.isComment;
                            o5.packageNum = "包裹" + packageBean.packageNum;
                            multiList.add(o5);
                            for (OrderItemBean item : packageBean.skus) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(item.detail, OrderGoodsDetailBean.class);
                                OrderDetailMultiBean o6 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                                o6.pic = detailBean.pic;
                                o6.category = detailBean.category;
                                o6.name = detailBean.name;
                                o6.price = item.unitPriceY;
                                o6.count = item.count;
                                multiList.add(o6);
                            }
                        }
                    }

                    if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                        for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                            OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                            OrderDetailMultiBean o5 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                            o5.pic = detailBean.pic;
                            o5.category = detailBean.category;
                            o5.name = detailBean.name;
                            o5.price = unDelivery.unitPriceY;
                            o5.count = unDelivery.count;
                            multiList.add(o5);
                        }
                    }
                    break;
            }
        }
        //添加素材
        addMaterialData(multiList, order);
        return multiList;
    }

    /**
     * 添加素材
     *
     * @param multiList 多布局数据
     * @param order     订单数据
     */
    private void addMaterialData(List<OrderDetailMultiBean> multiList, OrderBean order) {
        if (order.materialOrderDetailModels != null && order.materialOrderDetailModels.size() > 0) {
            OrderDetailMultiBean o1 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_SECTION_MATERIAL);
            multiList.add(o1);
            for (OrderMaterialBean materialBean : order.materialOrderDetailModels) {
                OrderDetailMultiBean o2 = new OrderDetailMultiBean(OrderDetailMultiBean.TYPE_ITEM);
                o2.isMaterial = true;
                o2.pic = materialBean.materialInfo.thumbnail;
                if (materialBean.designerInfo != null && materialBean.designerInfo.nickName != null) {
                    o2.category = "设计师：" + materialBean.designerInfo.nickName;
                } else {
                    o2.category = "设计师：" + GlobalVariable.UZAO_MATERIAL_NAME;
                }

                o2.name = materialBean.materialInfo.sourceMaterialName;
                o2.price = materialBean.materialInfo.countPriceY;
                //授权时长
                String periodUnit = "";
                if ("month".equals(materialBean.periodUnit)) {
                    periodUnit = "个月";
                } else if ("year".equals(materialBean.periodUnit)) {
                    periodUnit = "年";
                } else if ("forever".equals(materialBean.periodUnit)) {
                    periodUnit = "永久";
                    materialBean.authPeriod = "";
                }
                o2.time = "授权时长:    " + materialBean.authPeriod + periodUnit;
                multiList.add(o2);
            }
        }
    }
}
