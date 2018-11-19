package com.zhaotai.uzao.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class MyMoveAbleSeekBar extends AppCompatSeekBar {

    private boolean moveAble = true;

    public MyMoveAbleSeekBar(Context context) {
        super(context);
    }

    public MyMoveAbleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMoveAbleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isMoveAble() {
        return moveAble;
    }

    public void setMoveAble(boolean moveAble) {
        this.moveAble = moveAble;
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //原来是要将TouchEvent传递下去的,我们不让它传递下去就行了

        if (moveAble) {
            return super.onTouchEvent(event);
        }

        return false;
    }
}
