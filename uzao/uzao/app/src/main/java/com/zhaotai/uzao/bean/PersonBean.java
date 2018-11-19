package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/10
 * Created by LiYou
 * Description : 个人信息
 */

public class PersonBean implements Serializable {
    public String aboutMe;
    public String avatar;
    public String background;
    public String birthDate;
    public String city;
    public String description;
    public String favoriteCount;
    public String interestCount;
    public String favnum;
    public String email;
    public String mobile;
    public int rewardCount;
    public String nickName;
    public String sequenceNBR;
    public String userId;
    public String userName;
    public String realName;
    public String provinceName;
    public String cityName;
    public String followCount;
    public String regionName;
    public String profession; //职业
    public String gender;  // 1男 0女
    public String spuCount;//我的商品
    public String myDesignCount;//我的作品
    public String myFootprintCount;//我的足迹
    public String unReadCount;//未读消息
    public String designerId;
    public String isPasswordAuthed;
    public List<TagBean> tags;    //标签

}
