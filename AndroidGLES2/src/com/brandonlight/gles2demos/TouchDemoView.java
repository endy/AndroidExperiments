package com.brandonlight.gles2demos;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


public class TouchDemoView extends GLSurfaceView {
	
	TouchDemoRenderer mRenderer;
	
	public TouchDemoView(Context context)
	{
		super(context);
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		setEGLContextClientVersion(2);
		mRenderer = new TouchDemoRenderer();
		
		setRenderer(mRenderer);
	}
	
	public boolean onTouchEvent(final MotionEvent event)
    {

    	int width = getWidth();
    	int height = getHeight();

    	//android.util.Log.d("gldemos", s);
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		Log.d("INPUT", "Touch down");
    	}
    	
    	switch (event.getAction())
    	{
    		case MotionEvent.ACTION_MOVE:
    			Log.d("INPUT", "Touch Move");
    			break;
    		case MotionEvent.ACTION_UP:
    			Log.d("INPUT", "Touch up");
    			break;
    		case MotionEvent.ACTION_HOVER_ENTER:
    			Log.d("INPUT", "Hover Enter");
    			break;
    		
    		default:
    			break;
    	
    	}
    		
    	
    	return true;
    }


}
