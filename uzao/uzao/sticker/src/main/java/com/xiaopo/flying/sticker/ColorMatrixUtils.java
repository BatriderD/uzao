package com.xiaopo.flying.sticker;

import android.graphics.ColorMatrix;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class ColorMatrixUtils {

    /**
     * 颜色矩阵变换
     * @param contrastNum
     * @param saturationNum
     * @param brightNum
     * @return
     */
    public static ColorMatrix handleImageEffect(float contrastNum, float saturationNum, float brightNum) {


        ColorMatrix mAllMatrix = new ColorMatrix();


        ColorMatrix saturationMatrix = new ColorMatrix();


        ColorMatrix contrastMatrix = new ColorMatrix();


        ColorMatrix brightnessMatrix = new ColorMatrix();


        saturationMatrix.setSaturation(saturationNum);

        brightnessMatrix.set(new float[]{1.0F, 0.0F, 0.0F, 0.0F, brightNum, 0.0F, 1.0F, 0.0F, 0.0F, brightNum, 0.0F, 0.0F, 1.0F, 0.0F, brightNum, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});

        float regulateBright = 0.0F;
        regulateBright = (1.0F - contrastNum) * 128.0F;
        contrastMatrix.reset();
        contrastMatrix.set(new float[]{contrastNum, 0.0F, 0.0F, 0.0F, regulateBright, 0.0F, contrastNum, 0.0F, 0.0F, regulateBright, 0.0F, 0.0F, contrastNum, 0.0F, regulateBright, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});


        mAllMatrix.postConcat(saturationMatrix);
        mAllMatrix.postConcat(brightnessMatrix);
        mAllMatrix.postConcat(contrastMatrix);

//        http://www.createjs.cc/src/docs/easeljs/files/easeljs_filters_ColorMatrix.js.html#l41
//        /**
//         * Adjusts the color saturation of the pixel.
//         * Positive values will increase saturation, negative values will decrease saturation (trend towards greyscale).
//         * @method adjustSaturation
//         * @param {Number} value A value between -100 & 100.
//         * @return {ColorMatrix} The ColorMatrix instance the method is called on (useful for chaining calls.)
//         * @chainable
//         **/
//        p.adjustSaturation = function(value) {
//            if (value == 0 || isNaN(value)) { return this; }
//            value = this._cleanValue(value,100);
//            var x = 1+((value > 0) ? 3*value/100 : value/100);
//            var lumR = 0.3086;
//            var lumG = 0.6094;
//            var lumB = 0.0820;
//            this._multiplyMatrix([
//                    lumR*(1-x)+x,lumG*(1-x),lumB*(1-x),0,0,
//                    lumR*(1-x),lumG*(1-x)+x,lumB*(1-x),0,0,
//                    lumR*(1-x),lumG*(1-x),lumB*(1-x)+x,0,0,
//                    0,0,0,1,0,
//                    0,0,0,0,1
//		]);
//            return this;
//        };

        return mAllMatrix;
    }

    /**
     * 颜色矩阵变换
     *
     * @param contrastNum
     * @param saturationNum
     * @param brightNum
     * @return
     */
    public static ColorMatrix handleImageEffect2(float contrastNum, float saturationNum, float brightNum, float hue) {


        ColorMatrix mAllMatrix = new ColorMatrix();


        ColorMatrix saturationMatrix = new ColorMatrix();


        ColorMatrix contrastMatrix = new ColorMatrix();


        ColorMatrix brightnessMatrix = new ColorMatrix();


        saturationMatrix.setSaturation(saturationNum);

        brightnessMatrix.set(new float[]{1.0F, 0.0F, 0.0F, 0.0F, brightNum, 0.0F, 1.0F, 0.0F, 0.0F, brightNum, 0.0F, 0.0F, 1.0F, 0.0F, brightNum, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});

        float regulateBright = 0.0F;
        regulateBright = (1.0F - contrastNum) * 128.0F;
        contrastMatrix.reset();
        contrastMatrix.set(new float[]{contrastNum, 0.0F, 0.0F, 0.0F, regulateBright, 0.0F, contrastNum, 0.0F, 0.0F, regulateBright, 0.0F, 0.0F, contrastNum, 0.0F, regulateBright, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F});


        ColorMatrix hueMatrix = getHueMatrix(hue);

        mAllMatrix.postConcat(hueMatrix);
        mAllMatrix.postConcat(saturationMatrix);
        mAllMatrix.postConcat(brightnessMatrix);
        mAllMatrix.postConcat(contrastMatrix);

        return mAllMatrix;
    }


    private static ColorMatrix getHueMatrix(double value) {
        ColorMatrix hueMatrix = new ColorMatrix();
        if (value == 0) {
            return hueMatrix;
        }
        value = clean(value, 180) / 180 * Math.PI;
        double cosVal = Math.cos(value);
        double sinVal = Math.sin(value);
        double lumR = 0.213;
        double lumG = 0.715;
        double lumB = 0.072;
        hueMatrix.set(new float[]{
                (float) (lumR + cosVal * (1 - lumR) + sinVal * (-lumR)), (float) (lumG + cosVal * (-lumG) + sinVal * (-lumG)), (float) (lumB + cosVal * (-lumB) + sinVal * (1 - lumB)), 0, 0,
                (float) (lumR + cosVal * (-lumR) + sinVal * (0.143)), (float) (lumG + cosVal * (1 - lumG) + sinVal * (0.140)), (float) (lumB + cosVal * (-lumB) + sinVal * (-0.283)), 0, 0,
                (float) (lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR))), (float) (lumG + cosVal * (-lumG) + sinVal * (lumG)), (float) (lumB + cosVal * (1 - lumB) + sinVal * (lumB)), 0, 0,
                0, 0, 0, 1, 0,
                0, 0, 0, 0, 1
        });
        return hueMatrix;
    }

    private static double clean(double value, double limit) {
        return Math.min(limit, Math.max(-limit, value));
    }
}
