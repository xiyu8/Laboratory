package com.example.scrollpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.scrollpicker.scrollpicker.FloorLayout;
import com.example.scrollpicker.scrollpicker.FloorOptionCallBack;

public class MainActivity extends AppCompatActivity implements FloorOptionCallBack {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FrameLayout frameLayout = findViewById(R.id.main);
    FloorLayout floorLayout = new FloorLayout(this, this);
    frameLayout.addView(floorLayout);
  }

  @Override
  public void selectedFloor(int whichFloor) {

  }

  @Override
  public void confirmTimeStamp(long timeStamp, long dayTime, boolean isStartTime) {

  }

  @Override
  public void cancel() {

  }
}
