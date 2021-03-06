package com.brandonlight.gles2demos;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.hardware.*;

public class ParticleTestView extends GLSurfaceView {

	private ParticleTestRenderer mRenderer;
	
    public ParticleTestView(Context context)
    {
        super(context);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        setEGLContextClientVersion(2);
        
        mRenderer = new ParticleTestRenderer();
        
        setRenderer(mRenderer);
        
        
    }
    
    public boolean onTouchEvent(final MotionEvent event)
    {

    	int width = getWidth();
    	int height = getHeight();

    	//android.util.Log.d("gldemos", s);
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		mRenderer.initParticles(1.0f - ((event.getX(0)/ (float)width) * 2.0f), 
    							    -1.0f * (1.0f - ((event.getY(0) / (float)height) * 2.0f)));
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
    
    public boolean onSensorChanged(SensorEvent event)
    {
    	return true;
    }
}
