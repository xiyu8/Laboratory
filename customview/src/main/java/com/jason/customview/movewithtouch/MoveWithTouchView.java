package com.jason.customview.movewithtouch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class MoveWithTouchView extends FrameLayout {
    public MoveWithTouchView(Context context) {
        super(context);
        init();
    }

    public MoveWithTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveWithTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MoveWithTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    OverScroller mScroller;

    private void init() {
        mScroller = new OverScroller(getContext());
    }

    VelocityTracker mVelocityTracker;
    int mLastX,mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currX = (int) event.getX();
        int currY = (int) event.getY();
        if (mVelocityTracker == null) {
            //创建 velocityTracker 对象
            mVelocityTracker = VelocityTracker.obtain();
        }
        //关联事件对象
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下时如果还在滚动将停止滚动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                // 每次移动的变化量,内容向左移动，变化量是负数，反之是正数
                int detalX = currX - mLastX;
                int detalY = currY - mLastY;

                // 由于scrollTo的特殊性，传入负数，内容其实是向右移动的，这与我们向左移动相违背，
                // 所以这里要将变化量取反再和上一次滚动的距离相加得出新的滚动距离（水平方向滚动距离为正表示内容在向左滚动）
                int newScrollX = getScrollX() - detalX; // 可理解为 getScrollX() + (-detalX);
                int newScrollY = getScrollY() - detalY; // 可理解为 getScrollX() + (-detalX);
//                if (newScrollX > maxScrollX) {
//                    // 向左移动的mScrollX最大值不能超过getWidth() - childWidth
//                    newScrollX = maxScrollX;
//                }
//                if (newScrollX < 0) {
//                    // 向右边移动的mScrollX最小值是0，也就是第一个View完全显示的效果
//                    newScrollX = 0;
//                }
                // 传入正数，内容向左移动，反之向右移动
                scrollTo(newScrollX, newScrollY);
                break;
            case MotionEvent.ACTION_UP:
                // 松手后平滑滚动
                smoothScroll();
                break;
        }
        // 实时更新上次的x坐标
        mLastX = currX;
        mLastY = currY;
        return true;

    }

    private void smoothScroll() {

    }


}
