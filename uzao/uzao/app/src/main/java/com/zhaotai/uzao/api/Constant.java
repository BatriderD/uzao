package com.zhaotai.uzao.api;

/**
 * description: 网络请求常量
 * author : zp
 * date: 2017/7/13
 */

public class Constant {

    // 请求首页数据 的首页参数
    public static final int PAGING_HOME = 0;

    // 请求分页数据 和服务器返回的信息Current_page是否为第一页的比对  如果返回参数为1 则是第一页
    public static final int CURRENTPAGE_HOME = 1;

    //我的商品页面 每页显示多少条目的设置
    public static final int MY_PRODUCT_PAGE_PARAM = 15;
    //    消息详情分页数设置
    public static final int MESSAGE_DETAIL_PAGE_PARAM = 15;
    //    素材评论分页数
    public static final int MATERIAL_DISCUSS_PAGE_PARAM = 15;
}
