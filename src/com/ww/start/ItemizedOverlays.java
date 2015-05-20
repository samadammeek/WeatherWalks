package com.ww.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.ww.consumemedia.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class ItemizedOverlays extends ItemizedOverlay {

	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	Context mContext;
	MapView mapView;
	public ItemizedOverlays(Drawable defaultMarker, Context context, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
		mContext = context;
		this.mapView = mapView;
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	
	public void addOverlayItem(OverlayItem overlay){
		mOverlays.add(overlay);
		populate();
	}
	
	protected boolean onTap(int index) {
		  OverlayItem item = mOverlays.get(index);
		  
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  
		  dialog.setTitle("Item Number " + item.getTitle());
		  
		  final String itemString = item.getSnippet();
		  
		  dialog.setMessage("Would you like to view " + itemString + "?");
		  
		  
		  final String itemType = itemString.substring(itemString.length()-3,itemString.length()); 
		  System.out.println("ItemString = " + itemString);
		  System.out.println(itemType);
		 
		  
		  dialog.setPositiveButton("Show Media",new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				  
				if(itemType.equals("jpg") == true) {
					
					Bundle b = new Bundle();
					b.putString("fileName", "/Weather Walks/Recorded Media/" + itemString);
					
					Intent i = new Intent(mContext, ImageIntent.class);
					
					i.putExtras(b);
					
					mContext.startActivity(i);
					
				}
				if(itemType.equals("mp3") == true){
					Bundle b = new Bundle();
					b.putString("fileName", "/Weather Walks/Recorded Media/" + itemString);
					
					Intent i = new Intent(mContext, AudioIntent.class);
					
					i.putExtras(b);
					
					mContext.startActivity(i);
				}
				if(itemType.equals("txt") == true){
					AlertDialog.Builder textDialog = new AlertDialog.Builder(mContext);
					
					File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Weather Walks/Recorded Media/" + itemString);
					try {
						BufferedReader br = new BufferedReader(new FileReader(file));
						
						String line;
						//String lineArray[] = new String[4];
						String allText = "";
						
						try {
							while((line = br.readLine())!=null){
								allText = allText + " " + line;
								
							}
							
							br.close();
							textDialog.setMessage(allText);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					textDialog.setTitle("Contents of " + itemString+":");
					textDialog.show();
					
					
				}
				
				
			}
			  
		  });
		  
		  
		  dialog.setNegativeButton("Back", new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
			  
		  });
		  dialog.show();
		  return true;
		}
	
	 
	public boolean onTouchEvent(MotionEvent event, int index){
		
		handleLongPress(event,mOverlays.get(index));
		System.out.println("Index " + index);
		return true;
		
	}
	
	public OverlayItem getOverlayItem(int i){
		return mOverlays.get(i);
	}
	
	private void handleLongPress(final MotionEvent event, OverlayItem overlay){
		final int LONG_PRESS_TIMER = 500;
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			
			Toast.makeText(mContext, "Long Press", Toast.LENGTH_SHORT).show();
			
		}
	}

}
