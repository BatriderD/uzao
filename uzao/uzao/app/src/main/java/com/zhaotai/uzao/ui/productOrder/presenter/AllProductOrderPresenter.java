package com.zhaotai.uzao.ui.productOrder.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.produceOrder.AllSampleProduceReceiveEvent;
import com.zhaotai.uzao.bean.EventBean.produceOrder.WaitHandleSampleProduceReceiveEvent;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProductOrderBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.productOrder.contract.ProductOrderContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Time: 2017/8/22
 * Created by LiYou
 * Description :  全部生产订单
 */

public class AllProductOrderPresenter extends ProductOrderContract.AllPresenter {

    private ProductOrderContract.AllView view;

    public AllProductOrderPresenter(Context context, ProductOrderContract.AllView view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取全部生产订单
     */
    @Override
    public void getAllProductOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getProductOrderList(start,"")
                .compose(RxHandleResult.<PageInfo<ProductOrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ProductOrderBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ProductOrderBean> orderBeanPageInfo) {

                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showAllProductOrderList(orderBeanPageInfo);
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
     * 获取待处理生产订单
     */
    @Override
    public void getWaitHandleProductOrderList(final int start, final boolean isLoading) {
        Api.getDefault().getProductOrderList(start,"pending")
                .compose(RxHandleResult.<PageInfo<ProductOrderBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ProductOrderBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ProductOrderBean> orderBeanPageInfo) {

                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showAllProductOrderList(orderBeanPageInfo);
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
     * 确认收货 -- 样品的确认收货
     * @param orderNo 订单id
     * @param type      打样 大货样
     * @param position  adapter位置
     * @param orderStatus   订单状态
     * @param source   true 全部订单
     */
    @Override
    public void receiveSampleProduct(String orderNo, String type, final int position, final boolean source, final String orderStatus) {
        Api.getDefault().receiveSampleProduce(orderNo,type)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext) {
                    @Override
                    public void _onNext(Object s) {
                        if(source){//全部订单
                            AllSampleProduceReceiveEvent event = new AllSampleProduceReceiveEvent(position,orderStatus);
                            EventBus.getDefault().post(event);
                        }else {//待处理
                            WaitHandleSampleProduceReceiveEvent event = new WaitHandleSampleProduceReceiveEvent(position,orderStatus);
                            EventBus.getDefault().post(event);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 确认收货 -- 样品的确认收货
     * @param orderNo 订单id
     * @param position  adapter位置
     * @param orderStatus   订单状态
     * @param source   true 全部订单
     */
    @Override
    public void receiveProduct(String orderNo, final int position, final boolean source, final String orderStatus) {
        Api.getDefault().receiveProduce(orderNo)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext) {
                    @Override
                    public void _onNext(Object s) {
                        if(source){//全部订单
                            AllSampleProduceReceiveEvent event = new AllSampleProduceReceiveEvent(position,orderStatus);
                            EventBus.getDefault().post(event);
                        }else {//待处理
                            WaitHandleSampleProduceReceiveEvent event = new WaitHandleSampleProduceReceiveEvent(position,orderStatus);
                            EventBus.getDefault().post(event);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

}
