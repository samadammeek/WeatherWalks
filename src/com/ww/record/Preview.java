package com.ww.record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder mHolder;
	public Camera camera;
	Parameters cameraParameters = null;
	List <String> list = new ArrayList<String>();
	
	public Preview(Context context) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters = camera.getParameters();
		//parameters.setPreviewSize(currentCameraSize[0],currentCameraSize[1]);
		//.out.println(currentCameraSize[1] + " " + currentCameraSize[0]);
		parameters.set("orientation", "landscape");
		//parameters.setPreviewFormat(ImageFormat.JPEG);
		parameters.setJpegQuality(50);
		parameters.setPreviewSize(640,480);
		camera.setParameters(parameters);
		camera.startPreview();		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
			camera.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera arg1) {
					Preview.this.invalidate();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		//camera.setDisplayOrientation(90);
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		
	}

}
