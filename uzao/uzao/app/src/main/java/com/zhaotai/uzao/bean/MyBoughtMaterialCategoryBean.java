package com.zhaotai.uzao.bean;

/**
 * description:
 * author : ZP
 * date: 2018/1/10 0010.
 */

public class MyBoughtMaterialCategoryBean {
    /**
     * name : 精灵
     * code : A
     */

    private String name;
    private String code;

    public MyBoughtMaterialCategoryBean(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
