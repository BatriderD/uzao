package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/12
 * Created by LiYou
 * Description :
 */

public class CategoryBean implements Serializable{


    public String agencyCode;
    public String categoryKeywords;
    public String categoryPy;
    public String categoryType;
    public String changedFields;
    public String description;
    public String extend1;
    public String extend2;
    public String extend3;
    public boolean hasChildren;
    public String icon;
    public Object lockDate;
    public String lockStatus;
    public String lockUserId;
    public Object orderNum;
    public Object recDate;
    public String recStatus;
    public String recUserId;
    public Object sequenceNBR;
    public List<?> categoryInfos;
    public String categoryCode;
    public String categoryName;
    public String parentCode;
    public boolean isChecked;
    public String levelNum;
    public List<CategoryBean> children;
    public List<CategoryBean> rightChildren;

    public boolean selected;
}
