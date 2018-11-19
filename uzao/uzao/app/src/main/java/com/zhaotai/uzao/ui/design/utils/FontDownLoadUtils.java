package com.zhaotai.uzao.ui.design.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.FileUtil;
import com.zhaotai.uzao.utils.network.DownloadAPI;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * description: 字体下载工具类
 * author : ZP
 * date: 2018/4/10 0010.
 */

public class FontDownLoadUtils {
    private ArrayList<String> fontList = new ArrayList<>();
    private static final String TAG = "FontDownLoadUtils";


    public void downLoadFont(Context mContext, @NonNull final String fileName, @NonNull final String fontName, @NonNull final FontDownLoadListener loadListener) {
        if (fontList.contains(fontName)) {
            loadListener.onReadyLoading();
            return;
        }
        fontList.add(fontName);
        final File fontFile = FileUtil.getFontFile(mContext, fileName);
        //下载地址
        String url = ApiConstants.DESIGN_FILE + fileName;

        new DownloadAPI(ApiConstants.UZAOCHINA_IMAGE_HOST_DESIGN).downloadFile(url, fontFile, new RxSubscriber<InputStream>(mContext) {

            @Override
            public void onComplete() {
                Log.d(TAG, "字体" + fontName + "下载成功:");
                fontList.remove(fontName);
                loadListener.onSuccess(fontFile);
            }

            @Override
            public void _onNext(InputStream inputStream) {

            }

            @Override
            public void _onError(String message) {
                loadListener.onError(message);
                fontFile.delete();
                fontList.remove(fontName);
            }
        });
    }


    public interface FontDownLoadListener {
        void onReadyLoading();

        void onSuccess(File file);

        void onError(String message);
    }
}