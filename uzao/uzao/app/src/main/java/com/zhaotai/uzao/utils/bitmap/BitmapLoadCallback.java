package com.zhaotai.uzao.utils.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;


/**
 * Time: 2017/11/17
 * Created by LiYou
 * Description :
 */

public interface BitmapLoadCallback {
    void onBitmapLoaded(@NonNull Bitmap bitmap, int sampleSize);

    void onFailure(@NonNull Exception bitmapWorkerException);
}
