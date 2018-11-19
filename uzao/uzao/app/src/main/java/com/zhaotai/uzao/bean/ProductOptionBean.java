package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 筛选
 */

public class ProductOptionBean {
    public String[] types;
    public String[] saleModes;
    public List<FilterBean> tags;
    public List<FilterBean> brand;
    public List<Cat> cat;
    public String categoryCode;

    public boolean openTags;
    public boolean openCat;
    public boolean openBrand;

    public class Cat {
        public boolean isSelected;
        public String code;
        public String name;
        public String count;
    }

    public static class FilterBean {
        public boolean isSelected;
        public String name;
        public String tagName;
        public String code;
        public String id;
        public String tagCode;
    }


}
