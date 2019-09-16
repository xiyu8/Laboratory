package com.example.scrollpicker.old;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by xiyu on 2018/4/9.
 */

public class MyScrollView extends ScrollView {

    private OnScrollListener listener;
    private OnScrollStopListener onScrollStopListener;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }
    public void setOnScrollStopListener(OnScrollStopListener listener) {
        this.onScrollStopListener = listener;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 监听滑动距离
    public interface OnScrollListener{
        void onScroll(int scrollY);
    }
    // 监听滑动停止
    public interface OnScrollStopListener{
        void onScrollStop();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(listener!=null){
            listener.onScroll(getScrollY());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessage(0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    Handler handler = new Handler()
    {


        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 0: checkIsScrolling();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void checkIsScrolling() {
        try {
            Field field = getClass().getSuperclass().getDeclaredField(
                    "mScroller");
            field.setAccessible(true);
            Object object = field.get(this);
            OverScroller scroller = (OverScroller) object;
            boolean isScrolling = scroller.computeScrollOffset();
            if (isScrolling) {
                handler.sendEmptyMessageDelayed(0, 10);
            } else {
                onScrollStopListener.onScrollStop();//滚动结束
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
























}
