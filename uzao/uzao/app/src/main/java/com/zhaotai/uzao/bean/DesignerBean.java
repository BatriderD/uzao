package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * Time: 2018/3/7
 * Created by LiYou
 * Description :
 */

public class DesignerBean implements Serializable {
    public String nickName;
    public String userId;
    public String avatar;
    public int spuCount;
    public int materialCount;
    public int favoriteCount;
    public int followCount;
    public Data data;

    public static class Data {
        public String isFavorited;
    }
}
