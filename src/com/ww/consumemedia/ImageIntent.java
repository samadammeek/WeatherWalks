package com.ww.consumemedia;

import com.ww.start.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageIntent extends Activity{

	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.image);
	        
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        
	        Bundle b = new Bundle();
	        
	        b = getIntent().getExtras();
	       	               
	        String fileName = b.getString("fileName");
	        
	        System.out.println(fileName);
	        
	        ImageView im = new ImageView(this);
	        
	        String imagePath = Environment.getExternalStorageDirectory().getPath() + fileName;
	        //decode smaller thumbnail for display
	        
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        
	        options.inSampleSize = 4;
	        
	        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
	        
	        im.setImageBitmap(bitmap);
	        
	        setContentView(im);
	 
	 }
	 
	 public void onDestroy(){
		 super.onDestroy();
		 //System.out.println("OnDestroy ImageIntent");
		 //this.finish();
		 
	 }
	 
	 public void onPause(){
		 super.onPause();
		// System.out.println("OnPause ImageIntent");
		 //this.finish();
	 }
	 
	 public void onStop(){
		 super.onStop();
		 System.out.println("Image Intent OnStop");
		 
	 }
}
