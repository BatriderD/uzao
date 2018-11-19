package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description :素材
 */

public class MaterialListBean implements Serializable {
    public String id;
    public String materialCode;
    public String source;
    public String materialName;//素材名称
    public String sourceMaterialName;
    public String materialId;
    public String nickName;
    public String thumbnail;
    public String pic;//品牌主页 设计师主页 素材图片
    public String price;
    public String priceY;
    public String pyName;
    public String saleMode;
    public String status;
    public Boolean obtained;
    public String commentCount;//评论数
    public String viewCount;//浏览数
    public String salesCount;
    public String favoriteCount;//喜欢数
    public String sequenceNBR;
    public Data data;
    public String scale;
    public String fileMime;
    public ArrayList<TagBean> tags;
    public boolean isSelected;

    public class Data implements Serializable {
        public String height;
        public String nickName;
        public float resizeScale;
        public String width;
        public String userId;
        public String fileMime;
        public String avatar;
    }

    public class TagBean implements Serializable {
        public String name;
        public String code;
    }
}
