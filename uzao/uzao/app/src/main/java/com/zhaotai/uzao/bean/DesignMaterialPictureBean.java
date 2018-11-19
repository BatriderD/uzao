package com.zhaotai.uzao.bean;

import android.graphics.Bitmap;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import puzzle.PuzzleLayout;

/**
 * description: 编辑载体形状adapter  bean类
 * author : zp
 * date: 2017/8/31
 */

public class DesignMaterialPictureBean implements MultiItemEntity {
    public static final int PICTURE_TYPE_SHAPE = 0;
    public static final int PICTURE_TYPE_PUZZLE = 1;


    private Bitmap orgBitmap;
    private int itemType;


    private PuzzleLayout puzzleLayout;

    public DesignMaterialPictureBean(int itemType, Bitmap orgBitmap) {
        this.orgBitmap = orgBitmap;
        this.itemType = itemType;
    }


    public DesignMaterialPictureBean(int itemType, PuzzleLayout puzzleBean) {
        this.itemType = itemType;
        this.puzzleLayout = puzzleBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }


    public Bitmap getOrgBitmap() {
        return orgBitmap;
    }

    public PuzzleLayout getPuzzleLayout() {
        return puzzleLayout;
    }

}
