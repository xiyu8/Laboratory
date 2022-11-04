package com.jason.aidlserver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String aidlPkg = IMyAidlInterface.class.getPackage().getName();
        //get the class name from the interface package
        String aidlPkgInterfaceName = IMyAidlInterface.class.getName();
        String aidlClsName = aidlPkgInterfaceName.replace("IMyAidlInterface", "AidlService");
        Intent intent = new Intent("com.jason.aidlserver.AidlService");
        intent.setClassName(aidlPkg, aidlClsName);
        boolean bRet = getApplicationContext().bindService(intent, connection, Service.BIND_AUTO_CREATE);
        Log.e("IRemote", "IRemoteService Service.BIND_AUTO_CREATE return: " + bRet);
        View
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
                user.setName("32323");
                binder.test2(user);
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