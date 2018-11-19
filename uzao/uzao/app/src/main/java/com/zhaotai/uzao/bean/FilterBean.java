package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 筛选
 */

public class FilterBean {
    public FilterBean() {

    }

    public FilterBean(String name) {
        this.name = name;
    }

    public FilterBean(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public boolean isSelected;
    public String name;
    public String tagName;
    public String code;
    public String id;
    public String tagCode;
}
