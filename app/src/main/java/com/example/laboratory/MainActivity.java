package com.example.laboratory;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//      Looper.prepare();
//      Handler handler =new Handler();
//      Message message = Message.obtain();
//      message.setCallback();
//      handler.sendMessage();
//      handler.post();
//      handler.postDelayed();
//      Looper.loop();
//
//      handler.post();
//
//      AtomicInteger atomicInteger;
//      atomicInteger.incrementAndGet();
//
//      ReentrantLock

  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopService(new Intent(MainActivity.this, MyService.class));
//            }
//        }, 5000);

    }

    public void onClick(View view) {
      startActivityForResult(new Intent(this,MainActivity2.class),111);

    }
}
