package com.jason.customview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.jason.customview.dialog.LoadingDialog;
import com.jason.customview.pickerkit.PickerView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WWWW", "onCreate");
        setContentView(R.layout.activity_main);
        PickerView pickerView = findViewById(R.id.picker_view);
        pickerView.setData(new ArrayList<String>(){{add("111");add("222");add("333");add("444");add("555");}});
//        RecyclerView;
//        View;
//        ViewGroup viewGroup;
//        viewGroup.addView();
//        FrameLayout;
//        RecyclerView.LayoutManager;
//        LinearLayoutManager


        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.showDialog();

//        new Handler().postDelayed(new Runnable()           {
//            @Override
//            public void run() {
//                loadingDialog.dismissDialog();
//            }
//        }, 3000);
//
//        new Timer().schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                System.out.println("bomobing...");
//            }
//        }, 2000, 3000);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("WWWW", "onConfigurationChanged:"+newConfig.orientation);
    }

    @Override
    protected void onStart() {
        Log.e("WWWW", "onStart");
        super.onStart();
    }


    @Override
    protected void onStop() {
        Log.e("WWWW", "onResume");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.e("WWWW", "onStop");

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("WWWW", "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("WWWW", "onDestroy");
        super.onDestroy();
    }
}