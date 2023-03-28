#include "com_jason_nativeinterface_jni_CryptTool.h"
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

JNIEXPORT jstring JNICALL Java_com_jason_nativeinterface_jni_CryptTool_getCryptKey
  (JNIEnv *env, jobject obj){
     return (*env)->NewStringUTF(env,"Hellow World，jni*********************************");
  }

static jstring getCryptIv(JNIEnv *evn, jstring ss) {
//    strcat("Hellow World，jni*********************************", ss);

    char *ii[50];
    char *tt[50];
//    strcpy(*ii,  ss);
//    strcpy(*tt, "This is destination");
    strcat(*ii, "This is destination ");
    return (*evn)->NewStringUTF(evn,*ii);
}

JNINativeMethod nativeMethod[] = {{"getCryptIv", "(Ljava/lang/String;)Ljava/lang/String;", (jstring*)getCryptIv},};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {

    JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    // jclass clz，java中 方法的描述
    jclass clz = (*env)->FindClass(env,"com/jason/nativeinterface/jni/CryptTool");
    // JNINativeMethod nativeMethod[] 结构体数组，native中C方法的描述
    (*env)->RegisterNatives(env,clz, nativeMethod, sizeof(nativeMethod)/sizeof(nativeMethod[0])); //C中的用法与C++不同(*env)-> *env->

    return JNI_VERSION_1_4;
}
