package com.zhaotai.uzao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description :
 */

public class NestGridView extends GridView {
    public NestGridView(Context context) {
        super(context);
    }

    public NestGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
