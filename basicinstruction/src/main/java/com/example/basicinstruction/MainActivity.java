package com.example.basicinstruction;
;
import android.os.Bundle;

import com.example.basicinstruction.base.BasicActivity;

public class MainActivity extends BasicActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_main;
  }
}
