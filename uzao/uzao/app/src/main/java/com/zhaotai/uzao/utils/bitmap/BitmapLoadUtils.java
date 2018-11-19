package com.zhaotai.uzao.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.util.EglUtils;
import com.yalantis.ucrop.util.ImageHeaderParser;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Time: 2017/11/16
 * Created by LiYou
 * Description :
 */

public class BitmapLoadUtils {
    private static final String TAG = "BitmapLoadUtils";
    private int degree;

    public static void decodeBitmapInBackground(@NonNull Context context,
                                                @NonNull Uri uri,
                                                int requiredWidth, int requiredHeight,
                                                BitmapLoadCallback loadCallback) {

//        new BitmapLoadTask(context, uri, requiredWidth, requiredHeight, loadCallback).execute();
    }

    public static Bitmap transformBitmap(@NonNull Bitmap bitmap, @NonNull Matrix transformMatrix) {
        try {
            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), transformMatrix, true);
            if (!bitmap.sameAs(converted)) {
                bitmap = converted;
            }
        } catch (OutOfMemoryError error) {
            Log.e(TAG, "transformBitmap: ", error);
        }
        return bitmap;
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width lower or equal to the requested height and width.
            while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * @param options 鲁班计算算法
     * @return
     */
    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options) {
        int mSampleSize;
        int srcHeight = options.outHeight;
        int srcWidth = options.outWidth;
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        srcWidth = srcWidth > srcHeight ? srcHeight : srcWidth;
        srcHeight = srcWidth > srcHeight ? srcWidth : srcHeight;

        double scale = ((double) srcWidth / srcHeight);

        if (scale <= 1 && scale > 0.5625) {
            if (srcHeight < 1664) {
                mSampleSize = 1;
            } else if (srcHeight >= 1664 && srcHeight < 4990) {
                mSampleSize = 2;
            } else if (srcHeight >= 4990 && srcHeight < 10240) {
                mSampleSize = 4;
            } else {
                mSampleSize = srcHeight / 1280 == 0 ? 1 : srcHeight / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            mSampleSize = srcHeight / 1280 == 0 ? 1 : srcHeight / 1280;
        } else {
            mSampleSize = (int) Math.ceil(srcHeight / (1280.0 / scale));
        }

        return mSampleSize;
    }

    public static int getExifOrientation(@NonNull Context context, @NonNull Uri imageUri) {
        int orientation = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            InputStream stream = context.getContentResolver().openInputStream(imageUri);
            if (stream == null) {
                return orientation;
            }
            orientation = new ImageHeaderParser(stream).getOrientation();
            close(stream);
        } catch (IOException e) {
            Log.e(TAG, "getExifOrientation: " + imageUri.toString(), e);
        }
        return orientation;
    }

    public static int exifToDegrees(int exifOrientation) {
        int rotation;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_TRANSPOSE:
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
            case ExifInterface.ORIENTATION_TRANSVERSE:
                rotation = 270;
                break;
            default:
                rotation = 0;
        }
        return rotation;
    }

    public static int exifToTranslation(int exifOrientation) {
        int translation;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
            case ExifInterface.ORIENTATION_TRANSPOSE:
            case ExifInterface.ORIENTATION_TRANSVERSE:
                translation = -1;
                break;
            default:
                translation = 1;
        }
        return translation;
    }

    /**
     * This method calculates maximum size of both width and height of bitmap.
     * It is twice the device screen diagonal for default implementation (extra quality to zoom image).
     * Size cannot exceed max texture size.
     *
     * @return - max bitmap size in pixels.
     */
    @SuppressWarnings({"SuspiciousNameCombination", "deprecation"})
    public static int calculateMaxBitmapSize(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }

        // Twice the device screen diagonal as default
        int maxBitmapSize = (int) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        // Check for max texture size via Canvas
        Canvas canvas = new Canvas();
        final int maxCanvasSize = Math.min(canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight());
        if (maxCanvasSize > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxCanvasSize);
        }

        // Check for max texture size via GL
        final int maxTextureSize = EglUtils.getMaxTextureSize();
        if (maxTextureSize > 0) {
            maxBitmapSize = Math.min(maxBitmapSize, maxTextureSize);
        }

        Log.d(TAG, "maxBitmapSize: " + maxBitmapSize);
        return maxBitmapSize;
    }

    @SuppressWarnings("ConstantConditions")
    public static void close(@Nullable Closeable c) {
        if (c != null && c instanceof Closeable) { // java.lang.IncompatibleClassChangeError: interface not implemented
            try {
                c.close();
            } catch (IOException e) {
                // silence
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        FileInputStream is = null;
        Bitmap resBitmap = null;
        int degree = 0;


        try {
            is = new FileInputStream(filepath);
            BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);
            degree = readPictureDegree(filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        try {
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);
            resBitmap = rotateBitmapByDegree(bitmap, degree);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }


        return resBitmap;
    }


    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {

        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵

        Matrix matrix = new Matrix();

        matrix.postRotate(degree);

        try {

            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片

            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        } catch (OutOfMemoryError e) {

        }

        if (returnBm == null) {

            returnBm = bm;

        }

        if (bm != returnBm) {

            bm.recycle();

        }

        return returnBm;

    }


    public static int readPictureDegree(String path) {

        int degree = 0;

        try {

            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:

                    degree = 90;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:

                    degree = 180;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:

                    degree = 270;

                    break;

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return degree;

    }
}
