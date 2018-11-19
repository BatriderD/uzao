package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/7/26
 * Created by LiYou
 * Description :
 */

public class PayOrderDetailBean {

    public String orderNo;
    public String orderStatus;
    public String orderType;

    public String receiverAddress;
    public String receiverMobile;
    public String receiverName;

    public String sequenceNBR;
    public String totalAmount;
    public String totalAmountY;
    public String userId;

    public List<OrderGoodsBean> orderDetailModels;
    public List<OrderMaterialBean> materialOrderDetailModels;

}
