package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.CancelOrderAllEvent;
import com.zhaotai.uzao.bean.EventBean.CancelOrderEvent;
import com.zhaotai.uzao.bean.EventBean.CancelWaitApproveOrderEvent;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.OrderBean;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderItemBean;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.bean.OrderMultiBean;
import com.zhaotai.uzao.bean.PackageBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.global.CacheConstants;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.order.activity.MaterialPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.order.adapter.OrderMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.OrderContract;
import com.zhaotai.uzao.utils.ACache;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单操作类
 */

public class OrderPresenter extends OrderContract.Presenter {

    private OrderContract.View view;
    private OrderContract.WaitPayView waitPayView;
    private OrderContract.WaitApproveAndDeliveryView waitApproveAndDeliveryView;
    private OrderContract.WaitReceiveView waitReceiveView;
    private Gson gson = new Gson();

    public OrderPresenter(Context context, OrderContract.View view) {
        mContext = context;
        this.view = view;

    }


    public OrderPresenter(Context context, OrderContract.WaitPayView view) {
        mContext = context;
        waitPayView = view;
    }

    public OrderPresenter(Context context, OrderContract.WaitApproveAndDeliveryView view) {
        mContext = context;
        waitApproveAndDeliveryView = view;
    }

    public OrderPresenter(Context context, OrderContract.WaitReceiveView view) {
        mContext = context;
        waitReceiveView = view;
    }


    /**
     * 获取全部订单
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    @Override
    public void getAllOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getAllOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(final PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 待付款订单列表
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    @Override
    public void getWaitPayOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getWaitPayOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 待审核订单列表
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    @Override
    public void getWaitApproveOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getWaitApproveOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 待评价 订单列表
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    public void getWaitCommendOrder(final int start, final boolean isLoading) {
        Api.getDefault().getWaitCommendOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 待发货订单列表
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    @Override
    public void getWaitDeliveryOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getWaitDeliveryOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 待收货订单列表
     *
     * @param start     开始位置
     * @param isLoading 是否状态页loading
     */
    @Override
    public void getWaitReceiveOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getWaitReceiveOrderList(start)
                .compose(RxHandleResult.<PageInfo<OrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<OrderBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<OrderBean> orderBean) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showOrderList(orderBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 订单详情  - 待付款
     *
     * @param orderId 订单id
     */
    @Override
    public void getOrderDetail(String orderId) {
        Api.getDefault().getOrderDetail(orderId)
                .compose(RxHandleResult.<OrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<OrderDetailBean>(mContext, false) {
                    @Override
                    public void _onNext(OrderDetailBean orderDetailBean) {
                        waitPayView.showDetail(orderDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 订单详情  - 待审核 和带发货
     *
     * @param orderId 订单id
     */
    @Override
    public void getWaitApproveAndDeliveryOrderDetail(String orderId) {
        Api.getDefault().getOrderDetail(orderId)
                .compose(RxHandleResult.<OrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<OrderDetailBean>(mContext, false) {
                    @Override
                    public void _onNext(OrderDetailBean orderDetailBean) {
                        waitApproveAndDeliveryView.showDetail(orderDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 订单详情  - 待收货
     *
     * @param orderId 订单id
     */
    @Override
    public void getWaitReceiveOrderDetail(String orderId) {
        Api.getDefault().getOrderDetail(orderId)
                .compose(RxHandleResult.<OrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<OrderDetailBean>(mContext, false) {
                    @Override
                    public void _onNext(OrderDetailBean orderDetailBean) {
                        waitReceiveView.showDetail(orderDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 删除订单
     *
     * @param orderId 订单id
     */
    @Override
    public void deleteOrder(final String orderId, final BaseQuickAdapter adapter, final int position) {
        Api.getDefault().deleteOrder(orderId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        List<OrderMultiBean> data = adapter.getData();
                        Iterator<OrderMultiBean> iterator = data.iterator();
                        while (iterator.hasNext()) {
                            if (orderId.equals(iterator.next().orderNo)) {
                                iterator.remove();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除订单失败");
                    }
                });
    }

    /**
     * 取消订单
     *
     * @param orderId      订单id
     * @param cancelReason 取消原因
     */
    @Override
    public void cancelOrder(final String orderId, String cancelReason) {
        OrderGoodsBean info = new OrderGoodsBean();
        info.orderNo = orderId;
        info.cancelReason = cancelReason;
        Api.getDefault().cancelOrder(info)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("取消订单成功");
                        //通知 取消订单成功
                        EventBus.getDefault().post(new CancelOrderAllEvent(orderId));
                        if (waitPayView != null) {
                            waitPayView.cancelOrder();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("取消订单失败");
                    }
                });
    }

    /**
     * 待付款详情 -- 取消订单
     *
     * @param orderId      订单id
     * @param cancelReason 取消原因
     * @param position     item位置
     */
    @Override
    public void waitPayDetailCancelOrder(final String orderId, String cancelReason, final int position) {
        OrderGoodsBean info = new OrderGoodsBean();
        info.orderNo = orderId;
        info.cancelReason = cancelReason;
        Api.getDefault().cancelOrder(info)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        ToastUtil.showShort("取消订单成功");
                        EventBus.getDefault().post(new CancelOrderEvent(position));
                        EventBus.getDefault().post(new CancelOrderAllEvent(orderId));
                        if (waitPayView != null) {
                            waitPayView.cancelOrder();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("取消订单失败");
                    }
                });
    }


    /**
     * 确认收货
     *
     * @param packageOrderNo 包裹订单id
     */
    @Override
    public void confirmReceiveGoods(String packageOrderNo) {
        OrderItemBean info = new OrderItemBean();
        info.orderNo = packageOrderNo;
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
     * 倒计时
     *
     * @param remainingTime 剩余时间
     */
    @Override
    public void countDown(int remainingTime) {
        final int countTime = remainingTime;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(countTime)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        aLong = aLong * 1000;
                        return countTime - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ((BaseActivity) mContext).add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 0) {
                            ToastUtil.showShort("订单已关闭");
                            waitPayView.finishView();
                        } else {
                            String time = TimeUtils.millis2String(aLong, new SimpleDateFormat("mm分钟ss秒", Locale.getDefault()));
                            waitPayView.updateTime(time);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 倒计时-- 间隔分钟
     *
     * @param remainingTime 剩余时间
     */
    @Override
    public void countMinDown(int remainingTime) {
        final int countTime = remainingTime;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(countTime + 1000)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        aLong = aLong * 1000;
                        return countTime - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ((BaseActivity) mContext).add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 0) {
                            ToastUtil.showShort("已自动确认收货");
                            waitReceiveView.finishView();
                        } else {
                            String time = TimeUtils.millis2FitTimeSpan(aLong, 4);
                            waitReceiveView.updateTime(time);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 取消待审核订单
     *
     * @param orderId  订单Id
     * @param position item位置
     */
    public void cancelWaitApproveOrder(String orderId, final int position) {
        OrderGoodsBean info = new OrderGoodsBean();
        info.orderNo = orderId;
        Api.getDefault().cancelOrder(info)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        EventBus.getDefault().post(new CancelWaitApproveOrderEvent(position));
                        if (waitApproveAndDeliveryView != null) {
                            waitApproveAndDeliveryView.finishView();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("取消订单失败");
                    }
                });
    }

    public void gotoPayMaterial(OrderMultiAdapter adapter, ArrayList<String> orderIds) {
        List<MaterialDetailBean> detailList = new ArrayList<>();
        List<OrderMultiBean> data = adapter.getData();
        for (String orderNo : orderIds) {
            for (OrderMultiBean multi : data) {
                if (orderNo.equals(multi.orderNo) && multi.isMaterial && multi.getItemType() == OrderMultiBean.TYPE_ITEM_MATERIAL) {
                    MaterialDetailBean detailBean = new MaterialDetailBean();
                    detailBean.thumbnail = multi.pic;
                    detailBean.sourceMaterialName = multi.name;
                    detailBean.priceY = multi.price;
                    MaterialDetailBean.Designer designd = new MaterialDetailBean.Designer();
                    designd.nickName = multi.category;
                    detailBean.designer = designd;
                    detailBean.sequenceNBR = multi.materialId;
                    detailBean.saleMode = GlobalVariable.MATERIAL_MODE_CHARGE;//收费素材
                    detailList.add(detailBean);
                }
            }
        }
        //素材支付页面
        MaterialPayConfirmOrderActivity.launch(mContext, detailList, orderIds, true);
    }

    /**
     * 获取取消订单原因
     */
    public void getCancelReason() {
        //判断是否有缓存
        final ACache aCache = ACache.get(mContext);
        String json = aCache.getAsString(CacheConstants.CACHE_ORDER_CANCEL_REASON);
        if (StringUtil.isEmpty(json)) {
            Api.getDefault().getAllDictionary("orderClosedReason")
                    .compose(RxHandleResult.<List<DictionaryBean>>handleResult())
                    .subscribe(new RxSubscriber<List<DictionaryBean>>(mContext, true) {
                        @Override
                        public void _onNext(List<DictionaryBean> dictionaryBeen) {
                            String json = gson.toJson(dictionaryBeen);
                            //缓存订单取消原因
                            aCache.put(CacheConstants.CACHE_ORDER_CANCEL_REASON, json, ACache.TIME_DAY);
                            if (view != null) {
                                view.showCancelReason(dictionaryBeen);
                            }
                            if (waitPayView != null) {
                                waitPayView.showCancelReason(dictionaryBeen);
                            }
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        } else {
            List<DictionaryBean> dictionaryBeen = gson.fromJson(json, new TypeToken<List<DictionaryBean>>() {
            }.getType());
            if (view != null) {
                view.showCancelReason(dictionaryBeen);
            }
            if (waitPayView != null) {
                waitPayView.showCancelReason(dictionaryBeen);
            }
        }
    }

    /**
     * 添加 待收货以后 带底部布局的商品
     *
     * @param list  构造订单数据
     * @param order 订单数据
     */
    private void addPackageWithBottom(List<OrderMultiBean> list, OrderBean order) {
        // 添加包裹
        if (order.orderPackage != null && order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0)
            for (PackageBean packageBean : order.orderPackage.packageBig) {
                //包裹商品
                if (packageBean.skus != null && packageBean.skus.size() > 0) {
                    if (packageBean.skus.size() == 1) {
                        OrderGoodsDetailBean detailBean = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                        OrderMultiBean o5 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_SINGLE_WITH_BOTTOM);
                        o5.pic = detailBean.pic;
                        o5.category = detailBean.category;
                        o5.name = detailBean.name;
                        o5.price = packageBean.skus.get(0).unitPriceY;
                        o5.packageStatus = packageBean.orderStatus;
                        o5.isCommend = packageBean.isComment;
                        o5.packageOrderNo = packageBean.orderNo;
                        o5.packageNum = "包裹" + packageBean.packageNum;
                        o5.orderNo = order.orderNo;
                        list.add(o5);
                    } else if (packageBean.skus.size() == 2) {
                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                        OrderMultiBean o6 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL_WITH_BOTTOM);
                        o6.pic1 = detailBean1.pic;
                        o6.pic2 = detailBean2.pic;
                        o6.isCommend = packageBean.isComment;
                        o6.packageStatus = packageBean.orderStatus;
                        o6.packageOrderNo = packageBean.orderNo;
                        o6.packageNum = "包裹" + packageBean.packageNum;
                        o6.orderNo = order.orderNo;
                        list.add(o6);
                    } else {
                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                        OrderGoodsDetailBean detailBean3 = gson.fromJson(packageBean.skus.get(2).detail, OrderGoodsDetailBean.class);
                        OrderMultiBean o7 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL_WITH_BOTTOM);
                        o7.pic1 = detailBean1.pic;
                        o7.pic2 = detailBean2.pic;
                        o7.pic3 = detailBean3.pic;
                        o7.isCommend = packageBean.isComment;
                        o7.packageStatus = packageBean.orderStatus;
                        o7.packageNum = "包裹" + packageBean.packageNum;
                        o7.packageOrderNo = packageBean.orderNo;
                        o7.orderNo = order.orderNo;
                        list.add(o7);
                    }
                }
            }
    }

    /**
     * 添加 订单头
     *
     * @param list  构造订单数据
     * @param order 订单数据
     */
    private void addSectionOrderNum(List<OrderMultiBean> list, OrderBean order) {
        OrderMultiBean o1 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_ORDER_NUM);
        o1.orderNo = order.orderNo;
        if (OrderStatus.ORDER_TYPE_MATERIAL.equals(order.orderType)) {
            o1.isMaterial = true;
        }
        list.add(o1);
    }

    /**
     * 添加底部按钮布局
     */
    private void addBottom(List<OrderMultiBean> list, OrderBean order) {
        switch (order.orderStatus) {
            case OrderStatus.WAIT_PAY:
            case OrderStatus.CANCELED: //已关闭
            case OrderStatus.UN_APPROVE:
            case OrderStatus.CLOSED:
            case OrderStatus.WAIT_REFUND:
                OrderMultiBean o1 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_BOTTOM_PRICE);
                o1.orderNo = order.orderNo;
                o1.orderStatus = order.orderStatus;
                if (order.payAmount == null) {
                    o1.orderPrice = order.totalAmountY;
                } else {
                    o1.orderPrice = order.payAmountY;
                }
                list.add(o1);
                break;
            default:
                OrderMultiBean o2 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_BOTTOM);
                o2.orderNo = order.orderNo;
                o2.orderStatus = order.orderStatus;
                list.add(o2);
                break;
        }


    }

    /**
     * 构造订单列表数据 - 待收货
     *
     * @param orderList 订单数据
     */
    public List<OrderMultiBean> constructMultiDataWaitPay(List<OrderBean> orderList) {
        List<OrderMultiBean> list = new ArrayList<>();

        for (OrderBean order : orderList) {

            //添加订单头
            OrderMultiBean o1 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_WAIT_PAY);
            o1.orderNo = order.orderNo;
            if (OrderStatus.ORDER_TYPE_MATERIAL.equals(order.orderType)) {
                o1.isMaterial = true;
            }
            list.add(o1);

            //根据订单状态 判断添加顺序
            if (order.orderPackage != null) {
                if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                    for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                        OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                        OrderMultiBean o9 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                        o9.pic = detailBean.pic;
                        o9.category = detailBean.category;
                        o9.name = detailBean.name;
                        o9.price = unDelivery.unitPriceY;
                        o9.count = unDelivery.count;
                        o9.orderNo = order.orderNo;
                        list.add(o9);
                    }
                }
            }
            addMaterialData(list, order);
            addBottom(list, order);
            if (list.size() > 0) {
                list.get(list.size() - 1).isLast = true;
            }
        }
        dateIntercept(list, orderList);
        return list;
    }


    /**
     * 构造订单列表数据 - 待收货
     *
     * @param orderList 订单数据
     */
    public List<OrderMultiBean> constructMultiDataWaitReceive(List<OrderBean> orderList) {
        List<OrderMultiBean> list = new ArrayList<>();

        for (OrderBean order : orderList) {
            //添加订单头
            addSectionOrderNum(list, order);
            //添加包裹
            addPackageWithBottom(list, order);
            //添加未发货
            if (order.orderPackage != null && order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                OrderMultiBean o8 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_WAIT_DELIVERY);
                o8.orderNo = order.orderNo;
                list.add(o8);
                if (order.orderPackage.unDelivery.size() == 1) {
                    OrderGoodsDetailBean detailBean = gson.fromJson(order.orderPackage.unDelivery.get(0).detail, OrderGoodsDetailBean.class);
                    OrderMultiBean o5 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                    o5.pic = detailBean.pic;
                    o5.category = detailBean.category;
                    o5.name = detailBean.name;
                    o5.price = order.orderPackage.unDelivery.get(0).unitPriceY;
                    o5.count = order.orderPackage.unDelivery.get(0).count;
                    o5.orderNo = order.orderNo;
                    list.add(o5);
                } else if (order.orderPackage.unDelivery.size() == 2) {
                    OrderGoodsDetailBean detailBean1 = gson.fromJson(order.orderPackage.unDelivery.get(0).detail, OrderGoodsDetailBean.class);
                    OrderGoodsDetailBean detailBean2 = gson.fromJson(order.orderPackage.unDelivery.get(1).detail, OrderGoodsDetailBean.class);
                    OrderMultiBean o6 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                    o6.pic1 = detailBean1.pic;
                    o6.pic2 = detailBean2.pic;
                    o6.horizontalSize = "2";
                    o6.orderNo = order.orderNo;
                    list.add(o6);
                } else {
                    OrderGoodsDetailBean detailBean1 = gson.fromJson(order.orderPackage.unDelivery.get(0).detail, OrderGoodsDetailBean.class);
                    OrderGoodsDetailBean detailBean2 = gson.fromJson(order.orderPackage.unDelivery.get(1).detail, OrderGoodsDetailBean.class);
                    OrderGoodsDetailBean detailBean3 = gson.fromJson(order.orderPackage.unDelivery.get(2).detail, OrderGoodsDetailBean.class);
                    OrderMultiBean o7 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                    o7.pic1 = detailBean1.pic;
                    o7.pic2 = detailBean2.pic;
                    o7.pic3 = detailBean3.pic;
                    o7.horizontalSize = String.valueOf(order.orderPackage.unDelivery.size());
                    o7.orderNo = order.orderNo;
                    list.add(o7);
                }
            }
            //添加素材
            addMaterialData(list, order);
            if (list.size() > 0) {
                list.get(list.size() - 1).isLast = true;
            }
        }
        dateIntercept(list, orderList);
        return list;
    }


    /**
     * 构造订单列表数据
     */
    public List<OrderMultiBean> constructMultiData(List<OrderBean> orderList) {
        List<OrderMultiBean> list = new ArrayList<>();

        for (OrderBean order : orderList) {

            //添加订单头
            addSectionOrderNum(list, order);

            //根据订单状态 判断添加顺序
            if (order.orderPackage != null) {
                switch (order.orderStatus) {
                    case OrderStatus.WAIT_APPROVE://待审核
                        if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                            for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                                OrderMultiBean o9 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                o9.pic = detailBean.pic;
                                o9.category = detailBean.category;
                                o9.name = detailBean.name;
                                o9.price = unDelivery.unitPriceY;
                                o9.count = unDelivery.count;
                                o9.orderNo = order.orderNo;
                                list.add(o9);
                            }
                        }
                        break;
                    case OrderStatus.WAIT_PAY://待付款
                        if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                            for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                                OrderMultiBean o9 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                o9.pic = detailBean.pic;
                                o9.category = detailBean.category;
                                o9.name = detailBean.name;
                                o9.price = unDelivery.unitPriceY;
                                o9.count = unDelivery.count;
                                o9.orderNo = order.orderNo;
                                list.add(o9);
                            }
                        }
                        addMaterialData(list, order);
                        addBottom(list, order);
                        break;
                    case OrderStatus.WAIT_HANDLE://待发货
                    case OrderStatus.WAIT_DELIVERY:
                        if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                            //1# 先添加待发货
                            OrderMultiBean o2 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_WAIT_DELIVERY);
                            o2.orderNo = order.orderNo;
                            list.add(o2);
                            for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                                OrderMultiBean o3 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                o3.pic = detailBean.pic;
                                o3.category = detailBean.category;
                                o3.name = detailBean.name;
                                o3.price = unDelivery.unitPriceY;
                                o3.count = unDelivery.count;
                                o3.orderNo = order.orderNo;
                                list.add(o3);
                            }
                        }
                        //2# 添加包裹
                        if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0)
                            for (PackageBean packageBean : order.orderPackage.packageBig) {
                                //包裹头
                                OrderMultiBean o4 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_PACKAGE);
                                o4.packageStatus = packageBean.orderStatus;
                                o4.isCommend = packageBean.isComment;
                                o4.packageNum = "包裹" + packageBean.packageNum;
                                o4.orderNo = order.orderNo;
                                list.add(o4);
                                //包裹商品
                                if (packageBean.skus != null && packageBean.skus.size() > 0) {
                                    if (packageBean.skus.size() == 1) {
                                        OrderGoodsDetailBean detailBean = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o5 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                        o5.pic = detailBean.pic;
                                        o5.category = detailBean.category;
                                        o5.name = detailBean.name;
                                        o5.price = packageBean.skus.get(0).unitPriceY;
                                        o5.count = packageBean.skus.get(0).count;
                                        o5.orderNo = order.orderNo;
                                        list.add(o5);
                                    } else if (packageBean.skus.size() == 2) {
                                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o6 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                                        o6.pic1 = detailBean1.pic;
                                        o6.pic2 = detailBean2.pic;
                                        o6.horizontalSize = "2";
                                        o6.orderNo = order.orderNo;
                                        list.add(o6);
                                    } else {
                                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean3 = gson.fromJson(packageBean.skus.get(2).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o7 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                                        o7.pic1 = detailBean1.pic;
                                        o7.pic2 = detailBean2.pic;
                                        o7.pic3 = detailBean3.pic;
                                        o7.horizontalSize = String.valueOf(packageBean.skus.size());
                                        o7.orderNo = order.orderNo;
                                        list.add(o7);
                                    }
                                }
                            }
                        break;
                    case OrderStatus.WAIT_RECEIVE://待收货
                        addPackageWithBottom(list, order);
                        break;
                    case OrderStatus.FINISHED: //已完成
                        addPackageWithBottom(list, order);
                        break;
                    case OrderStatus.CANCELED: //已关闭
                    case OrderStatus.UN_APPROVE:
                    case OrderStatus.CLOSED:
                    case OrderStatus.WAIT_REFUND:
                        if (order.orderPackage.packageBig != null && order.orderPackage.packageBig.size() > 0) {
                            for (PackageBean packageBean : order.orderPackage.packageBig) {
                                //包裹头
                                OrderMultiBean o4 = new OrderMultiBean(OrderMultiBean.TYPE_SECTION_PACKAGE);
                                o4.packageStatus = packageBean.orderStatus;
                                o4.isCommend = packageBean.isComment;
                                o4.packageNum = "包裹" + packageBean.packageNum;
                                o4.orderNo = order.orderNo;
                                list.add(o4);
                                //包裹商品
                                if (packageBean.skus != null && packageBean.skus.size() > 0) {
                                    if (packageBean.skus.size() == 1) {
                                        OrderGoodsDetailBean detailBean = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o5 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                        o5.pic = detailBean.pic;
                                        o5.category = detailBean.category;
                                        o5.name = detailBean.name;
                                        o5.price = packageBean.skus.get(0).unitPriceY;
                                        o5.count = packageBean.skus.get(0).count;
                                        o5.orderNo = order.orderNo;
                                        list.add(o5);
                                    } else if (packageBean.skus.size() == 2) {
                                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o6 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                                        o6.pic1 = detailBean1.pic;
                                        o6.pic2 = detailBean2.pic;
                                        o6.horizontalSize = "2";
                                        o6.orderNo = order.orderNo;
                                        list.add(o6);
                                    } else {
                                        OrderGoodsDetailBean detailBean1 = gson.fromJson(packageBean.skus.get(0).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean2 = gson.fromJson(packageBean.skus.get(1).detail, OrderGoodsDetailBean.class);
                                        OrderGoodsDetailBean detailBean3 = gson.fromJson(packageBean.skus.get(2).detail, OrderGoodsDetailBean.class);
                                        OrderMultiBean o7 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_HORIZONTAL);
                                        o7.pic1 = detailBean1.pic;
                                        o7.pic2 = detailBean2.pic;
                                        o7.pic3 = detailBean3.pic;
                                        o7.horizontalSize = String.valueOf(packageBean.skus.size());
                                        o7.orderNo = order.orderNo;
                                        list.add(o7);
                                    }
                                }
                            }
                        }
                        if (order.orderPackage.unDelivery != null && order.orderPackage.unDelivery.size() > 0) {
                            for (OrderItemBean unDelivery : order.orderPackage.unDelivery) {
                                OrderGoodsDetailBean detailBean = gson.fromJson(unDelivery.detail, OrderGoodsDetailBean.class);
                                OrderMultiBean o8 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_VERTICAL);
                                o8.pic = detailBean.pic;
                                o8.category = detailBean.category;
                                o8.name = detailBean.name;
                                o8.price = unDelivery.unitPriceY;
                                o8.count = unDelivery.count;
                                o8.orderNo = order.orderNo;
                                list.add(o8);
                            }
                        }
                        addMaterialData(list, order);
                        addBottom(list, order);
                        break;
                }
            } else if (order.orderStatus.equals(OrderStatus.CLOSED) || order.orderStatus.equals(OrderStatus.WAIT_PAY)) {
                addMaterialData(list, order);
                addBottom(list, order);
            }

            //添加素材
            if (!order.orderStatus.equals(OrderStatus.WAIT_PAY) && !order.orderStatus.equals(OrderStatus.CLOSED)
                    && !order.orderStatus.equals(OrderStatus.CANCELED) && !order.orderStatus.equals(OrderStatus.UN_APPROVE)) {
                addMaterialData(list, order);
            }
            if (list.size() > 0) {
                list.get(list.size() - 1).isLast = true;
            }
        }
        dateIntercept(list, orderList);
        return list;
    }

    /**
     * 添加素材
     *
     * @param multiList 多布局数据
     * @param order     订单数据
     */
    private void addMaterialData(List<OrderMultiBean> multiList, OrderBean order) {
        if (order.materialOrderDetailModels != null && order.materialOrderDetailModels.size() > 0) {
            if (!order.orderStatus.equals(OrderStatus.WAIT_APPROVE)) {
                OrderMultiBean o1 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_GOODS_SECTION_MATERIAL);
                o1.orderNo = order.orderNo;
                o1.orderStatus = order.orderStatus;
                if (order.orderPackage == null) {
                    o1.isMaterial = true;
                }
                multiList.add(o1);
            }
            for (OrderMaterialBean materialBean : order.materialOrderDetailModels) {
                OrderMultiBean o2 = new OrderMultiBean(OrderMultiBean.TYPE_ITEM_MATERIAL);
                o2.pic = materialBean.materialInfo.thumbnail;
                if (materialBean.designerInfo != null && materialBean.designerInfo.nickName != null) {
                    o2.category = "设计师：" + materialBean.designerInfo.nickName;
                } else {
                    o2.category = "设计师：" + GlobalVariable.UZAO_MATERIAL_NAME;
                }
                o2.name = materialBean.materialInfo.sourceMaterialName;
                o2.price = materialBean.materialInfo.countPriceY;
                o2.orderNo = order.orderNo;
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
                if (order.orderPackage == null) {
                    o2.isMaterial = true;
                }
                multiList.add(o2);
            }
        }
    }

    /**
     * 给多布局数据统一添加 一些字段 如订单状态
     *
     * @param multiList 多布局数据
     * @param orderList 订单数据
     */
    private void dateIntercept(List<OrderMultiBean> multiList, List<OrderBean> orderList) {
        for (OrderMultiBean multi : multiList) {
            for (OrderBean order : orderList) {
                if (multi.orderNo.equals(order.orderNo)) {
                    //添加订单状态
                    multi.orderStatus = order.orderStatus;
                    //添加订单类型
                    multi.orderType = order.orderType;
                }
            }
        }
    }

}
