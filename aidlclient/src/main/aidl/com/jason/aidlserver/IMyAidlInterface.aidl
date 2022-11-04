// IMyAidlInterface.aidl
package com.jason.aidlserver;

import com.jason.aidlserver.User;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int testAidl(int value1, int value2);

    int test2(in User user);

}