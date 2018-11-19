package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.bean.ApplyAfterSaleDetailBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesDetailMultiBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.TransportBean;
import com.zhaotai.uzao.bean.post.AfterSalesGoodsInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.ApplyAfterSalesDetailContract;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/8/2 0002
 * Created by LiYou
 * Description :
 */
public class ApplyAfterSalesDetailPresenter extends ApplyAfterSalesDetailContract.Presenter {

    private ApplyAfterSalesDetailContract.View mView;
    private ApplyAfterSaleDetailBean data;

    public ApplyAfterSalesDetailPresenter(ApplyAfterSalesDetailContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getData(String applyNo) {
        Api.getDefault().myApplyAfterDetail(applyNo)
                .compose(RxHandleResult.<ApplyAfterSaleDetailBean>handleResult())
                .subscribe(new RxSubscriber<ApplyAfterSaleDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(ApplyAfterSaleDetailBean applyAfterSaleDetailBean) {
                        data = applyAfterSaleDetailBean;
                        constructorData(applyAfterSaleDetailBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    private void constructorData(ApplyAfterSaleDetailBean applyAfterSaleDetailBean) {
        Gson gson = new Gson();
        List<ApplyAfterSalesDetailMultiBean> multiList = new ArrayList<>();
        ApplyAfterSalesDetailMultiBean m1 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_SECTION_ORDER_NUM);
        m1.orderId = applyAfterSaleDetailBean.applyNo;
        m1.packageNum = getOrderStatus(applyAfterSaleDetailBean.status);
        multiList.add(m1);

        //添加包裹商品
        if (applyAfterSaleDetailBean.afterSaleApplyDetailModels != null) {
            for (ApplyAfterSaleDetailBean.SpuInfo orderItemBean : applyAfterSaleDetailBean.afterSaleApplyDetailModels) {
                OrderGoodsDetailBean detailBean = gson.fromJson(orderItemBean.orderDetail, OrderGoodsDetailBean.class);
                ApplyAfterSalesDetailMultiBean m2 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_GOODS);
                m2.pic = detailBean.pic;
                m2.category = detailBean.category;
                m2.name = detailBean.name;
                m2.price = DecimalUtil.getPrice(orderItemBean.unitPrice);
                m2.count = orderItemBean.count;
                multiList.add(m2);
            }
        }

        //添加申请内容
        ApplyAfterSalesDetailMultiBean m3 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_APPLY);
        m3.applyType = applyAfterSaleDetailBean.type;
        m3.reason = applyAfterSaleDetailBean.applyDescription;
        multiList.add(m3);

        if (applyAfterSaleDetailBean.images != null && applyAfterSaleDetailBean.images.size() > 0) {
            //添加图片
            ApplyAfterSalesDetailMultiBean m4 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_IMAGE);
            multiList.add(m4);
            for (int i = 0; i < applyAfterSaleDetailBean.images.size(); i++) {
                ApplyAfterSalesDetailMultiBean m5 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_IMAGE);
                m5.pic = applyAfterSaleDetailBean.images.get(i);
                m5.picList = applyAfterSaleDetailBean.images;
                m5.picPosition = i;
                multiList.add(m5);
            }
        }
        //添加用户物流信息
        if (applyAfterSaleDetailBean.publicUserTransportInfo != null) {
            ApplyAfterSalesDetailMultiBean m6 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_TRANSPORT);
            ApplyAfterSalesDetailMultiBean m7 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_TRANSPORT);
            if (applyAfterSaleDetailBean.internalTransportInfos != null && !applyAfterSaleDetailBean.internalTransportInfos.isEmpty()) {
                m6.sectionTransportName = "用户换货物流信息";
                m7.platformTransport = true;
            } else {
                m6.sectionTransportName = "退货物流信息";
                m7.platformTransport = false;
            }
            multiList.add(m6);
            m7.transportName = applyAfterSaleDetailBean.publicUserTransportInfo.transportName;
            m7.transportNo = applyAfterSaleDetailBean.publicUserTransportInfo.transportNo;
            multiList.add(m7);
        }
        //添加平台物流信息
        if (applyAfterSaleDetailBean.internalTransportInfos != null && applyAfterSaleDetailBean.internalTransportInfos.size() > 0) {
            ApplyAfterSalesDetailMultiBean m8 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_SECTION_TRANSPORT);
            m8.platformTransport = true;
            m8.sectionTransportName = "平台换货物流信息";
            multiList.add(m8);
            for (TransportBean transportBean : applyAfterSaleDetailBean.internalTransportInfos) {
                ApplyAfterSalesDetailMultiBean m9 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_TRANSPORT);
                m9.transportName = transportBean.transportName;
                m9.transportNo = transportBean.transportNo;
                multiList.add(m9);
            }
        }
        if ("reject".equals(applyAfterSaleDetailBean.status)) {
            ApplyAfterSalesDetailMultiBean m10 = new ApplyAfterSalesDetailMultiBean(ApplyAfterSalesDetailMultiBean.TYPE_ITEM_REJECT_REASON);
            m10.rejectReason = applyAfterSaleDetailBean.handleOpinions;
            multiList.add(m10);
        }
        mView.showData(multiList);
    }


    /**
     * 添加发货物流
     *
     * @param transportCompany 物流公司
     * @param transportNo      物流单号
     */
    public void applyTransport(String transportCompany, String transportNo) {
        if (StringUtil.isEmpty(transportCompany)) {
            ToastUtil.showShort("请选择物流公司");
            return;
        }
        if (StringUtil.isEmpty(transportNo)) {
            ToastUtil.showShort("请填写物流单号");
            return;
        }
        if (data != null) {
            AfterSalesGoodsInfo info = new AfterSalesGoodsInfo();
            info.sequenceNBR = data.applyNo;
            info.transportCompany = transportCompany;
            info.transportNo = transportNo;
            Api.getDefault().customerDelivery(data.type.toLowerCase(), info)
                    .compose(RxHandleResult.<AfterSalesBean>handleResult())
                    .subscribe(new RxSubscriber<AfterSalesBean>(mContext, true) {
                        @Override
                        public void _onNext(AfterSalesBean s) {
                            ToastUtil.showShort("添加物流成功");
                            mView.finishView();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("添加物流失败");
                        }
                    });
        }
    }

    /**
     * 获取订单状态
     */
    private String getOrderStatus(String status) {
        switch (status) {
            case "returned":
                return "已退款";
            case "waitConfirm":
                return "待处理";
            case "confirming":
                return "处理中";
            case "waitCustDeliver":
                return "等待退货";
            case "reject":
                return "售后失败";
            case "replace":
                return "已换货";
            default:
                return "处理中";
        }
    }
}
