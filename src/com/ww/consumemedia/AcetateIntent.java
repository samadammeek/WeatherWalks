package com.ww.consumemedia;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ww.start.R;



public class AcetateIntent extends Activity{
	Preview preview;
	ImageView im;
	Bitmap bitmap;
	FrameLayout fl;
	Bundle b;
	
	public void onCreate(Bundle savedInstantState){
		super.onCreate(savedInstantState);
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
       		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.acetate);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        b = getIntent().getExtras();

        String fileName = b.getString("fileName");
        
        preview = new Preview(this);
        
        im = new ImageView(this);
        
        String imagePath = Environment.getExternalStorageDirectory().getPath() + "/Weather Walks/assets/" + fileName;
        
        bitmap = BitmapFactory.decodeFile(imagePath);
        
        fl = (FrameLayout) findViewById(R.id.FrameLayout01);
        
        im.setImageBitmap(bitmap);
                
        fl.addView(preview);

        
        fl.addView(im);
        
        
        
        
        
        
        
        
        
        
    }
		
		
	}


