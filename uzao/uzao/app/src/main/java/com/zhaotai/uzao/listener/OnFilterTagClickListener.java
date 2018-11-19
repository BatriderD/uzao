package com.zhaotai.uzao.listener;

import java.util.Map;

/**
 * Time: 2018/4/13
 * Created by LiYou
 * Description : 筛选监听
 */

public interface OnFilterTagClickListener {

    /**
     * 标签点击
     *
     * @param params 标签数据
     */
    void onTagSelect(Map<String, String> params);

    /**
     * 重置
     */
    void reset();

    /**
     * 关闭筛选框
     */
    void closeDrawer();
}
