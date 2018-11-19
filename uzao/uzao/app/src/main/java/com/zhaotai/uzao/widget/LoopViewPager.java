package com.zhaotai.uzao.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * description: 自动轮播的viewpager
 * author : zp
 * date: 2017/8/10
 */

public class LoopViewPager extends ViewPager {

    private boolean isAutoPlay = true;
    private Disposable subscribe;

    public LoopViewPager(Context context) {
        super(context);
        init();
    }


    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public void start() {
        PagerAdapter adapter = this.getAdapter();
        if (adapter == null || adapter.getCount() == 0)
            return;
        final int count = adapter.getCount();
        // 5s的延迟，5s的循环时间
        // 5s的延迟，5s的循环时间
        if (subscribe == null) {
            subscribe = Observable.interval(5, 5, TimeUnit.SECONDS) // 5s的延迟，5s的循环时间
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            if (count > 0 && isAutoPlay) {
                                int currentItem = getCurrentItem();
                                currentItem++;
                                setCurrentItem(currentItem);
                            }
                        }


                    });
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                start();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stop();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void stop() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

}
