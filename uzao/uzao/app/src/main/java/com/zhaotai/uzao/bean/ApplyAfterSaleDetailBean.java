package com.zhaotai.uzao.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/6/15
 * Created by LiYou
 * Description : 申请售后详情
 */

public class ApplyAfterSaleDetailBean {
    public String description;
    public String applyDescription;
    public String handleOpinions;
    public String applyNo;
    public String orderNo;
    public String status;
    public String createTime;
    public String refundAmountY;//退款金额
    public String type;
    public ArrayList<String> images;
    public String hasHimSelf;
    public List<SpuInfo> afterSaleApplyDetailModels;
    public List<SpuInfo> productOrderDetailModels;
    public TransportBean publicUserTransportInfo;
    public List<TransportBean> internalTransportInfos;//平台物流信息

    public static class SpuInfo {
        public String category;
        public String name;
        public String pic;
        public String unitPrice;
        public String unitPriceY;
        public String count;
        public String orderDetail;
        public String sequenceNBR;
        public int availableSkuCount;
    }


}
