#include "common_defs.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sensebridge_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello Swarnava from C++ " + header_string();
    return env->NewStringUTF(hello.c_str());
}