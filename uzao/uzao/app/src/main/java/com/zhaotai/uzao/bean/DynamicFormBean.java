package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description : 动态表单
 */

public class DynamicFormBean {
    public String groupCode;
    public String groupType;
    public List<DynamicValuesBean> values;
    public List<DynamicFormBean> children;
}