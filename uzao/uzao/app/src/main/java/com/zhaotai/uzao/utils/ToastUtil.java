package com.zhaotai.uzao.utils;

import android.widget.Toast;

import com.zhaotai.uzao.app.MyApplication;


/**
 * time:2017/4/7
 * description: Toast统一管理类
 * author: LiYou
 */

public class ToastUtil {
    private static Toast toast;

    private static Toast initToast(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getAppContext(),message,duration);
        }else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message 显示内容
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message 显示内容
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }
}
