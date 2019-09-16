package com.example.basicinstruction.Tool;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import static com.example.basicinstruction.base.Configuration.isDebug;

public class LL {

  static Toast toast;

  public static void t(Context context, String str) {
    if (toast == null) {
      toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
    }
    toast.setText(str);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  public static void e(String content) {
    if (isDebug)
      Log.e("TAG", content);
  }
}
