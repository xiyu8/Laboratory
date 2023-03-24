package com.jason.nativeinterface.jni;

public class CryptTool {

    static{
        System.loadLibrary("crypt_tool");
    }

    public static native String getCryptKey();

    public static native String getCryptIv(String ss);



}
