package com.ww.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ww.consumemedia.AcetateIntent;
import com.ww.consumemedia.AudioIntent;
import com.ww.consumemedia.ImageIntent;
import com.ww.record.AudioRecorder;
import com.ww.record.TextRecorder;
import com.ww.record.VideoRecorder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class WeatherWalksActivity extends MapActivity {
  
	private static final int MENU_EXIT = 0;
	String keypass = "8C:2A:DE:5D:B9:DC:70:D5:E1:1A:BB:BF:5F:38:4C:EC";
	String keypassMac = "DA:DC:A7:EA:03:C3:F6:8E:3E:83:03:0F:A2:92:8B:DF";
	
	
	String androidAPIMapKey = "android:apiKey=0-Tzck7k4Ku6pUt5qpNt3zCiTPbY0_D4Ufs-n3w";
	AttributeSet attrib;
	MapView mapView;
	SensorControl sc;
	polygonOverlay po;
	GeoPoint [] gp;
	MapController mControl;
	ArrayList<PolyVPair> polygons = new ArrayList<PolyVPair>();
	RouteOverlay rol;
	GeoPoint[] routeGP;
	Context context;
	Button mediaButton;
	MyLocationOverlay locationOverlay;
	ImageButton audioButton;
	ImageButton cameraButton;
	ImageButton textButton;
	Button refreshButton;
	ArrayList<String> mediaAssets = new ArrayList<String>();
	boolean setSatelliteView;
	Button mapViewSatelliteButton;
	int permURNnumber;
	Timer timer;
	Vibrator vibrate;
	Activity activity;
	boolean GPSFix;
	Timer GPSTraceTimer;
	//ArrayList<String> GPSTrace;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        
        context = this;
        
        activity = this;
                
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(false);
       // mapView.setSatellite(true);
        
        locationOverlay = new MyLocationOverlay(this, mapView);
        
        mControl = mapView.getController();
        
        final List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        final ItemizedOverlays itemizedoverlay = new ItemizedOverlays(drawable, this,mapView);
        mapOverlays.add(locationOverlay);
        
      
        
        //read in polygon file
       
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Weather Walks/polygonData1.csv");
        File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/Weather Walks/routedata.csv");
        
        LoadInPolygons(file);
        readInRouteFromFile(file2);
        readInMediaTextFile();
        
        //record GPS Trace
        System.out.println("External Storage = " + Environment.getExternalStorageDirectory());
  
        
        
      // double myLocLat = 54.58015;
      // double myLocLon = -3.16660;
        
        //GeoPoint myLocation = new GeoPoint((int)(myLocLat*1E6),(int)(myLocLon*1E6));
         
       
       
        sc = new SensorControl(this, this, mControl, mapView);
       
        //OverlayItem overlayitem = new OverlayItem(myLocation, "Location", "myLocation");
       
     
        po = new polygonOverlay(polygons, mapView, true, this);
        rol = new RouteOverlay(mapView, routeGP, true);
       
        
        sc.setPolygonOVerlay(po);
      
                  
        mapOverlays.add(po);
        mapOverlays.add(rol);
        
        GPSFix = false;
        
        
       // po.setLocation(myLocation);
        
        //mControl.animateTo(myLocation);
        
        
        
       /** GPSTraceTimer = new Timer();
        GPSTraceTimer.schedule(new TimerTask(){
        	public void run(){
        		double GPSdouble[] = new double[2];
        		long currentTime = System.currentTimeMillis()/1000;
        		
        			
        			ArrayList<String> GPSTrace = new ArrayList<String>();
        			GPSdouble[0] = sc.getGPS()[0];
        			GPSdouble[1] = sc.getGPS()[1];
        			String tempString = GPSdouble[0] + "," + GPSdouble[1] + ","+ currentTime;
        			GPSTrace.add(tempString);
        			System.out.println("GPSTraceSize " + GPSTrace.size());
        			readAndWriteGPSTrace(GPSTrace);
        		
        	}
        }, 10000,60000);**/
       
       
        //set up time task
        
        permURNnumber = -1;
        
        timer = new Timer();
        
        mediaButton = (Button) findViewById(R.id.mediaButton);
        
        mediaButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				mapView.postInvalidate();
			
				int URNnumber = po.getcurrentURN();
				
				for (int i = 0; i < mediaAssets.size(); i++){
					String line = mediaAssets.get(i);
					String [] lineArray = new String[3];
					
					lineArray = line.split(",");
					
					int arrayURN = Integer.parseInt(lineArray[0]);
					
					if(arrayURN == URNnumber){
						
						String fileName = lineArray[1];
						int type = Integer.parseInt(lineArray[2]);
						
						System.out.println("URN = " + fileName);
						
						
						if(type == 1){
							
							startImageIntent(fileName);
						}
						
						if(type == 2){
							startVideoIntent(fileName);
								
							
						}
						
						if(type == 3){
							startAcetateIntent(fileName);
						}
						
					}
				
				}
				
				Toast.makeText(context, "Urn is " + URNnumber, Toast.LENGTH_SHORT).show();
				
				
				
			}
        	
        	
        });
        
        //
        
        
        //mapOverlays.add(itemizedoverlay);
        
        mapView.postInvalidate();
        
        //add buttons
        
        audioButton = (ImageButton) findViewById(R.id.recordAudioButton);
        
        audioButton.setImageResource(R.drawable.microphone_images);
        
        audioButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				
				startAudiorecorder();
				
			}
        	
        });
        
        cameraButton = (ImageButton) findViewById(R.id.takePhotoButton);
        
        cameraButton.setImageResource(R.drawable.camera_images); 
        
        cameraButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				startVideoRecorder();
			}
        	
        });
        
        textButton = (ImageButton) findViewById(R.id.recordTextButton);
        
        textButton.setImageResource(R.drawable.pen_images);
        
        textButton.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		startTextRecorder();
        	}
        });
        
        
        //button to update map
        refreshButton = (Button) findViewById(R.id.refreshMediaButton);
        
        refreshButton.setOnClickListener(new OnClickListener(){
        
			public void onClick(View v) {
				
				File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/recorded media/recorded media.txt");
				int count=0;
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					
					String line;
					String lineArray[] = new String[4];
					
					
						try {
						
							while((line = br.readLine())!=null){
							lineArray = line.split(",");
							int urn = Integer.parseInt(lineArray[0]);
							String name = lineArray[1];
							int lat = (int) (Double.parseDouble(lineArray[2]) * 1E6);
							int lon = (int) (Double.parseDouble(lineArray[3]) * 1E6);
							count++;
							
							GeoPoint gp = new GeoPoint(lat,lon);
							
							OverlayItem overlayitem = new OverlayItem(gp, urn+"", name);
							
							//String fileType = name.substring(name.length()-3, name.length());
							
							
							itemizedoverlay.addOverlayItem(overlayitem);
							
						}
						
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				
					
				 catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
				if (count > 0){
					mapOverlays.add(itemizedoverlay);
				}
				mapView.postInvalidate();
				
			}
			
        });
        
        
        mapViewSatelliteButton = (Button) findViewById (R.id.mapOrSatelliteButton);
        
        mapViewSatelliteButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        	
        		if(setSatelliteView == false){
        			mapView.setSatellite(true);
        			setSatelliteView = true;
        		}
        		
        		else{
        			mapView.setSatellite(false);
        			setSatelliteView = false;
        			
        		}
        		
        		
        	}

        });
             
    }
    
    protected void onResume(){
    	super.onResume();
    	locationOverlay.enableMyLocation();
    	sc.onResume();
    	
    	
    	
        timer = new Timer();
       
        timer.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					
					mapView.postInvalidate();
					
					int tempURNnumber = po.getcurrentURN();
					
					System.out.println("permURN Number = " +permURNnumber + " tempURNNumber " + tempURNnumber);
					
					
					if(tempURNnumber != permURNnumber){
						
					for (int i = 0; i < mediaAssets.size(); i++){
						String line = mediaAssets.get(i);
						String [] lineArray = new String[3];
						
						lineArray = line.split(",");
						
						int arrayURN = Integer.parseInt(lineArray[0]);
						
						permURNnumber = tempURNnumber;
						
						if(arrayURN == permURNnumber){
							
							String fileName = lineArray[1];
							int type = Integer.parseInt(lineArray[2]);
							System.out.println("media " + fileName);
							if(type == 1){
								vibrate.vibrate(1000);
								startImageIntent(fileName);
								
							}
							
							if(type == 2){
								vibrate.vibrate(1000);
								startVideoIntent(fileName);
									
								
							}
							
							if(type == 3){
								vibrate.vibrate(1000);
								startAcetateIntent(fileName);
								
							}
						}
							
							
							
						}
					}
				}
							
					
				
        			
        		
        }, 10000, 10000);
        

        
        
    }
    
    protected void onPause(){
    	super.onPause();
    	System.out.println("onPause");
    	//locationOverlay.disableMyLocation();
    	//sc.onDestroy();
    	timer.cancel();
    	
    }
    
    protected void onStop(){
    	super.onStop();
    	System.out.println("onStop");
    	//locationOverlay.disableMyLocation();
    	//sc.onDestroy();
    	timer.cancel();
    	//this.finish();
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	System.out.println("onDestroy");
    	timer.cancel();
    	//sc.onDestroy();
    	//locationOverlay.disableMyLocation();
    	//this.finish();
    }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void LoadInPolygons(File file){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			
			
			
			while((line = br.readLine())!=null){
				
				String[] templine = new String[3];
				templine = line.split(",");
				
				int tempUrn = Integer.parseInt(templine[0]);
				int lat = (int)(Double.parseDouble(templine[1])*1E6);
				int lon = (int)(Double.parseDouble(templine[2])*1E6);
				GeoPoint tempGP = new GeoPoint(lat,lon);
				
				PolyVPair tempPVP = new PolyVPair(tempUrn, tempGP);
				
				polygons.add(tempPVP);
							
				
			}
			
			br.close();
			
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		
		}

	public void readInRouteFromFile(File file){
		
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
			
			
			String line;
			
			int counter = 0;
									
			while((line = br.readLine())!=null){
						
				counter++;
							
			}
			
			routeGP = new GeoPoint[counter];
			
			br.close();
			
			br = new BufferedReader(new FileReader(file));
			counter = 0;
			
			while((line = br.readLine())!=null){
				String[] templine = new String[3];
				templine = line.split(",");
				int templat = (int) (Double.parseDouble(templine[0])*1E6);
				int templon = (int) (Double.parseDouble(templine[1])*1E6);
				
				GeoPoint gp = new GeoPoint(templat,templon);
						
				routeGP[counter] = gp;
				
				counter++;
						
			}
			
			
			
			br.close();
			
			} catch (FileNotFoundException e) {
			
				e.printStackTrace();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		
	}
	
	public void startAudiorecorder(){
		
	
		GeoPoint coords = locationOverlay.getMyLocation();
		
		if(locationOverlay.isMyLocationEnabled() == true && locationOverlay.getLastFix().getAccuracy()<100
				&& locationOverlay.getLastFix().getAccuracy()>0){
			
			double [] doubleCoords = new double[2];	
			doubleCoords[0] = coords.getLatitudeE6()/1E6;
			doubleCoords[1] = coords.getLongitudeE6()/1E6;
				//test coords
		
				
				Bundle b = new Bundle();
				b.putDoubleArray("Coords",doubleCoords);
				Intent i = new Intent(context, AudioRecorder.class);
				i.putExtras(b);
				startActivity(i);
		}
		
		else{
			Toast.makeText(context, "Please wait for Location", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void startVideoRecorder(){
		GeoPoint coords = locationOverlay.getMyLocation();
		
		//System.out.println("Position Accuracy" + locationOverlay.getLastFix().getAccuracy());
		
		if(locationOverlay.isMyLocationEnabled() == true && locationOverlay.getLastFix().getAccuracy()<100
				&& locationOverlay.getLastFix().getAccuracy()>0){
			
					double [] doubleCoords = new double[2];	
					doubleCoords[0] = coords.getLatitudeE6()/1E6;
					doubleCoords[1] = coords.getLongitudeE6()/1E6;
				//test coords
		
					
						Bundle b = new Bundle();
						b.putDoubleArray("Coords",doubleCoords);
						Intent i = new Intent(context, VideoRecorder.class);
						i.putExtras(b);
						startActivity(i);
		
		}
		else{
			Toast.makeText(context, "Please wait for accurate Location", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	
	public void startTextRecorder(){
		
		
		GeoPoint coords = locationOverlay.getMyLocation();
		if(locationOverlay.isMyLocationEnabled() == true && locationOverlay.getLastFix().getAccuracy()<100
				&& locationOverlay.getLastFix().getAccuracy()>0){
			
			double [] doubleCoords = new double[2];	
			doubleCoords[0] = coords.getLatitudeE6()/1E6;
			doubleCoords[1] = coords.getLongitudeE6()/1E6;
				//test coords
		
				
				Bundle b = new Bundle();
				b.putDoubleArray("Coords",doubleCoords);
				Intent i = new Intent(context, TextRecorder.class);
				i.putExtras(b);
				startActivity(i);
		}
		
		else{
			Toast.makeText(context, "Please wait for accurate Location", Toast.LENGTH_SHORT).show();
		}
					
	}
	
	
	private void readInMediaTextFile(){
		
		File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/medialist.txt");
	try {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		
		
		while((line = br.readLine()) !=null){
			
		mediaAssets.add(line);
			
		}
		
		br.close();		
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
		
	}
	
	private void readAndWriteGPSTrace(ArrayList<String> GPSTrace){
		File file = new File(Environment.getExternalStorageDirectory().getPath() +"/weather walks/GPSTrace.csv");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			
			
			while((line = br.readLine()) !=null){
				
			GPSTrace.add(line);
			System.out.println("ReadAndWrite " + GPSTrace.size());
				
			}
			
			br.close();		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			FileOutputStream fos = new FileOutputStream(file, false);
			PrintStream ps = new PrintStream(fos);
			
			for(int i = 0; i <GPSTrace.size();i++){
				ps.println(GPSTrace.get(i));
			}
			System.out.println("GPS Trace Written WW");
			ps.close();
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	private void startVideoIntent(String file){
		
		Bundle b = new Bundle();
		b.putString("fileName","/Weather Walks/assets/" + file);
		
		Intent i = new Intent(context,AudioIntent.class);
		
		i.putExtras(b);
		
		startActivity(i);
		
		
	}
	
private void startImageIntent(String file){
		
		Bundle b = new Bundle();
		b.putString("fileName", "/Weather Walks/assets/"+file);
		
		Intent i = new Intent(context,ImageIntent.class);
		
		i.putExtras(b);
		
		startActivity(i);
		
		
	}

private void startAcetateIntent(String file){
		
		Bundle b = new Bundle();
		b.putString("fileName", file);
		
		Intent i = new Intent(context, AcetateIntent.class);
		
		i.putExtras(b);
		
		startActivity(i);
}


public boolean onCreateOptionsMenu(Menu menu){
    menu.add(0, MENU_EXIT, 3, "Exit").setIcon(android.R.drawable.ic_menu_delete);
   
    return true;
}

public boolean onOptionsItemSelected(MenuItem item){
    
    boolean handled = false;

    switch (item.getItemId()){
    case MENU_EXIT:
    	locationOverlay.disableMyLocation();
    	sc.onDestroy();
    	timer.cancel();
    	this.finish();
    }
	return handled;
    	
    }


}