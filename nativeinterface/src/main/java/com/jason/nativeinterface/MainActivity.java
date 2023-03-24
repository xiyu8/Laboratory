package com.jason.nativeinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jason.nativeinterface.jni.CryptTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Class clazz;

        /**
         * sample for local import so lib,And invoke so's C code
         * Additional:the default using,C in AndroidStudio project,config make C code,generate C's so lib,import the lib,and use it in java directly.
         *
         *
         *
         */

        String ss = CryptTool.getCryptIv("cc");
        Log.e("CryptTool", "CryptTool.getCryptKey()" + ss);
//        String ss = NDKTools.getStringFromNDK();
//        Log.e("NDKTools", "NDKTools.getStringFromNDK()" + ss);


//        getClass().getAnnotation();
//        getClass().getField();
//        getClass().getMethod();
    }
}