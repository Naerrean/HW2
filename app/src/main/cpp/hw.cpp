#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hw_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject,
        jint key = 0) {
    std::string hello = "";

    switch (key) {
        case 0:{
            hello = "Hello from C++ ?";
            break;
        }
        case 1:{
            hello = "Hello from Kotlin++ ?";
            break;
        }
        case 2:{
            hello = "Hello from JS++ ?";
            break;
        }
        case 3:{
            hello = "Hello from TEST !!!";
            break;
        }
        default:{
            hello = "Dulya++";
            break;
        }
    }
    return env->NewStringUTF(hello.c_str());
}
