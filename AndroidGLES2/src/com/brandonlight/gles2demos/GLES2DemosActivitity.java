package com.brandonlight.gles2demos;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.opengl.*;
import android.hardware.*;

import java.util.*;

public class GLES2DemosActivitity extends Activity {
    /** Called when the activity is first created. */
	
	GLSurfaceView mGLView;
	
	SensorManager mSensorManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mGLView = new ParticleTestView(this);
       //mGLView = new TriColorVertView(this);               
        setContentView(mGLView);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        
        for (int i = 0; i < sensors.size(); ++i)
        {
        	android.util.Log.i("gldemos", "Sensor Name: " + sensors.get(i).getName() + " Vendor:" + sensors.get(i).getVendor());
        	
        }      
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}