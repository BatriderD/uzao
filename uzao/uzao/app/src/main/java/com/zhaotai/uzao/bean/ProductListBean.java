package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 带筛选的商品列表
 */

public class ProductListBean<T> {
    public PageInfo<T> page;
    public ProductOptionBean opt;
    public List<CheckedOpt> checkedOpt;
    public Category category;

    public static class Category {
        public String cat1;
        public String cat2;
        public String cat3;
    }

    public static class CheckedOpt {
        public String tn;
        public String tc;
        public List<Pps> pps;
    }

    public static class Pps {
        public String pc;
        public String pv;
    }
}
