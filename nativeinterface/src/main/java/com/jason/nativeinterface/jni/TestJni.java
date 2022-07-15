package com.jason.nativeinterface.jni;

public class TestJni {

        static{
            System.loadLibrary("test");
        }

        public native void dynamicLog();

        public native void staticLog();


}
