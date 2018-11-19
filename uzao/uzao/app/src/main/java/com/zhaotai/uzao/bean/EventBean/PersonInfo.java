package com.zhaotai.uzao.bean.EventBean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 1.修改
 */

public class PersonInfo {

    public String code;
    public String nickName;
    public String realName;
    public int gender;
    public String birthDate;
    public String avatar;
    public String profession;
    public String province;
    public String city;
    public String region;
    public String provinceName;
    public String cityName;
    public String regionName;
    public String interestDesignSpuCount;//收藏的商品
    public String interestDesignerCount;//关注的设计师
    public String myDesignSpuCount;//我的商品
    public String myFootprintCount;//足迹
    public String aboutMe;    //简介
    public List<TagsBean> tags;    //标签


    public static class TagsBean implements Serializable {
        public String tagCode;
        public String tagName;

        public TagsBean(String tagCode) {
            this.tagCode = tagCode;
        }
    }

}
