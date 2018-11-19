package com.zhaotai.uzao.app;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description :
 */

public class EventBusEvent {
    /**
     * 刷新地址界面
     */
    public static final String REFRESH_ADDRESS = "refresh_address";

    /**
     * 重新请求匿名token
     */
    public static final String REFRESH_ANONYMOUS = "refresh_anonymous";

    /**
     * 修改昵称
     */
    public static final String CHANGE_NICK_NAME = "change_nick_name";
    /**
     * 修改真实姓名
     */
    public static final String CHANGE_REAL_NAME = "change_real_name";
    /**
     * 修改职业
     */
    public static final String CHANGE_PROFESSION = "change_profession";
    /**
     * 修改我的足迹
     */
    public static final String CHANGE_TRACK_NUM = "change_track_num";
    /**
     * 修改职业
     */
    public static final String CHANGE_REGIN = "change_regin";
    /**
     * 修改头像
     */
    public static final String CHANGE_HEAD_IMAGE = "change_head_image";
    /**
     * 请求个人信息 和余额
     */
    public static final String REQUEST_PERSON_INFO = "request_person_info";
    /**
     * 请求个人信息
     */
    public static final String REQUEST_PERSON = "request_person";
    /**
     * 刷新收藏的商品
     */
    public static final String REFRESH_COLLECTION = "refresh_collection";
    /**
     * 刷新关注的设计师
     */
    public static final String REFRESH_ATTENTION = "refresh_attention";
    /**
     * 首页刷新关注的设计师
     */
    public static final String HOME_REFRESH_ATTENTION = "refresh_attention";
    /**
     * 刷新我的商品
     */
    public static final String REFRESH_PRODUCT = "refresh_product";
    /**
     * 刷新未读消息数量
     */
    public static final String REFRESH_UNREAD_COUNT = "refresh_unread_count";

    /**
     * 退出登录
     */
    public static final String LOG_OUT = "log_out";

    /**
     * 退出登录
     */
    public static final String HOME_FRAGMENT_DATA = "HomeFragmentData";

    /**
     * 退出登录
     */
    public static final String ReLoading = "ReLoading";
    /**
     * 增加一个贴纸
     */
    public static final String DESIGN_MATERIAL_STICKER = "designMaterialSticker";
    /**
     * 选择设计
     */
    public static final String SELECTED_DESIGN_WORK = "selected_design_work";

    /**
     * 3d页面 接收图片地址
     */
    public static final String RECEIVE_BLENDER_WORK_URL = "receive_blender_work_url";

    /**
     * 接收图片网络图片的地址
     */
    public static final String RECEIVE_NETWORK_PICTURE_URL = "receive_network_picture_url";
    /**
     * 接收图片网络图片的地址
     */
    public static final String RECEIVE_NETWORK_PICTURE_INFO = "receive_network_picture_info";

    /**
     * 接受白色背景透明化 图片地址
     */
    public static final String RECEIVE_WHITE_TO_ALPH_URL = "receive_white_to_alph_url";

    /**
     * 通知当前Sticker刷新
     */
    public static final String NOTIFIED_STICKER_CHANGED = "NOTIFIED_STICKER_CHANGED";

    /**
     * 关闭指定页面
     */
    public static final String CLOSE_SELECT_ACTIVITY = "close_select_activity";

    public static final String DESIGN_MATERIAL_COLOR_DATA = "design_material_color_data";
    /**
     * 设计模块上传截图文件成功的传递文件
     */
    public static final String UPLOAD_THMBNAIL = "UPLOAD_THMBNAIL";
    public static final String DESIGN_MATERIAL_FRAGMENT_INIT_FINISH = "DESIGN_MATERIAL_FRAGMENT_INIT_FINISH";
    public static final String DOWN_FONT_PROGERESS = "DOWN_FONT_PROGERESS";
    /**
     * 3dUV面设计完成结果传递
     */
    public static final String DESIGN_3D_UV_FINISH = "design_3d_uv_finish";
    public static final String SELECTED_TECHNOLOGY = "SELECTED_TECHNOLOGY";
    public static final String ADD_EDITOR_WORD = "add_editor_word";
    public static final String ADD_MATERIAL_BEAN = "ADD_MATERIAL_BEAN";
    public static final String CHANGE_ART_FONT = "change_art_font";
    public static final String CHOOSE_TAG_Person_Tag_FINISH = "CHOOSE_TAG_Person_Tag_FINISH";
    public static final String CHOOSE_TAG_SPU_TAG_FINISH = "choose_tag_spu_tag_finish";
    public static final String CHOOSE_TAG_THEME_TAG_FINISH = "choose_tag_theme_tag_finish";
    public static final String CHOOSE_INTRODUCE = "choose_introduce";
    public static final String ADD_MODULE_TO_THEME = "add_module_to_theme";
    public static final String SAVE_THEME_TEMPLATE_SUCCESS = "SAVE_THEME_TEMPLATE_SUCCESS";
    public static final String FONT_DOWN_LOAD_SUCCESS = "font_down_load_success";
    public static final String DEL_MESSAGE_CENTER = "del_message_center";
    public static final String EDITOR_HAS_CHANGED = "editor_has_changed";
    public static final String EDITOR_HAS_SAVED = "editor_has_saved";
    public static final String EDITOR_INIT_FAILED = "EDITOR_INIT_FAILED";
    public static final String REFRESH_MAIN_DATA = "REFRESH_MAIN_DATA";
}
