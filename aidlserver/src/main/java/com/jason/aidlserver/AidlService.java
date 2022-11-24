package com.jason.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

public class AidlService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }
    private IBinder binder = new IMyAidlInterface.Stub(){
        @Override
        public int testAidl(int value1, int value2) throws RemoteException {
            return testAidlImp(value1, value2);
        }

        @Override
        public int test2(User user) throws RemoteException {
            return Integer.parseInt(user.getName())+2;
        }
//        @Override
//        public boolean test2(User user){
//            return false;
//        }

    };

    public int testAidlImp(int value1, int value2){
        return 33;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
