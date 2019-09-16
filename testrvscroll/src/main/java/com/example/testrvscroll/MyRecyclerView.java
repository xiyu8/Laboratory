package com.example.testrvscroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRecyclerView extends RecyclerView {
  public MyRecyclerView(@NonNull Context context) {
    super(context);
    init();
  }

  public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  LinearLayoutManager manager;
  private void init() {
    String[] ss = {"111", "222", "333"};
    ArrayList arrayList=new ArrayList(Arrays.asList("11","22","33","44","55","66","77","88","99","111","222","333","444","555","666","777","888","999","1111","2222","3333"));
    Adapter adapter = new Adapter(arrayList);
    setLayoutManager(new LinearLayoutManager(getContext()));
    setAdapter(adapter);
    manager = (LinearLayoutManager) getLayoutManager();
    addOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
//          mShouldScroll = false;
//          smoothMoveToPosition(recyclerView, mToPosition);
//          smoothScrollToPosition(11);
//          smoothScrollToPosition(3);
          scollToPosition(recyclerView, 3);
        }


      }

      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

//        //在这里进行第二次滚动
//        if (move){
//          move = false;
//          //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
//          int n = index - manager.findFirstVisibleItemPosition();
//          if ( 0 <= n && n < recyclerView.getChildCount()){
//            //获取要置顶的项顶部离RecyclerView顶部的距离
//            int top = recyclerView.getChildAt(n).getTop();
//            //最后的移动
//            recyclerView.smoothScrollBy(0, top);
//          }
//        }



      }
    });
  }

  boolean move;


  public class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public Adapter(@Nullable List<String> data) {
      super(R.layout.layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
      helper.setText(R.id.text, item);
    }
  }



  //目标项是否在最后一个可见项之后
  private boolean mShouldScroll;
  //记录目标项位置
  private int mToPosition;
  /**
   * 滑动到指定位置
   */
  private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
    // 第一个可见位置
    int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
    // 最后一个可见位置
    int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
    if (position < firstItem) {
      // 第一种可能:跳转位置在第一个可见位置之前
      mRecyclerView.smoothScrollToPosition(position);
    } else if (position <= lastItem) {
      // 第二种可能:跳转位置在第一个可见位置之后
      int movePosition = position - firstItem;
      if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
        int top = mRecyclerView.getChildAt(movePosition).getTop();
        mRecyclerView.smoothScrollBy(0, top);
      }
    } else {
      // 第三种可能:跳转位置在最后可见项之后
      mRecyclerView.smoothScrollToPosition(position);
      mToPosition = position;
      mShouldScroll = true;
    }
  }


  int index;
  public void scollToPosition(RecyclerView recyclerView,int n) {
    //滑动到指定的item
    this.index = n ;//记录一下 在第三种情况下会用到
    //拿到当前屏幕可见的第一个position跟最后一个postion
    int firstItem = manager.findFirstVisibleItemPosition();
    int lastItem = manager.findLastVisibleItemPosition();
    //区分情况
    if (n <= firstItem ){
      //当要置顶的项在当前显示的第一个项的前面时
      recyclerView.smoothScrollToPosition(n);
    }else if ( n <= lastItem ){
      //当要置顶的项已经在屏幕上显示时
      int top = recyclerView.getChildAt(n - firstItem).getTop();
      recyclerView.smoothScrollBy(0,top);
    }else{
      //当要置顶的项在当前显示的最后一项的后面时
      recyclerView.smoothScrollToPosition(n);
      //这里这个变量是用在RecyclerView滚动监听里面的
      move = true;
    }
  }

}
