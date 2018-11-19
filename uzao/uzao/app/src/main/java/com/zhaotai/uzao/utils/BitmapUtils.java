package com.zhaotai.uzao.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.zhaotai.uzao.widget.LoadingDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description: Bitmap 工具类
 * author : zp
 * date: 2017/7/25
 */

public class BitmapUtils {

    public static final String TAG = "BitmapUtils";

    public static Bitmap changeColor(Bitmap baseBitmap, int red, int green, int blue) {
        Bitmap afterBitmap = Bitmap.createBitmap(baseBitmap.getWidth(),
                baseBitmap.getHeight(), baseBitmap.getConfig());
        Canvas canvas = new Canvas(afterBitmap);
        Paint paint = new Paint();
        // 根据SeekBar定义RGBA的矩阵, 通过修改矩阵第五列颜色的偏移量改变图片的颜色
        float[] src = new float[]{
                red / 128.0f, 0, 0, 0, 0,
                0, green / 128.0f, 0, 0, 0,
                0, 0, blue / 128.0f, 0, 0,
                0, 0, 0, 1, 0};

        // 3.定义ColorMatrix，并指定RGBA矩阵
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(src);
        // 4.使用ColorMatrix创建一个ColorMatrixColorFilter对象, 作为画笔的滤镜, 设置Paint的颜色
        paint.setColorFilter(new ColorMatrixColorFilter(src));
        // 5.通过指定了RGBA矩阵的Paint把原图画到空白图片上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);

        return afterBitmap;
    }

    public static Bitmap changeColor(Bitmap baseBitmap, String color) {
        Bitmap afterBitmap = Bitmap.createBitmap(baseBitmap.getWidth(),
                baseBitmap.getHeight(), baseBitmap.getConfig());
        Canvas canvas = new Canvas(afterBitmap);
        Paint paint = new Paint();

        paint.setColorFilter(new LightingColorFilter(0, Color.parseColor(color)));
        // 5.通过指定了RGBA矩阵的Paint把原图画到空白图片上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);
        return afterBitmap;
    }

    /**
     * 保存bitmap 为图片
     * @param file 目标图片
     * @param bmp  图片
     * @return
     */
    public static File saveImageToGallery(@NonNull File file, @NonNull Bitmap bmp) {
        if (bmp == null) {
            throw new IllegalArgumentException("bmp should not be null");
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "saveImageToGallery: the path of bmp is " + file.getAbsolutePath());
        return file;
    }

    /**
     * 保存文件到图库
     * @param context 上下文
     * @param file  图片文件
     */
    public static void notifySystemGallery(@NonNull Context context, @NonNull File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("bmp should not be null");
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(),
                    file.getName(), null);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File couldn't be found");
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static Bitmap compressImage(Bitmap image,int size,int options) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

}
