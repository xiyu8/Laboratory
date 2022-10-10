package com.example.laboratory;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 1、 bind start 一起使用时，的调用周期
 *     https://www.jianshu.com/p/ee224f18a4bd
 * 2、start stop 可以是不同的context上下文，start模式生命不和context自动绑定（context start了，context销毁后，service不销毁；context start了，另一个context能stop）
 * 3、bind模式，和context自动绑定（context bind后，context销毁了，service自动unbind）
 *
 *
 */
public class MyService extends Service {
    public MyService() {
    }
    @Override
    public void onCreate() {
        Log.e("MyService", "1111111111");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyService", "2222222222222222");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyService", "333333");
        return myBinder=new MyService.MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("MyService", "444444");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("MyService", "55555");
        super.onDestroy();
    }

    MyBinder myBinder;

    public class MyBinder extends Binder {

    }
}