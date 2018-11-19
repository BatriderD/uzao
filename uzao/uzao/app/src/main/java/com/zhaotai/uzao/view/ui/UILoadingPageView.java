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

public class UILoadingPageView extends AppCompatImageView {
    private AnimationDrawable animationDrawable;

    public UILoadingPageView(Context context) {
        this(context, null);
    }

    public UILoadingPageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.UILoadingStyle);
    }

    public UILoadingPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.loading_page_normal_animation);
        animationDrawable = (AnimationDrawable) getDrawable();
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
