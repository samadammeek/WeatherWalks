package com.ww.start;

import com.google.android.maps.GeoPoint;

public class PolyVPair {
	
	private int urn;
	private GeoPoint gp;
	

	public PolyVPair(int urn, GeoPoint gp){
		this.urn = urn;
		this.gp = gp;
		
	}
	
	public int getURN(){
		return urn;
	}
	
	public GeoPoint getGeoPoint(){
		return gp;
	}
	
}
