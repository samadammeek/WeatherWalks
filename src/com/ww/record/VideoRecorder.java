package com.ww.record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ww.start.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class VideoRecorder extends Activity{
	protected static final String TAG = null;
	RelativeLayout cameraPreview;
	Preview preview;
	Button button;
	ArrayList<String> audioFileLines = new ArrayList<String>();
	int URN = 0;
	double[] coords = new double[2];
	Activity activity;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videorecorder);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        activity = this;
		
		cameraPreview = (RelativeLayout) findViewById (R.id.cameraPreview);
		
		preview = new Preview(this);
		
		cameraPreview.addView(preview);
		
		readInFile();
		
		Bundle b = getIntent().getExtras();
		
		coords = b.getDoubleArray("Coords");
		
		button = (Button) findViewById(R.id.takePhotoButton);
		
		button.setText("Take Picture");
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				preview.camera.takePicture(shutter, raw, jpeg);
				
				audioFileLines.add(URN + "," + URN+".jpg" + "," + coords[0] + "," + coords[1]);
				writeOutFile();
				onDestroy();
				
				  Timer time = new Timer();
				  time.schedule(new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						  activity.finish();
					}
					
				  }, 1000);
				
			  
			}
			
		});
		
	}
	final ShutterCallback shutter = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	final PictureCallback raw = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	final PictureCallback jpeg = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			
			FileOutputStream outStream = null;
			
			
			
			try {
				outStream = new FileOutputStream(String.format(
						Environment.getExternalStorageDirectory().getPath()+"/weather walks/recorded media/"+ URN+ ".jpg", System.currentTimeMillis()));
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				bitmap.compress(CompressFormat.JPEG, 75, outStream);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	};
	public void readInFile(){
		  
		  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt"
		  		);
		  
		  try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			while((line = br.readLine())!=null){
				
				audioFileLines.add(line);
	
				
			}
			URN = audioFileLines.size()+1;
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		//URN = audioFileLines.size(); 
		System.out.println("URN " + URN);
		  
	  }
	  
	  public void writeOutFile(){
		  
		  File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt"
				  );
		  
		  try {
			FileOutputStream fos = new FileOutputStream(file, false);
			PrintStream ps = new PrintStream(fos);
			
			
			for(int i = 0; i < audioFileLines.size();i++){
				ps.println(audioFileLines.get(i));
			}
			
			ps.close();
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
	}
	  
	  protected void onDestroy(){
		  super.onDestroy();
		  
	  }

}
