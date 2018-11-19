package com.zhaotai.uzao.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by zhangpeng on 2017/6/28.
 */

public class VideoViewFullScreen extends VideoView {
    private Uri mUri;
    private int currentPosition;

    public VideoViewFullScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
// TODO Auto-generated constructor stub
    }

    public VideoViewFullScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
    }

    public VideoViewFullScreen(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//这里重写onMeasure的方法
// TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
        mUri = uri;
    }

    @Override
    public void pause() {
        super.pause();
        if (this != null) {
            currentPosition = this.getCurrentPosition();
        }
    }

    public void continuePlay() {
        if (this!=null){
            this.seekTo(currentPosition);
            start();
        }
    }
}
