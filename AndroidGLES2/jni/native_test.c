
#include <jni.h>
#include <android/log.h>


#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <EGL/egl.h>
#include <EGL/eglext.h>

void TestPrint()
{
	__android_log_print(ANDROID_LOG_INFO, "nativetest", "it worked!");
}

void TestGLES2()
{
    typedef EGLAPI EGLDisplay EGLAPIENTRY FPTR_EGL_GET_DISPLAY(EGLNativeDisplayType display_id);

    FPTR_EGL_GET_DISPLAY* pEglGetDisplay;

    pEglGetDisplay = &eglGetDisplay;

    EGLint majorVersion;
    EGLint minorVersion;

    EGLDisplay display = (*pEglGetDisplay)(EGL_DEFAULT_DISPLAY);

    if (display == EGL_NO_DISPLAY)
    {
        // unable to get display
        return;
    }

    if (!eglInitialize(display, &majorVersion, &minorVersion))
    {
        // unable to init display
        return;
    }

    EGLint numConfigs;
    eglGetConfigs(display, NULL, 0, &numConfigs);

    //EGLConfig* pConfigs = new EGLConfig[numConfigs];
    EGLConfig* pConfigs = malloc(sizeof(EGLConfig)*numConfigs);

    eglGetConfigs(display, pConfigs, numConfigs, &numConfigs);

    typedef struct _EglAttribList
    {
        EGLint attribType;
        const char* attribName;
    } EglAttribList;

    EglAttribList attribList[] =
    {
        { EGL_RED_SIZE, "EGL_RED_SIZE" },
        { EGL_GREEN_SIZE, "EGL_GREEN_SIZE" },
        { EGL_BLUE_SIZE, "EGL_BLUE_SIZE" },
        { EGL_LUMINANCE_SIZE, "EGL_LUMINANCE_SIZE" },
        { EGL_ALPHA_SIZE, "EGL_ALPHA_SIZE" },
        { EGL_DEPTH_SIZE, "EGL_DEPTH_SIZE" },
        { EGL_STENCIL_SIZE, "EGL_STENCIL_SIZE" },

        { EGL_MAX_PBUFFER_WIDTH, "EGL_MAX_PBUFFER_WIDTH" },
        { EGL_MAX_PBUFFER_HEIGHT, "EGL_MAX_PBUFFER_HEIGHT" },
        { EGL_MAX_PBUFFER_PIXELS, "EGL_MAX_PBUFFER_PIXELS" },

        { EGL_SAMPLES, "EGL_SAMPLES" },
        { EGL_SAMPLE_BUFFERS, "EGL_SAMPLE_BUFFERS" }
    };

    const int AttribListSize = sizeof(attribList) / sizeof(EglAttribList);

    int configIdx = 0;
    for (configIdx = 0; configIdx < numConfigs; ++configIdx)
    {
        //ALOGE << "Config " << configIdx << ":" << std::endl;
        __android_log_print(ANDROID_LOG_INFO, "nativetest", "Config %d", configIdx);

        int attribIdx = 0;
        EGLint attribValue;
        for (attribIdx = 0; attribIdx < AttribListSize; ++attribIdx)
        {
            eglGetConfigAttrib(display, pConfigs[0], attribList[attribIdx].attribType, &attribValue);
            //std::cout << "\t" << attribList[attribIdx].attribName << "=" << attribValue << std::endl;
            __android_log_print(ANDROID_LOG_INFO, "nativetest", "%s=%d", attribList[attribIdx].attribName, attribValue);
        }
    }

    //GLWindow* pWindow = GLWindow::Create(256, 256);

    /*
    const EGLint windowAttribList[] =
    {
        EGL_RENDER_BUFFER, EGL_BACK_BUFFER,
        EGL_NONE
    };

    EGLSurface surface = NULL;// = eglCreateWindowSurface(display, pConfigs[0], pWindow->GetHwnd(), NULL); //&windowAttribList[0]);
    //pWindow->Show();

    if (surface == EGL_NO_SURFACE)
    {
        // no surface
        switch (eglGetError())
        {
        case EGL_BAD_NATIVE_WINDOW:
            break;
        }
    }

    EGLint contextAttribs[] =
    {
        EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL_NONE,
    };

    EGLContext context = eglCreateContext(display, pConfigs[0], EGL_NO_CONTEXT, &contextAttribs[0]);

    eglMakeCurrent(display, surface, surface, context);

    BOOL quit = FALSE;
    while (!quit)
    {
        pWindow->ProcessMsg(&quit);

        // draw stuff

        glClearColor(1.0f, 0.0f, 1.0, 1.0);
        glClear(GL_COLOR_BUFFER_BIT);

        eglSwapBuffers(display, surface);
    }

    eglDestroyContext(display, context);
    eglDestroySurface(display, surface);

    //delete [] pConfigs;
    //pConfigs = NULL;

     */

}

void Java_com_brandonlight_gles2demos_ParticleTestRenderer_testGLES2(JNIEnv * env, jobject obj)
{
	TestGLES2();
}

void  Java_com_brandonlight_gles2demos_ParticleTestRenderer_testPrint(JNIEnv * env, jobject obj)
{
    TestPrint();
}
