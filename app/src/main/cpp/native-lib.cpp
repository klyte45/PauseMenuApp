#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_halkyproject_com_pausemenu_PauseMenuMain_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
