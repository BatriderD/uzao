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

public class HomeFragmentBean implements MultiItemEntity, Serializable {


    public HomeFragmentBean(int type) {
        this.type = type;
//        initData();
    }

//    //造假数据
//    private void initData() {
//
//    }

    public final static int HOME_FRAGMENT_BANDER_FIRST = 1;
    public final static int HOME_FRAGMENT_BANDER_SECOND = 2;
    public final static int HOME_FRAGMENT_BANDER_THIRD = 3;
    public final static int HOME_FRAGMENT_BANDER_FOURTH = 4;
    public final static int HOME_FRAGMENT_BANDER_FIFTH = 5;
    public final static int HOME_FRAGMENT_COUNT_LIST_FIRST = 11;
    public final static int HOME_FRAGMENT_COUNT_LIST_SECOND = 12;

    //大标题
    public String title;
    //    类型
    public int type;

    public List<HomeFragmentBanderFirstBean> banderFirstList = new ArrayList<>();
    public List<HomeFragmentBanderSecondBean> banderSecoundList = new ArrayList<>();
    public List<HomeFragmentBanderThirdBean> banderThirdList = new ArrayList<>();
    public List<HomeFragmentBanderFourthBean> bannerFourthList = new ArrayList<>();
    public List<HomeFragmentBanderFifthBean> bannerFifthList = new ArrayList<>();

    public List<CategoryBean> myFifthList;
    public List<HomeFragmentListFirstBean> listFirstBeans = new ArrayList<>();
    public List<HomeFragmentListSecondBean> listSecondBeans = new ArrayList<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getItemType() {
        return type;
    }


    //第一个轮播图的bean类
    public static class HomeFragmentBanderFirstBean {
        public HomeFragmentBanderFirstBean(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String imageUrl;
    }

    //第二个轮播图的bean类
    public static class HomeFragmentBanderSecondBean {
        public String imageUrl;
        public String title;

        public HomeFragmentBanderSecondBean(String imageUrl, String title) {
            this.imageUrl = imageUrl;
            this.title = title;
        }
    }

    //    第三个轮播图的bean类
    public static class HomeFragmentBanderThirdBean {
        public String brand;
        public String name;
        public List<PicBean> beans = new ArrayList<>();

        public static class PicBean {
            public PicBean(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String imageUrl;
        }
    }

    //第四个轮播图的bean类
    public static class HomeFragmentBanderFourthBean {

        public HomeFragmentBanderFourthBean(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String imageUrl;

    }

    //listBean类
    public static class HomeFragmentListFirstBean implements MultiItemEntity, Serializable {

        //        类型
        public final static int HOME_FRAGMENT_LIST_FIRST_BEAN_TYPE1 = 1;
        public final static int HOME_FRAGMENT_LIST_FIRST_BEAN_TYPE2 = 2;
        public final static int HOME_FRAGMENT_LIST_FIRST_BEAN_TYPE3 = 3;

        //        成员变量
        public Type1Bean bean1;
        public Type2Bean bean2;

        public int type;

        public HomeFragmentListFirstBean(int type) {
            this.type = type;
        }

        @Override
        public int getItemType() {
            return type;
        }

        public static class Type1Bean {
            public String imageUrl;
        }

        public static class Type2Bean {
            public String picProduct;
            public String saleTag;
            public String title;
            public String description;
            public String productTag;
            public String money;
        }
    }

    public static class HomeFragmentBanderFifthBean {
        public HomeFragmentBanderFifthBean(String iconUrl, String name) {
            this.iconUrl = iconUrl;
            this.name = name;
        }

        public String iconUrl;
        public String name;
    }

    public static class HomeFragmentListSecondBean {


            public ADItem ad;
            public List<ProductITem> items;

            public static class ADItem {
                public String name;
                public String imgUrl;
                //                标签
                public List<String> tags;
                public String money;

                public ADItem(String name, String imgUrl, List<String> tags, String money) {
                    this.name = name;
                    this.imgUrl = imgUrl;
                    this.tags = tags;
                    this.money = money;

                }
            }

            public static class ProductITem {
                public ProductITem(String name, String imgUrl, String money) {
                    this.name = name;
                    this.imgUrl = imgUrl;
                    this.money = money;
                }

                public String name;
                public String imgUrl;
                public String money;

            }
        }

}
