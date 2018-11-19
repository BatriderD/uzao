package com.zhaotai.uzao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ScrollWebView extends WebView {

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }
    public OnScrollListener listener;
    /**
     * This is called in response to an internal scroll in this view (i.e., the
     * view scrolled its own contents). This is typically as a result of
     * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
     * called.
     *
     * @param l Current horizontal scroll origin.
     * @param t Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (listener != null){
            if (t - oldt <= 2){
                listener.onScrollDown();
            }
            if(oldt - t >= 2) {
                listener.onScrollUp();
            }
            listener.scrollHeight(t);
        }
    }
    public void setListener(OnScrollListener listener){
        this.listener = listener;
    }

    public interface OnScrollListener{
        void onScrollUp();//上滑
        void onScrollDown();//下滑
        void scrollHeight(int h);
    }
}
 