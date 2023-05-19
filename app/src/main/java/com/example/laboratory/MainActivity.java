package com.example.laboratory;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jason.annotation.BindView;
import com.jason.breadknife.BreadKnife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

//      ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
//      threadLocal.get();
//      threadLocal.set();
//
//      ViewGroup viewGroup = new ViewGroup();
//      viewGroup.dispatchTouchEvent();


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



//      Intent intent = new Intent();
//      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      //ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//      //ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
//      ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
//      intent.setComponent(comp);
////      if (getEmuiVersion() == 3.1) {
////          //emui 3.1 的适配
////          startActivity(intent);
////      } else {
////          //emui 3.0 的适配
//          comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");//悬浮窗管理页面
//          intent.setComponent(comp);
////      }
//      startActivity(intent);

      //小米电池优化界面
//      requestIgnoreBatteryOptimizations();



//      ComponentName componentName = new ComponentName("com.huawei.systemmanager",
//              "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
//      ComponentName componentName = new ComponentName("com.huawei.systemmanager",
//              "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
      //小米自启动界面
//      ComponentName componentName = new ComponentName("com.miui.securitycenter",
//              "com.miui.permcenter.autostart.AutoStartManagementActivity");
//      Intent intent = new Intent();
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      intent.setComponent(componentName);
//      startActivity(intent);


      //ThreadLocal的使用
      for (int i = 0; i < 2; i++) {
          new Thread(new Runnable() {
              @Override
              public void run() {
                  //创建每个线程私有的变量
                  int data = new Random().nextInt(100);
                  System.out.println(Thread.currentThread().getName()+" has put data: "+data);
                  //往local里面设置值
                  intThreadLocal.set(data);
                  DataBean dataBean = new DataBean();
                  dataBean.setName("name "+data);
                  dataBean.setAge(data);
                  dataThreadLocal.set(dataBean);
                  new A().get();
                  new B().get();

                  //获取自己线程的MyThreadLocalScopeDate实例对象
                  ThreadLocalDataBean threadLocalDataBean = ThreadLocalDataBean.getThreadInstance();
                  threadLocalDataBean.setName("name"+data);
                  threadLocalDataBean.setAge(data);
                  new A().get();
                  new B().get();

              }
          }).start();
      }


  }

    static class A{
        public void get(){
            int data =intThreadLocal.get();
            System.out.println("A from "+Thread.currentThread().getName()+" has get intThreadLocal: "+data);
            DataBean scopeDate=dataThreadLocal.get();
            System.out.println("A from "+Thread.currentThread().getName()+" has get dataThreadLocal name: "+scopeDate.getName()+" , age: "+scopeDate.getAge());
            ThreadLocalDataBean threadLocalDataBean = ThreadLocalDataBean.getThreadInstance();
            System.out.println("A from "+Thread.currentThread().getName()+" has get threadLocalDataBean name: "+threadLocalDataBean.getName()+" , age: "+threadLocalDataBean.getAge());
        }
    }


    static class B{
        public void get(){
            int data =intThreadLocal.get();
            System.out.println("B from "+Thread.currentThread().getName()+" has get intThreadLocal: "+data);
            DataBean scopeDate=dataThreadLocal.get();
            System.out.println("B from "+Thread.currentThread().getName()+" has get dataThreadLocal name: "+scopeDate.getName()+" , age: "+scopeDate.getAge());
        }
    }

    private static ThreadLocal<Integer> intThreadLocal = new ThreadLocal<Integer>();
    private static ThreadLocal<DataBean> dataThreadLocal = new ThreadLocal<DataBean>();

    class DataBean{
        private String name;
        private int age;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }

    }


     static class ThreadLocalDataBean{//单例模式

        private ThreadLocalDataBean(){};//构造方法私有化

         private String name;
         private int age;
         //ThreadLocal当一个Map使用；把Map放到一个单例类中使用,数据bean为此单例类属性，Map的value为 此单例类
        private static ThreadLocal<ThreadLocalDataBean> threadLocalDataBeanThreadLocal = new ThreadLocal<ThreadLocalDataBean>();//封装MyThreadLocalScopeDate是线程实现范围内共享

        //思考AB两个线程过来的情况 自己分析 AB都需要的自己的对象 没有关系 所以不需要同步 如果有关系就需要同步了
        public static /*synchronized*/ThreadLocalDataBean getThreadInstance(){
            ThreadLocalDataBean instance =threadLocalDataBeanThreadLocal.get();
            if(instance==null){
                instance = new ThreadLocalDataBean();
                threadLocalDataBeanThreadLocal.set(instance);
            }
            return instance;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
