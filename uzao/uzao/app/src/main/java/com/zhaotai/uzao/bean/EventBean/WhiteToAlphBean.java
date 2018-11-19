package com.zhaotai.uzao.bean.EventBean;

/**
 * description: 白色背景透明化 返回页面
 * author : ZP
 * date: 2017/11/16 0016.
 */

public class WhiteToAlphBean {
    private String showPicPath;
    private boolean isAlph;

    public WhiteToAlphBean(String showPicPath, boolean isAlph) {
        this.showPicPath = showPicPath;
        this.isAlph = isAlph;
    }

    public String getShowPicPath() {
        return showPicPath;
    }

    public void setShowPicPath(String showPicPath) {
        this.showPicPath = showPicPath;
    }

    public boolean isAlph() {
        return isAlph;
    }

    public void setAlph(boolean alph) {
        isAlph = alph;
    }
}
