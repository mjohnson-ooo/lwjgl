#include "Windows.h"
#include <jni.h>
#include "org_lwjgl_opengl_WindowsIME.h"

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_WindowsIME_CreateContext (
        JNIEnv *env, jclass unused)
{
    return (intptr_t)ImmCreateContext();
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_WindowsIME_DestroyContext (
        JNIEnv *env, jclass unused, jlong himc_int)
{
    HIMC himc = (HIMC)(INT_PTR)himc_int;
    return ImmDestroyContext(himc);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_WindowsIME_AssociateContext (
        JNIEnv *env, jclass unused, jlong hwnd_int, jlong himc_int)
{
    HWND hwnd = (HWND)(INT_PTR)hwnd_int;
    HIMC himc = (HIMC)(INT_PTR)himc_int;
    return (intptr_t)ImmAssociateContext(hwnd, himc);
}
