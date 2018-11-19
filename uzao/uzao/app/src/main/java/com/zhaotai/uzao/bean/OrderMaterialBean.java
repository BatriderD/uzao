package com.zhaotai.uzao.bean;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 订单素材
 */

public class OrderMaterialBean {

    public String orderNo;
    public String sequenceNBR;
    public String authPeriod;
    public String periodUnit;
    public MaterialInfo materialInfo;
    public Designer designerInfo;

    public class MaterialInfo {
        public String thumbnail;
        public String priceY;
        public String sourceMaterialName;
        public String sourceMaterialId;
        public String countPriceY;
    }

    public class Designer {
        public String userName;
        public String nickName;
        public String avatar;
    }
}
