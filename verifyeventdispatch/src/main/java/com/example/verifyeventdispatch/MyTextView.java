package com.example.verifyeventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class MyTextView extends AppCompatTextView implements View.OnClickListener, View.OnTouchListener {
  public MyTextView(Context context) {
    super(context);
    init();
  }
  public MyTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOnClickListener(this);
    setOnTouchListener(this);
  }


  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    super.dispatchTouchEvent(ev);
    return true;
  }


  @Override
  public void onClick(View view) {
    Log.e("yy","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy333" );
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    Log.e("yy","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy444" );
    return false;
  }
}
