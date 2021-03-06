package com.brandonlight.gles2demos;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class TouchDemoRenderer implements GLSurfaceView.Renderer {

    String mVsSource;
    String mFsSource;
    
    FloatBuffer mVbBuffer;
    ByteBuffer mIndexBuffer;
    
    
    int mVsId, mFsId, mProgramId, mVbId;

    public TouchDemoRenderer()
    {
        mVsSource = new String();
        mVsSource = "precision highp float; \n" +
                    "attribute  vec3 in_Position;               \n" +
                    "attribute  vec4 in_Color;                  \n" +
                    "varying vec4 v_Color;                   \n" +
                    "void main(void)                            \n" +
                    "{                                          \n" +
                    "   gl_Position = vec4(in_Position, 1.0);   \n" +
                    "   v_Color = in_Color; \n" +
                    "}                                          \n";

        mFsSource = new String();
        mFsSource = "precision mediump float;\n" + 
                    "varying vec4 v_Color;                     \n" +
                    "void main(void)                            \n" +
                    "{                                          \n" +
                    "   gl_FragColor = v_Color;                 \n"+
                    "}                                          \n";
    }

    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        int posAttribLoc = GLES20.glGetAttribLocation(mProgramId, "in_Position");
        int colorAttribLoc = GLES20.glGetAttribLocation(mProgramId, "in_Color");
        
        GLES20.glEnableVertexAttribArray(posAttribLoc);
        GLES20.glEnableVertexAttribArray(colorAttribLoc);
        checkGlError("vaPointer");
        
        GLES20.glVertexAttribPointer(posAttribLoc, 3, GLES20.GL_FLOAT, false, 7*4, 0);
        GLES20.glVertexAttribPointer(colorAttribLoc, 4, GLES20.GL_FLOAT, false, 7*4, 3*4);
       
        GLES20.glUseProgram(mProgramId);
        
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) 
    {
        // Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        
        float vbArray[] = 
        {
               -0.5f,  0.5f, 0.0f,       // v0 pos
                1.0f,  0.0f, 0.0f, 1.0f, // v0 color
                0.5f,  0.5f, 0.0f,       // v1 pos
                0.0f,  1.0f, 0.0f, 1.0f, // v1 color
                0.5f, -0.5f, 0.0f,       // v2 pos
                0.0f,  0.0f, 1.0f, 1.0f, // v2 color
               -0.5f, -0.5f, 0.0f,       // v3 pos
                1.0f,  1.0f, 1.0f, 1.0f, // v3 color
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(((4 * 3) + (4 * 4)) * 4);
        vbb.order(ByteOrder.nativeOrder());
        
        mVbBuffer = vbb.asFloatBuffer();
        mVbBuffer.put(vbArray);
        mVbBuffer.position(0);
        
        IntBuffer bufferIdBuffer = IntBuffer.allocate(1);
        GLES20.glGenBuffers(1, bufferIdBuffer);
        mVbId = bufferIdBuffer.get(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVbId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, ((4*3)+(4*4))*4, mVbBuffer, GLES20.GL_STATIC_DRAW);
         
        byte[] indices = {0, 1, 2, 0, 2, 3 };
        mIndexBuffer = ByteBuffer.allocate(6);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);

        mVsId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(mVsId, mVsSource);
        GLES20.glCompileShader(mVsId);
        
        String infoLog = GLES20.glGetShaderInfoLog(mVsId);
        Log.d("shaderLog", infoLog);

        mFsId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(mFsId, mFsSource);
        GLES20.glCompileShader(mFsId);

        infoLog = GLES20.glGetShaderInfoLog(mFsId);
        Log.d("shaderLog", infoLog);
        
        mProgramId = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramId, mVsId);
        GLES20.glAttachShader(mProgramId, mFsId);

        GLES20.glLinkProgram(mProgramId);

        String programLog = GLES20.glGetProgramInfoLog(mProgramId);
        Log.d("programLog", programLog);
        
    }
    
    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("TouchDemoRenderer", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

}
