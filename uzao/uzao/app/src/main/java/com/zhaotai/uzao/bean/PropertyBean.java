package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description : 属性
 */

public class PropertyBean implements Serializable{

    public String propertyTypeCode;
    public String propertyTypeName;
    public String propertyValue;
    public String propertyCode;
    public String isSku;
    public List<PropertyBean> spuProperties;
    //是否选择
    public boolean isSelect;
}
