package com.example.scrollpicker.scrollpicker;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.scrollpicker.R;

import java.util.List;

public class ViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
  Activity context;
  int itemHeight;

  public ViewAdapter(@Nullable List<String> data, Activity context, int resId, int itemHeight) {
    super(resId,data);
    this.context = context;
    this.itemHeight = itemHeight;
  }

  @Override
  protected void convert(BaseViewHolder helper, String item) {
    TextView textView=helper.getView(R.id.tttt);
    textView.getLayoutParams().height = itemHeight;
    textView.requestLayout();
    helper.setText(R.id.tttt, item);
  }

}
