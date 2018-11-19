package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/4/28
 * Created by LiYou
 * Description : 检查模板商品数据
 */

public class ValidateProductBean implements Serializable {

    public List<Content> wordart;//艺术字 字体
    public Material material;//素材

    public ValidateProductBean() {
        this.wordart = new ArrayList<>();
        this.material = new Material();
        material.expiredPublished = new ArrayList<>();
        material.notAvailable = new ArrayList<>();
    }

    public class Material implements Serializable {
        public List<Content> notAvailable;//过期 且 下架
        public List<Content> expiredPublished;//过期 没下架
    }

    public static class Content implements Serializable {
        public Content(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String thumbnail;
        public String id;
        public String name;
        public String sourceMaterialName;

        public Content() {
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Content){
                Content content = (Content) obj;
                return this.id.equals(content.id) && this.name.equals(content.name) && this.sourceMaterialName.equals(content.sourceMaterialName);
            }else {
                return false;
            }
        }
    }
}
