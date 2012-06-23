package com.brandonlight.gles2demos;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ParticleTestView extends GLSurfaceView {

	ParticleTestRenderer mRenderer;
	
    public ParticleTestView(Context context)
    {
        super(context);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        setEGLContextClientVersion(2);
        
        mRenderer = new ParticleTestRenderer();
        
        setRenderer(mRenderer);
    }
}
