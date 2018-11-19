package com.zhaotai.uzao.ui.person.collection.model;

/**
 * Time: 2017/12/1
 * Created by LiYou
 * Description :
 */

public class CategoryCodeBean {

    public CategoryCodeBean(String CATEGORY_CODE, String CATEGORY_NAME) {
        this.CATEGORY_CODE = CATEGORY_CODE;
        this.CATEGORY_NAME = CATEGORY_NAME;
        this.code = CATEGORY_CODE;
        this.name = CATEGORY_NAME;
    }

    public CategoryCodeBean(String CATEGORY_CODE, String CATEGORY_NAME, boolean isSelect) {
        this.CATEGORY_CODE = CATEGORY_CODE;
        this.CATEGORY_NAME = CATEGORY_NAME;
        this.code = CATEGORY_CODE;
        this.name = CATEGORY_NAME;
        this.isSelect = isSelect;
    }

    public String CATEGORY_CODE;
    public String CATEGORY_NAME;
    public String name;
    public String code;
    public boolean isSelect;
}
