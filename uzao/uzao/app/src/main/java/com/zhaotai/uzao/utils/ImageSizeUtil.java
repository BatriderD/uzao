package com.zhaotai.uzao.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Time: 2018/2/26
 * Created by LiYou
 * Description : 指定图片加载大小
 */

public class ImageSizeUtil {

    public static String changeImageSize(String path) {
        return changeImageSize(path, ImageSize.X200);
    }

    public static String changeImageSize(String path, @ImageSize String size) {
        if (path == null) {
            return "";
        }
        String[] sp = path.split("\\.");
        if (sp.length > 2) {
            return sp[0] + size + sp[1];
        } else {
            return path;
        }
    }

    @StringDef(value = {
            ImageSize.X50, ImageSize.X200
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageSize {
        String X50 = "_200x200.";
        String X200 = "_50x50.";
    }
}
