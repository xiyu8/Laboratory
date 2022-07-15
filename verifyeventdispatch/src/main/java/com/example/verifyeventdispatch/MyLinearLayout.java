package com.example.verifyeventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
  public MyLinearLayout(Context context) {
    super(context);
    init();
  }

  public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    setOnTouchListener(this);
    setOnClickListener(this);
  }


  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    super.dispatchTouchEvent(ev);
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    super.onTouchEvent(event);

    return false;
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    Log.e("yy","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy111" );
    return true;
  }

  @Override
  public void onClick(View view) {
    Log.e("yy","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy222" );
  }


}
