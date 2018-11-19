package com.zhaotai.uzao.bean;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/6/2
 * Created by LiYou
 * Description : 订单中 商品列表
 */

public class OrderGoodsBean implements Serializable{

    public String orderNo;
    public String sequenceNBR;
    public String price;
    public String recDate;
    public String orderDetail;
    public String skuPic;
    public String count;
    public String countPriceY;
    public String countPrice;
    public String afterSaleStatus;
    public String parentOrderNo;
    public String designerId;
    public String designerName;
    public String orderStatus;
    public String status;
    public String totalPrice;
    public String unitPriceY;
    public String payAmount;
    public String payAmountY;
    public String totalAmountY;
    public String spuId;
    public String orderType;
    public String materialName;
    public String materialDesigner;
    public String materialThumbnail;
    public String materialPriceY;
    public String materialAuthPeriod;
    public String materialPeriodUnit;
    public int starScore;
    public String cancelReason;

    public String activityIcon;
    public String avtivityName;

    public List<Uri> imgList;//用于记录待评价图片
    public List<String> imgName;
    public List<File> imgFiles;
    public String comment;//评论

}
