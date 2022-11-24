package com.jason.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.ViewGroup;

import com.jason.aidlserver.IMyAidlInterface;
import com.jason.aidlserver.User;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent();
//        //Android 5.0开始，启动服务必须使用显示的，不能用隐式的
//        intent.setComponent(new ComponentName("com.example.laboratory", "com.example.laboratory.AidlService"));
//        intent.setAction("com.example.laboratory.AidlService");
//        intent.setPackage("com.example.laboratory");
//        Intent intent = new Intent("com.example.laboratory.AidlService");
//        intent.setClassName("com.example.laboratory", "com.example.laboratory.AidlService");
//
//        bindService(intent, connection, Context.BIND_AUTO_CREATE);



        String aidlPkg = IMyAidlInterface.class.getPackage().getName();
        //get the class name from the interface package
        String aidlPkgInterfaceName = IMyAidlInterface.class.getName();
        String aidlClsName = aidlPkgInterfaceName.replace("IMyAidlInterface", "AidlService");
        Intent intent = new Intent("com.jason.aidlserver.AidlService");
        intent.setClassName(aidlPkg, aidlClsName);
        boolean bRet = getApplicationContext().bindService(intent, connection, Service.BIND_AUTO_CREATE);
        Log.e("IRemote", "IRemoteService Service.BIND_AUTO_CREATE return: " + bRet);


    }

    IMyAidlInterface binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定服务成功回调
            binder = IMyAidlInterface.Stub.asInterface(service);
            try {
                binder.testAidl(11, 22);
                User user = new User();
                user.setName("1111");
                user.setId(22);
                int tt = binder.test2(user);
                Log.e("IRemote", "int tt = binder.test2(new User()): " + tt);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务断开时回调
            binder = null;
        }
    };

}