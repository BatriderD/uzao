package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description: 模板所需的数据json对应的实体类
 * author : ZP
 * date: 2018/2/5 0005.
 */

public class TemplateJsonBean implements Serializable {

    //创建者
    public Creator creator;
    //主题的头信息
    public ThemeHeader themeHeader;
    //主题的内容信息
    public List<ThemeContent> themeContent;
    //主题创建者信息
    public static class Creator implements Serializable {
        //头像
        String avatar;
        //作者名称
        String nickName;

        public Creator(String avatar, String nickName) {
            this.avatar = avatar;
            this.nickName = nickName;
        }
    }

    //主题模块尼日欧诺个
    public static class ThemeContent implements Serializable {
        public String text;
        public List<Elem> elem;
    }

    //主题信息
    public static class ThemeHeader implements Serializable {
        //主题名称
        public String name;
        //主题介绍
        public String intro;
        //主题封面
        public String thumbnail;
        //主题关注数
        public String favoriteCounts;
        //主题评论数
        public String commentCounts;
        //主题标签
        public List<ThemeBean.TagsBean> tags;
    }

    //主题模块内容队形的数据
    public static class Elem implements Serializable {
        //id
        public String id;
        //类型
        public String type;
        //示例图片
        public String thumbnail;
        //名称
        public String name;
        //购买数
        public String buyCounts;
        //查看数
        public String viewCounts;
    }
}
