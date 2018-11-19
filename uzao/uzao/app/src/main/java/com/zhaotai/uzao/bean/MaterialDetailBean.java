package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材详情
 */

public class MaterialDetailBean implements Serializable {
    public DesignerBean assignDesigner;
    public String categoryNames;//分类
    public String designIdea;//设计理念
    public Designer designer;//设计师
    public String designerId;//设计师
    public String fileRefer;//图片源文件
    public String price;//
    public String priceY;//价格
    public String brandId;
    public String brandName;
    public String saleMode;//收费模式
    public String sourceMaterialName;//名称
    public String sourceMaterialId;//id
    public String sourceMaterialCode;//code
    public String thumbnail;//图片缩略图
    public String favoriteCount;//收藏数
    public String upvoteCount;//点赞数
    public String useCount;//使用数
    public String viewCount;//浏览数
    public String commentCount;//评论数
    public int rewardCount;//打赏次数
    public String salesCount;//销售数
    public String authPeriod;//授权时长
    public String periodUnit;//授权时长单位
    public String sequenceNBR;
    public boolean obtained;//是否获取
    public List<TagBean> tags;
    public List<MaterialDetailBean> sourceMaterialModels;
    public String totalAmountY;
    public String status;
    public String userId;
    public String scale;
    public String fileMime;

    public static class Designer implements Serializable{
        public String nickName;
        public String avatar;
    }

    public Data data;

    public static class Data {
        public String height;
        public String nickName;
        public float resizeScale;
        public String width;
        public String userId;
        public String fileMime;
        public String avatar;

    }

}
