package com.zhaotai.uzao.global;

/**
 * description:全局变量
 * author : zp
 * date: 2017/7/18
 */

public class GlobalVariable {
    public static final String ANDROID = "移动端";

    public static final String BASE_URL = "base_url";

    public static final String BASE_IMAGE_URL = "base_image_url";

    public static final String BASE_DESIGN_URL = "base_design_url";

    public static final String UZAO_MATERIAL_NAME = "优造中国";

    public static final int DESIGN_WIDTH = 360;
    /**
     * 注册登录模块
     */
    // 页面类别
    public static final String PAGE_TYPE = "PAGE_TYPE";
    //  注册页面
    public static final String REGISTER_TYPE = "REGISTER_TYPE";
    //  找回密码页面
    public static final String FINDPAS_TYPE = "FINDPSD_TYPE";
    //密码最长
    public static final int PSD_MAX = 20;
    //密码最短
    public static final int PSD_MIN = 6;
    //    三方登录渠道
    public static final String CHANNEL_QQ = "qq";
    public static final String CHANNEL_SINA = "weibo";
    public static final String CHANNEL_WEICHAT = "wechat";
    /**
     * 消息模块
     */
    // 页面类别
    public static final String MESSAGE_PAGE_TYPE = "PAGE_TYPE";
    //    站内消息NOTICEMESSAGE
    public static final String SYSTEM_MESSAGE = "notificationMessages";
    //    推送消息
    public static final String NOTIFICATION_EMESSAGE = "systemMessages";
    //    评论消息
    public static final String COMMENT_MESSAGE = "sourceMaterialMessages";
    /**
     * 优惠券类型
     */
    public static final int COUPON_UNUSED = 0;
    public static final int COUPON_USED = 1;
    public static final int COUPON_INVALID = 2;
    /**
     * 历史搜索
     */
    public static final String HISTORY_SEARCH = "history_search";
    /**
     * 素材历史搜索
     */
    public static final String HISTORY_SEARCH_MATERIAL = "history_search_material";

    /**
     * 流行词历史搜索
     */
    public static final String HISTORY_CATCH_WORD_SEARCH = "history_Catch_Word_Search";
    /**
     * 我的素材搜索历史
     */
    public static final String HISTORY_MY_MATERIAL_SEARCH = "history_My_Material_Search";

    /**
     * 个人模块
     */
    public static final String PERSONINFO = "PersonInfo";
    /**
     * 设计师模块
     */
    public static final String DESIGNERID = "designerId";
    /**
     * 主页 商品分类主分类
     */
    public static final String ROOTCATEGORYLIST = "rootcategorylist";
    /**
     * 自定义裁剪
     */
    public static final int MAX_WIDTH = 1024;
    public static final int MAX_HEIGHT = 1024;
    public static final int CROP_MAX_SIZE = 2000;
    /**
     * 编辑器
     */
    public static final int STANDARD_SIZE = 1000;
    public static final int MINSIZE = 100;

    /**
     * swipeVIew
     */
    public static final int SWIPE_DELAYED = 0;//延迟时间
    public static final int SWIPE_DURATION = 3;//持续时间
    public static final int SWIPE_DRAG_RATE = 2;//下拉比例
    /**
     * 启动设时间
     */
    public static long SPLASH_COUNT = 3;

    /**
     * 编辑器最大步数
     */
    public static int MAX_STEP = 5;
    public static String UNLOADDESIGNPICS = "unload_design_pics";
    public static String MODEL_2D = "2d";
    public static String MODEL_3D = "3d";
    public static int MAX_STICKER_COUNT = 10;

    /**
     * 素材费用模式
     */
    public static final String MATERIAL_MODE_CHARGE = "charge";
    public static final String MATERIAL_MODE_FREE = "free";//免费
    public static final String MATERIAL_MODE_FREE_CHARGE = "freeCharge";//免费且可以打赏

    public static String FIRST_DESIGN = "first_design";
    public static int SEARCH_CATCH_WORD_TYPE = 1;

    /**
     * 协议
     */
    //优造中国用户注册服务协议
    public static final String PROTOCOL_REGISTER = "REGPORTOCOL";
    //版权协议
    public static final String PROTOCOL_COPYRIGHT = "COPYRIGHTPORTOCOL";
    //优惠券说明
    public static final String PROTOCOL_DISCOUNT = "COUPONPROTOCAL";
    //素材使用方式
    public static final String PROTOCOL_MATERIAL_USE = "MATERIALUSEPROTOCOL";
    //邀请返利说明
    public static final String PROTOCOL_REBATE = "REBATEPROTOCOL";
    //默认字体
    public static final String DEFAULT_FONT_FILENAME = "default_font_path";
    public static final String DEFAULT_FONT_NAME = "default_font_name";
    //主题最大字数
    public static int THEME_ABOUT_ME_SIZE = 200;
    public static final String THEME_DEFAULT_THEME_PIC = "u2/M00/01/FC/ooYBAFq8nFSAFUsJAAQnVp8IraE012.jpg";

    public static String DEFAULT_HEAD_PIC = "u2/M00/02/0C/ooYBAFq81pqAChY9AAAgPRFHuQ4766.jpg";
}
