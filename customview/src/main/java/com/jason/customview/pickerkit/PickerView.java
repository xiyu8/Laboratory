package com.jason.customview.pickerkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PickerView extends View {

    public static final String TAG = "PickerView";
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.8f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;
    private Paint mBoarderPaint;

    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;
    private float selectedTextSizeFactor = 7.0f; //选中文字的字体大小因数，越大字越小
    private float unselectedTextSizeFactor = 8.0f; //未选中文字的字体大小因数，越大字越小

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mColorText = 0x333333;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastY;

    private float mDownY;    //标示手指第一次按下的位置
    private float mUpPosition;    //标示手指松开的位置
    /**
     * 滑动的距离
     */
    private float mMoveLenY = 0;
    private boolean isInit = false;
    private OnSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLenY) < SPEED) {
                mMoveLenY = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {// 这里mMoveLen / Math.abs(mMoveLenY)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLenY = mMoveLenY - mMoveLenY / Math.abs(mMoveLenY) * SPEED;
            }
            reDraw();
        }
    };

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null) {
            if (mDataList != null && mDataList.size() > mCurrentSelected) {
                mSelectListener.onSelect(this, mCurrentSelected, mDataList.get(mCurrentSelected));
            } else {
                mSelectListener.onSelect(this, 0, "");
            }
        }
    }

    public void setDataAndSelect(List<String> datas) {
        if (datas == null) datas = new ArrayList<>();
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        performSelect();
        reDraw();
    }

    public void setDataAndSelect(List<String> datas, String selected) {
        if (datas == null) datas = new ArrayList<>();
        mDataList = datas;
        if ((mCurrentSelected = datas.indexOf(selected)) == -1) {
            mCurrentSelected = datas.size() / 2;
            performSelect();
            reDraw();
        } else {
            setSelected(mCurrentSelected, true);
        }
    }

    public void setData(List<String> datas) {
        if (datas == null) datas = new ArrayList<>();
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        reDraw();
    }

    private void setSelected(int selected, boolean callback) {
        if (mDataList!=null&&mDataList.size()!=0) {
            mCurrentSelected = selected;
            int distance = mDataList.size() / 2 - mCurrentSelected;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelected--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelected++;
                }
            }
        }
        if (callback) {
            performSelect();
        }
        reDraw();
    }

    /**
     * 选择选中的item的index
     * 警告：该方法只适合本类调用,因为mDataList中item的位置会不停变化
     *
     * @param selected
     */
    @Deprecated
    public void setSelected(int selected) {
        setSelected(selected, true);
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    public void setSelected(String mSelectItem, boolean callback) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i, callback);
                break;
            }
    }

    public void setSelected(String mSelectItem) {
        setSelected(mSelectItem, true);
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        isInit = true;
        reDraw();
    }

    //选中文字的大小，maxFactor越大，选中的文字越小
    public void setSelectedTextSizeFactor(float factor) {
        this.selectedTextSizeFactor = factor;
        reDraw();
    }

    //非选中文字的大小，minFactor越大，非选中的文字越小
    public void setUnSelectedTextSizeFactor(float factor) {
        this.unselectedTextSizeFactor = factor;
        reDraw();
    }

    public void setLineColor(@ColorInt int color) {
        mBoarderPaint.setColor(color);
        reDraw();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mColorText);
        mBoarderPaint = new Paint();
        mBoarderPaint.setAntiAlias(true);
        mBoarderPaint.setStyle(Paint.Style.FILL);
        mBoarderPaint.setColor(Color.parseColor("#ff0000"));
        mBoarderPaint.setAlpha(128);
        mBoarderPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit) {
            // 按照View的高度计算字体大小
            mMaxTextSize = mViewHeight / selectedTextSizeFactor;
            mMinTextSize = mViewHeight / unselectedTextSizeFactor;
            if (mDataList != null && mDataList.size() != 0) {
                drawData(canvas);
            }
            drawBoarder(canvas);
        }
    }

    /**
     * 画边界红线
     *
     * @param canvas
     */
    private void drawBoarder(Canvas canvas) {
        float upperY = mViewHeight * 0.31f;
        float downerY = mViewHeight * 0.69f;
        canvas.drawLine(0, upperY, mViewWidth, upperY, mBoarderPaint);
        canvas.drawLine(0, downerY, mViewWidth, downerY, mBoarderPaint);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLenY); //1
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize; //mMaxTextSize
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha)); //1
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLenY);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLenY;
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDataList==null||mDataList.size()==0) return true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                cancelTimerTask();
                mLastY = event.getY();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveLenY += (event.getY() - mLastY);
                if (mMoveLenY > MARGIN_ALPHA * mMinTextSize / 2) {// 往下滑超过离开距离
                    moveTailToHead();  //重新计算滚动数据
                    mMoveLenY = mMoveLenY - MARGIN_ALPHA * mMinTextSize;
                } else if (mMoveLenY < -MARGIN_ALPHA * mMinTextSize / 2) {// 往上滑超过离开距离
                    moveHeadToTail(); //重新计算滚动数据
                    mMoveLenY = mMoveLenY + MARGIN_ALPHA * mMinTextSize;
                }
                reDraw();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
                mUpPosition = event.getY();
                if (Math.abs(mMoveLenY) < 0.0001) {
                    mMoveLenY = 0;
                    break;
                }
                cancelTimerTask();
                mTask = new MyTimerTask(updateHandler);
                timer.schedule(mTask, 0, 10);
                break;
        }
        return true;
    }

    //滚轮内容向下滚动，手指向上移动
    boolean isTextScrollDown() {
        return mDownY > mUpPosition;
    }


    private void doMove(MotionEvent event) {
        if (mMoveLenY > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLenY = mMoveLenY - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLenY < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLenY = mMoveLenY + MARGIN_ALPHA * mMinTextSize;
        }
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        mUpPosition = event.getY();
        if (Math.abs(mMoveLenY) < 0.0001) {
            mMoveLenY = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    private void cancelTimerTask() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }


    private void reDraw() {
        invalidate();
    }

    public interface OnSelectListener {
        void onSelect(PickerView view, int position, String text);
    }


    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }
}
