#include "Windows.h"
#include <malloc.h>
#include <jni.h>
#include "org_lwjgl_opengl_WindowsIME.h"

#define STRINGRETURNER_SIG "org/lwjgl/opengl/WindowsIME$StringReturner"

jobject getStringReturner (JNIEnv *env, jint result, jstring buf)
{
    jclass clazz = (*env)->FindClass(STRINGRETURNER_SIG);
    jmethodID constructor = (*env)->GetMethodID(clazz, "<init>", "()V");
    jobject object = (*env)->GetFieldID(clazz, constructor);
    jfieldID resultFieldId = (*env)->GetFieldID(clazz, "result", "I");
    jfieldID bufFieldId = (*env)->GetFieldID(clazz, "buf", "Ljava/lang/String;");

    (*env)->SetObjectField(object, resultFieldId, result);
    (*env)->SetObjectField(object, bufFieldlId, buf);

    return object;
}

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

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_WindowsIME_ImmGetCompositionString (
        JNIEnv *env, jclass unused, jlong himc_int, jlong dw_index_int)
{
    HIMC himc = (HIMC)(INT_PTR)himc_int;
    DWORD dwIndex = (DWORD)dw_index_int;
    LONG result = ImmGetCompositionString(himc, dwIndex, (LPVOID)NULL, 0l);

    if (dwIndex == GCS_CURSOR_POS || dwIndex == GCS_DELTASTART ||
            result == IMM_ERROR_NODATA || result == IMM_ERROR_GENERAL) {
        return getStringReturner(env, (jint)result, (jstring)NULL);
    }
    LPVOID buf = (LPVOID)malloc(result);
    result = ImmGetCompositionString(himc, dwIndex, buf, result);
    jstring str (*env)->NewStringUTF(env, buf);
    free(buf);
    return getStringReturner(env, (jint)result, str);
}
