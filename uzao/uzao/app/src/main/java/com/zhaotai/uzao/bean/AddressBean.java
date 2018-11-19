package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * time:2017/4/17
 * description:  我的地址 实体类
 * author: LiYou
 */

public class AddressBean implements Serializable{

    public String addrAlias;
    public String addrDetail;
    public String city;
    public String cityName;
    public String description;
    public String extend1;
    public String extend2;
    public String extend3;
    public String isDefault;
    public String postcode;
    public String province;
    public String provinceName;
    public String recDate;
    public String recStatus;
    public String recUserId;
    public String recieverName;
    public String recieverPhone;
    public String region;
    public String regionName;
    public String sequenceNBR;
    public String userId;
    public int position;

    @Override
    public String toString() {
        return "AddressBean{" +
                "addrAlias='" + addrAlias + '\'' +
                ", addrDetail='" + addrDetail + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", description='" + description + '\'' +
                ", extend1='" + extend1 + '\'' +
                ", extend2='" + extend2 + '\'' +
                ", extend3='" + extend3 + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", postcode='" + postcode + '\'' +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", recDate='" + recDate + '\'' +
                ", recStatus='" + recStatus + '\'' +
                ", recUserId='" + recUserId + '\'' +
                ", recieverName='" + recieverName + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", sequenceNBR='" + sequenceNBR + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
