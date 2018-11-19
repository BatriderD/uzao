package com.zhaotai.uzao.ui.search.mode;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Time: 2018/4/13
 * Created by LiYou
 * Description :
 */

public class TabEntity implements CustomTabEntity {

    public static final int TYPE_PRODUCT = 0X01;
    public static final int TYPE_MATERIAL = 0X02;
    public static final int TYPE_THEME = 0X03;
    public static final int TYPE_DESIGNER = 0X04;

    public TabEntity(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String title;
    public int type;

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
