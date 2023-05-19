package com.example.laboratory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        startService(new Intent(this, MyService.class));
        bindService(new Intent(this, MyService.class), serviceConnection, BIND_AUTO_CREATE);



        ArrayList<String> ll = new ArrayList<>();
        Observable
                .fromIterable(ll)
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Throwable {
                        return null;
                    }
                })
                .subscribe()
        ;

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}