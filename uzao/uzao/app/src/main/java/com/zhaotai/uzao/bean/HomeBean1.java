package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/7/27
 * Created by zp
 * Description : 首页bean类
 */

public class HomeBean1 implements MultiItemEntity, Serializable {

    //    类型
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //   名称
    private String title;

    public HomeBean1(int type, String title) {
        this.type = type;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return type;
    }


    public final static int HOME_APP_HOME_NULL = 0;

    /**
     * 首页
     */
    //首页轮播条
    public final static int HOME_APP_HOME_BANNERS = 1;
    //个性定制
    public final static int HOME_APP_HOME_CUSTOMACTS = 2;
    //联名设计
    public final static int HOME_APP_HOME_DESIGNS = 3;
    //商城活动
    public final static int HOME_APP_HOME_PRODUCTACTS = 4;
    //热卖单品
    public final static int HOME_APP_PRODUCTHOTS = 5;


    //首页轮播条
    public List<HomeValuesBean> appHomebanners = new ArrayList<>();
    //个性定制
    public List<HomeValuesBean> appHomeCustomActs = new ArrayList<>();
    //联名设计
    public List<HomeValuesBean> appHomeDesigns = new ArrayList<>();
    //商城活动
    public List<HomeValuesBean> appHomeProductActs = new ArrayList<>();
    //热卖单品
    public List<AppHomeHotBean> appProductHots = new ArrayList<>();


    //listBean类
    public static class AppHomeHotBean implements MultiItemEntity, Serializable {

        //        类型
        public final static int HOME_APP_HOT_IMG = 1;
        public final static int HOME_APP_HOT_PRODUCTS1 = 2;
        public final static int HOME_APP_HOT_PRODUCTSS = 3;

        //        成员变量
        public HomeValuesBean bean1;

        public int type;

        public AppHomeHotBean(int type) {
            this.type = type;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }


    /**
     * 商城
     */
    //商城轮播
    public final static int HOME_APP_PRODUCT_BANNER = 1;
    //商城一级分类
    public final static int HOME_APP_PRODUCT_CATEGORYS = 6;
    //商城活动
    public final static int HOME_APP_PRODUCT_PRODUCTACTS = 4;
    //商城热卖
    public final static int HOME_APP_PRODUCT_HOT = 7;

    /**
     * 商城
     */
    // 一级分类列表
    public List<CategoryBean> appMallCategorys = new ArrayList<>();
    // 轮播复用首页轮播数据类型
    // 商城活动复用首页的活动

    //商城热卖
    public List<MallProductHotBean> appMallProductHots = new ArrayList<>();

    //listBean类
    public static class MallProductHotBean implements MultiItemEntity, Serializable {

        //        类型
        public final static int MALL_APP_HOT_IMG = 1;
        public final static int MALL_APP_HOT_PRODUCTS1 = 2;
        public final static int MALL_APP_HOT_PRODUCTSS = 3;

        //        成员变量
        public HomeValuesBean bean1;

        public int type;

        public MallProductHotBean(int type) {
            this.type = type;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }


    /**
     * zao
     */
    //联名设计
    public final static int HOME_APP_DESIGN_DESIGNS = 3;
    //热门商品
    public final static int HOME_APP_DESIGN_HOT = 8;

    //热卖单品
    public List<HomeZaoBean.ValuesBean> appZaoDesignHots = new ArrayList<>();

}
