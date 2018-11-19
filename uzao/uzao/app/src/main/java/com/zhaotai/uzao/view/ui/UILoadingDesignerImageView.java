package com.zhaotai.uzao.view.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.zhaotai.uzao.R;

/**
 * Time: 2017/8/31
 * Created by LiYou
 * Description :
 * 用于显示 Loading 的 {@link View}，支持颜色和大小的设置。
 */

public class UILoadingDesignerImageView extends AppCompatImageView {
    private AnimationDrawable animationDrawable;

    public UILoadingDesignerImageView(Context context) {
        this(context, null);
        setBackgroundResource(R.drawable.loading_designer_animation);
        animationDrawable = (AnimationDrawable) getBackground();
    }

    public UILoadingDesignerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.UILoadingStyle);
    }

    public UILoadingDesignerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    public void stop() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }
}
