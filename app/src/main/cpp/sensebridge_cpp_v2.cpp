#include "common_defs.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sensebridge_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Swarnavo from C++ " + header_string();
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sensebridge_MainActivity_stringFromJNI2(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Potnavo from C++ " + header_string();
    return env->NewStringUTF(hello.c_str());
}