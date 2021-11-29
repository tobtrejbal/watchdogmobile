#include <jni.h>
#include <string>
#include "WatchDogCore.h"

extern "C" jstring Java_uhk_watchdog_watchdogmobile_gui_startActivity_StartActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    WatchDogCore * watchDogCore = new WatchDogCore();

    watchDogCore->start();

    return env->NewStringUTF(hello.c_str());
}

extern "C"
jstring
Java_uhk_watchdog_watchdogmobile_gui_startActivity_StartActivity_myString (
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++2";
    return env->NewStringUTF(hello.c_str());
}


