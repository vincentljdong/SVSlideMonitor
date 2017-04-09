package com.example.vincent_pc.svslidemonitor.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Vincent-PC on 2017/4/8.
 */

public class MyScrollView extends ScrollView {
    public OnScrollListener onScrollListener;

    /**
     * 上一个滚动时的Y值
     */
    private int lastScrollY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 滚动监听
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int scrollY = MyScrollView.this.getScrollY();
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;

                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }

            if (onScrollListener != null) {
                onScrollListener.setOnScrollListener(scrollY);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /**重写onTouchEvent，手势按下去时将Y值回调给setOnScrollListener*/
        if (onScrollListener != null) {
            onScrollListener.setOnScrollListener(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            /**监听手势抬起时，可能还在滑动，5毫秒去发送一次*/
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动时的接口回调
     * 用来回调Y值
     */
    public interface OnScrollListener {
        /**
         * 滚动时Y值的回调
         */
        public void setOnScrollListener(int scrollY);
    }
}
