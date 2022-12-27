package com.example.laboratory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jason.annotation.BindView;
import com.jason.breadknife.BreadKnife;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @BindView(id = R.id.rrr)
    RecyclerView recyclerView;



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

//      HashMap hashMap = ;
//      LinkedHashMap hashMap = ;
//      hashMap.put()


//      RecyclerView recyclerView = findViewById(R.id.rrr);
      BreadKnife.bind(this);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));

      recyclerView.setAdapter(new YourAdapter(new ArrayList<String>(){{
          add("******0*****");
          add("******1*****");
          add("******2*****");
          add("******3*****");
          add("******4*****");
          add("******5*****");
          add("******6*****");
          add("******7*****");
          add("******8*****");
          add("******9*****");
          add("******10*****");
          add("******11*****");
          add("******12*****");
          add("******13*****");
          add("******14*****");
          add("******15*****");
          add("******16*****");
          add("******17*****");
          add("******18*****");
          add("******19*****");
          add("******20*****");
          add("******21*****");
          add("******22*****");
          add("******23*****");
          add("******24*****");
          add("******25*****");
          add("******26*****");
          add("******27*****");
          add("******28*****");
          add("******29*****");
          add("******30*****");
          add("******31*****");
          add("******32*****");
          add("******33*****");
          add("******34*****");
          add("******35*****");
          add("******36*****");
          add("******37*****");
          add("******38*****");
          add("******39*****");
      }}));
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
//      startActivityForResult(new Intent(this,MainActivity2.class),111);

    }
}
