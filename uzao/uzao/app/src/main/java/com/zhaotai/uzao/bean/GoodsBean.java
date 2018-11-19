package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 商品列表实体类
 */

public class GoodsBean implements Serializable {
    public boolean isSelected;//是否选择
    public String categoryCode;
    public String designIdea;
    public String designerId;
    public String activityIcon;//活动图标
    public boolean activity;
    public String activityName;
    public String id;
    public String mkuId;
    public String customizeType;
    public String price;
    public String priceY;//商品价格
    public String salesPriceY;//售卖价格 上架时用到
    public String displayPriceY;//商品价格
    public String displayPrice;//商品价格
    public String pic;
    public String image;
    public String spuCode;
    public String describe;
    public String description;
    public String spuName;//商品名
    public String thumbnail;
    public String thumbanil;//在我的商品中 用到
    public String isTemplate;//是否模板商品 在我的商品中 用到
    public String spuId;
    public String status;
    public String spuType;
    public String nickName;
    public String sequenceNBR;
    public String approveIdea;
    public String viewCount;
    public List<String> spuImages;
    public String salesCount;
    public GoodsBean spuModel;
    public ResultData data;
    public List<FilterBean> tags;//标签

    public class ResultData implements Serializable {

        public String hasGoingActivity;//是否参加活动
        public String nickName;//设计师名字
        public String avatar;//设计师头像
        public String userId;

        public String activityIcon;
        public String activityName;
    }
}
