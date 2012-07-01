package com.brandonlight.gles2demos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class ParticleTestRenderer implements GLSurfaceView.Renderer {

	static {
        System.loadLibrary("native_test");
    }
	
    String mVsSource;
    String mFsSource;
    
    IntBuffer mIndexBuffer;

    FloatBuffer mPosVB;
    FloatBuffer mTexVB;
    int mPosVbId, mTexVbId, mVsId, mFsId, mProgramId;
   
    // particle data
    int mParticleCount;
    float[] mPositions;
    float[] mVelocities;
    float[] mTexCoords;
    
    public native void testPrint();
    public native void testGLES2();
    
    public ParticleTestRenderer()
    {
        //*
        mVsSource = new String();
        mVsSource = "precision highp float; \n" +
                    "attribute  vec3 in_Position;               \n" +
                    "attribute  vec2 in_TexCoord;               \n" +
                    "varying vec2 v_TexCoord;                   \n" +
                    "void main(void)                            \n" +
                    "{                                          \n" +
                    "   gl_PointSize = 4.0;                       \n" +
                    "   gl_Position = vec4(in_Position, 1.0);   \n" +
                    "   v_TexCoord = in_TexCoord;               \n" +
                    "}                                          \n";

        mFsSource = new String();
        mFsSource = //"#version 100 \n"+
                    "precision mediump float;\n" + 
                    "varying vec2 v_TexCoord;                         \n" +
                    "void main(void)                                  \n" +
                    "{                                                \n" +
                    "   gl_FragColor = vec4((v_TexCoord.x * 2.0), \n" +
                    "                       (v_TexCoord.y * 2.0), \n" +
                    "                       1.0,                      \n" +
                    "                       1.0);                     \n"+
                    "}                                                \n";
                    
    }

    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        updateParticles(0.01f);
        
        int posAttribLoc = GLES20.glGetAttribLocation(mProgramId, "in_Position");
        int texAttribLoc = GLES20.glGetAttribLocation(mProgramId, "in_TexCoord");
        
        GLES20.glEnableVertexAttribArray(posAttribLoc);
        GLES20.glEnableVertexAttribArray(texAttribLoc);
        checkGlError("vaPointer");
        
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mPosVbId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mParticleCount*3*4, mPosVB, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(posAttribLoc, 3, GLES20.GL_FLOAT, false, 3*4, 0);
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mTexVbId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mParticleCount*2*4, mTexVB, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(texAttribLoc, 2, GLES20.GL_FLOAT, false, 2*4, 0);
       
        GLES20.glUseProgram(mProgramId);
        //particleCount
        GLES20.glDrawElements(GLES20.GL_POINTS, mParticleCount, GLES20.GL_UNSIGNED_INT, mIndexBuffer);

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
     
    public void updateParticles(float timestep)
    {
    	//testPrint();
    	
    	
    	float bounds = 1.0f;
    	
    	timestep *= 3.0f;
    	
    	// calculate new velocity with x/'friction' or y/'gravity' constants
    	float gravityDecel = 4.0f * timestep;
    	//float xFriction = 3.0f * timestep;
    	
    	for (int i = 0; i < mParticleCount; ++i)
    	{
    		int posIndex = i * 3;
            int velIndex = i * 3;
            
    		mVelocities[velIndex+1] -= gravityDecel; // gravity is always negative
    		
    		float x = mPositions[posIndex+0] + (mVelocities[velIndex+0] * timestep);
    		mPositions[posIndex+0] = Math.min(bounds, Math.max(-bounds, x));

    		if ((mPositions[posIndex+0] <= -bounds) || (mPositions[posIndex+0] >= bounds))
            {
    			mVelocities[velIndex+0] *= -0.5f;
            }
     
    		float y = mPositions[posIndex+1] + (mVelocities[velIndex+1] * timestep);
    		mPositions[posIndex+1] = Math.min(bounds, Math.max(-bounds, y));
            
            // change direction
            if ((mPositions[posIndex+1] <= -bounds) || (mPositions[posIndex+1] >= bounds))
            {
            	mVelocities[velIndex+1] *= -0.5f;
            }
    	}
    	
        ByteBuffer posVb = ByteBuffer.allocateDirect(mParticleCount*3*4);
        posVb.order(ByteOrder.nativeOrder());
        mPosVB = posVb.asFloatBuffer();
        mPosVB.put(mPositions);
        mPosVB.position(0);
        
        ByteBuffer texVb = ByteBuffer.allocateDirect(mParticleCount*2*4);
        texVb.order(ByteOrder.nativeOrder());
        mTexVB = texVb.asFloatBuffer();
        mTexVB.put(mTexCoords);
        mTexVB.position(0);
    }
    
    public void initParticles(float impulseX, float impulseY)
    {
    	// initial layout of particles is in a 2D grid
    	int width = 25, height = 25; 
        mParticleCount = width * height;
        
        if (mPositions == null)
        {
        	mPositions  = new float[mParticleCount * 3];
        }
        if (mVelocities == null)
        {
        	mVelocities = new float[mParticleCount * 3];
        }
        if (mTexCoords == null)
        {
        	mTexCoords  = new float[mParticleCount * 2];
        }
        
        
    	for (int w = 0; w < width; ++w)
        {
            for (int h = 0; h < height; ++h)
            {
                int posIndex = (h * width * 3) + (w * 3);
                int velIndex = (h * width * 3) + (w * 3);
                int texIndex = (h * width * 2) + (w * 2);

                float posx = -1.0f + ((2.0f / (float)width) * w);
                float posy = -1.0f + ((2.0f / (float)height) * (float) h);

                mPositions[posIndex+0] = posx;
                mPositions[posIndex+1] = posy;
                mPositions[posIndex+2] = 0.5f;

                mTexCoords[texIndex+0] = w / (float)width;
                mTexCoords[texIndex+1] = 1.0f - h / (float)height;

                float magnitude = (float)Math.sqrt((double)(posx + impulseX)*(posx + impulseX) + (double)(posy + impulseY)*(posy + impulseY));

                mVelocities[velIndex+0] = (posx + impulseX) / magnitude;
                mVelocities[velIndex+1] = (posy + impulseY) / magnitude;
                mVelocities[velIndex+2] = 0;
            }
        }
    	
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) 
    {
    	testGLES2();
    	initParticles(0, 0);
        
        IntBuffer bufferIdBuffer = IntBuffer.allocate(2);
        GLES20.glGenBuffers(2, bufferIdBuffer);
        
        mPosVbId = bufferIdBuffer.get(0);   
        mTexVbId = bufferIdBuffer.get(1);

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        
        int[] indices = new int[mParticleCount];
        for (int i = 0; i < mParticleCount; ++i)
        {
            indices[i] = i;
        }
        
        mIndexBuffer = IntBuffer.allocate(mParticleCount);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        
        mVsId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(mVsId, mVsSource);
        GLES20.glCompileShader(mVsId);
        checkGlError("compile VS");
        
        String infoLog = GLES20.glGetShaderInfoLog(mVsId);
        Log.d("shaderLog", infoLog);

        mFsId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(mFsId, mFsSource);
        GLES20.glCompileShader(mFsId);
        checkGlError("compile FS");

        infoLog = GLES20.glGetShaderInfoLog(mFsId);
        Log.d("shaderLog", infoLog);
        
        mProgramId = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramId, mVsId);
        GLES20.glAttachShader(mProgramId, mFsId);

        GLES20.glLinkProgram(mProgramId);

        String programLog = GLES20.glGetProgramInfoLog(mProgramId);
        Log.d("programLog", programLog);
        checkGlError("link shaders");   
    }
    
    
    
    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ParticleTestRenderer", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
