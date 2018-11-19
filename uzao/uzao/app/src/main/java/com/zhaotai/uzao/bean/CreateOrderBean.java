package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/6/2
 * Created by LiYou
 * Description : 创建订单
 */

public class CreateOrderBean {

    public List<ShoppingGoodsBean> orderDetailModels;
    public List<MaterialListBean> materialOrderDetailModels;

    public String orderWay;//cart 购物车 butNow 立即购买
    public String receiptDetail; //发票信息
    public String receiverAddress; //地址
    public String receiverMobile; //电话
    public String receiverName; //收货人
    public OrderInvoiceBean orderInvoiceModel;
}
