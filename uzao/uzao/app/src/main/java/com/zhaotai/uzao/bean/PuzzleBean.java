package com.zhaotai.uzao.bean;

/**
 * Created by Administrator on 2017/7/6.
 */

public class PuzzleBean {
    private int type;
    private int piece_size;
    private int theme_id;

    public PuzzleBean() {
    }

    public PuzzleBean(int type, int piece_size, int theme_id) {
        this.type = type;
        this.piece_size = piece_size;
        this.theme_id = theme_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPiece_size() {
        return piece_size;
    }

    public void setPiece_size(int piece_size) {
        this.piece_size = piece_size;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    @Override
    public String toString() {
        return "PuzzleBean{" +
                "filterName=" + type +
                ", piece_size=" + piece_size +
                ", theme_id=" + theme_id +
                '}';
    }
}
