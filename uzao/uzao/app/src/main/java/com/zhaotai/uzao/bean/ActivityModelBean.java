package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/7/20
 * Created by LiYou
 * Description : 优惠活动实体类
 */

public class ActivityModelBean implements Serializable{

    public String icon;
    public String name;

    public List<FullCutConfigModels> fullcutConfigModels;

    public class FullCutConfigModels implements Serializable{
        public int cut;
        public int full;
    }
}
