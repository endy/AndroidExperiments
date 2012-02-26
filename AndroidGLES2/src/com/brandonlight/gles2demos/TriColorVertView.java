package com.brandonlight.gles2demos;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class TriColorVertView extends GLSurfaceView {
	
	public TriColorVertView(Context context)
	{
		super(context);
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		setEGLContextClientVersion(2);
		setRenderer(new TriColorVertRenderer());
	}

}
