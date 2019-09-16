package com.example.scrollpicker.scrollpicker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.scrollpicker.R;

import java.util.ArrayList;
import java.util.Arrays;

//
//
///**
// * Created by xiyu on 2018/4/9.
// */
//
public class FloorLayout extends LinearLayout{


  Activity context;
  int strideLength, windowsSize;
  FloorOptionCallBack floorOptionCallBack;
  MyRecyclerView recyclerView;
  ViewAdapter adapter;
  ArrayList<String> data;
  int newScrollState;

  int currentOffsetRv;

  public FloorLayout(Activity context,FloorOptionCallBack floorOptionCallBack) {
    super(context);

    strideLength = 200;
    windowsSize = 5;

    this.context = context;
    View myView = LayoutInflater.from(context).inflate(R.layout.floor_layout, this, true);
    recyclerView = findViewById(R.id.main_recyclerview);
    adapter = new ViewAdapter(new ArrayList<String>(),context,R.layout.item_layout,strideLength);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(adapter);
    generateAndAddOption();
    this.floorOptionCallBack = floorOptionCallBack;

    findViewById(R.id.cover_choose).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,strideLength));
//    adapter.getItemLayout().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,strideLength));

    recyclerView.getLayoutParams().height =strideLength * windowsSize;
    recyclerView.requestLayout();
    findViewById(R.id.cover).getLayoutParams().height = strideLength * windowsSize;
    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.e("TTTT", "CCCCCCCCCCCCCCCCCCCC"+position);
        if(position>=windowsSize/2&&position<data.size()+windowsSize/2) {
          Log.e("TTTT", "CCCCCCCCCCCCCCCCCCCC:"+currentOffsetRv);

          recyclerView.smoothScrollToPosition(position + 2);
//          currentOffsetRv = strideLength * (position + 3);
        }
      }
    });
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      int scrollIntegral;
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        newScrollState = newState;
        if(newState==RecyclerView.SCROLL_STATE_IDLE){
          Log.e("TTTT", "IIIIIIIIIIIIIIIIIIII"+currentOffsetRv);
          scrollIntegral = currentOffsetRv / strideLength;
          if (currentOffsetRv % strideLength > strideLength / 2) {
            recyclerView.smoothScrollToPosition(scrollIntegral + 1);
//            currentSelectedTime = rangeHead + oneDay * (scrollRemain + 1);
          } else {
            recyclerView.smoothScrollToPosition(scrollIntegral);
//            currentSelectedTime = rangeHead + oneDay * (scrollRemain);
          }
        }
      }
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
          currentOffsetRv += dy;
        Log.e("TTTT", "TTTTTTTTTTTTcurrentOffsetRv += dyTTTTTTTTTT："+(currentOffsetRv += dy));

      }
    });
  }

  long oneDay = 24 * 60 * 60 * 1000L;
  private void generateAndAddOption() {
    data=new ArrayList<>(Arrays.asList("11","22","33","44","55","66","77","88","99","111","222","333","444","555","666","777","888","999","1111","2222","3333"));
    for (int j = 0; j < windowsSize / 2; j++) {
      data.add(0, "");
      data.add("");
    }
    adapter.setNewData(data);
  }


//  @Override
//  public boolean onTouch(View v, MotionEvent event) {
//    MyScrollView area1 = v.getId() == R.id.daytime_area ? dayTimeArea : area;
//    int action = event.getAction();
//    if (action == MotionEvent.ACTION_UP) {
//      area1.requestDisallowInterceptTouchEvent(false);
//    } else {
//      area1.requestDisallowInterceptTouchEvent(true);
//    }
//    int mainScroll;
//    if (action == MotionEvent.ACTION_UP) {
//
//      mainScroll = FloorLayout.this.scrollY / strideLength;
//      if (FloorLayout.this.scrollY % strideLength > strideLength / 2) {
//        area1.smoothScrollTo(0, (mainScroll + 1) * strideLength);
//        switch (v.getId()) {
//          case R.id.area:
//            currentSelectedTime = rangeHead + oneDay * (mainScroll + 1);
//            break;
//          case R.id.daytime_area:
//            if (isStartTime) {
//              currentSelectedDaytime = dayTime = (mainScroll + 1) == 0 ? eightAM : twoPM;
//            } else {
//              currentSelectedDaytime = dayTime = (mainScroll + 1) == 0 ? twelveAM : sixPM;
//            }
//            break;
//        }
//      } else {
//        area1.smoothScrollTo(0, mainScroll * strideLength);
//        switch (v.getId()) {
//          case R.id.area:
//            currentSelectedTime = rangeHead + oneDay * (mainScroll);
//            break;
//          case R.id.daytime_area:
//            if (isStartTime) {
//              currentSelectedDaytime = dayTime = mainScroll == 0 ? eightAM : twoPM;
//            } else {
//              currentSelectedDaytime = dayTime = mainScroll == 0 ? twelveAM : sixPM;
//            }
//            break;
//        }
//      }
//
//    } else {
//      pressed = true;
//    }
//    return false;
//  }


//  private int dip2px(float dpValue) {
//    final float scale = context.getResources().getDisplayMetrics().density;
//    return (int) (dpValue * scale + 0.5f);
//  }

  private int px2dip(float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }


}