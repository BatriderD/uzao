package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/3/21 0021.
 */

public class MainTabBean {

    //子页面数据
    public String associateData;
    //    子页面数据
    public String associateType;
    //    锁状态
    public String lockStatus;
    //    名称
    public String navigateName;
    //    子code
    public String navigateCode;
    //    图标icon
    public String icon;
    //    孩子list
    public List<MainTabBean> children;
    //    是否有孩子
    public boolean hasChildren;

    public boolean isChecked;


}
