package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/6/5
 * Created by LiYou
 * Description :  售后订单
 */

public class AfterSalesBean implements Serializable {

    public List<SpuInfo> afterSaleApplyDetailModels;
    public String count;
    public String status;
    public String transportId;
    public String applyTime; //申请时间
    public String unitPrice;
    public String unitPriceY;
    public String type;   //退换货类型
    public String spuCode;//商品编号
    public String id;  //售后订单ID
    public String orderNo; //订单号
    public String countPrice;
    public String countPriceY;
    public String applyNo;//售后单号
    public String handleOpinions;//备注

    public static class SpuInfo implements Serializable {
        public String category;
        public String name;
        public String pic;
        public String unitPrice;
        public String count;
        public String orderDetail;
    }
}
