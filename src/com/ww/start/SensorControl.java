package com.ww.start;



import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SensorControl implements LocationListener{
	LocationManager lm;
	private double []locationG = new double[3];
	private polygonOverlay pol;
	MapController mControl;
	MapView mapView;
	int currentURN;
	
	GeoPoint GPLocation;
	Context context;

	public SensorControl(Context context){
		this.context = context;
	}


	
	public SensorControl(Context context, Activity activity, MapController mControl, MapView mapView){
	
	
		
	
		lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		//lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0 ,0 ,this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	
		
		this.mControl = mControl;
		
		this.mapView = mapView;
		
	}
	

	public void onLocationChanged(Location location) {
		
		
			locationG[0] = location.getLatitude();
			locationG[1] = location.getLongitude();
			locationG[2] = location.getAltitude();
			
			GPLocation = new GeoPoint((int)(locationG[0] * 1E6), (int)(locationG[1] * 1e6));
			pol.setLocation(GPLocation);
			
										
			mControl.animateTo(GPLocation);
			
			mapView.invalidate();
			
			if(pol.getcurrentURN() != -1){
				currentURN = pol.getcurrentURN();
			}
			
	
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
			
	}
	
	protected void onResume(){
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	protected void onStop(){
		lm.removeUpdates(this);
	}
	
	protected void onDestroy(){
		lm.removeUpdates(this);
	}
	
	public void setPolygonOVerlay(polygonOverlay pol){
		this.pol = pol;
		//locationG[0] = 52.93868148134221;
		//locationG[1] = -1.1955785751342773;
		//locationG[2] = 100;
		GeoPoint gp = new GeoPoint((int)(locationG[0] * 1E6), (int)(locationG[1] * 1e6));
		
		
		this.pol.setLocation(gp);
		
	}
	
	
	public double[] getGPS(){
		return locationG;
	}
	
	public GeoPoint getGPSGP(){
		GeoPoint gp = new GeoPoint((int)(locationG[0] * 1E6), (int) (locationG[1] * 1E6));
		return gp;
		
	
	}
	

	
	
	

	
	

}
