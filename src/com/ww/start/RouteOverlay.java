package com.ww.start;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RouteOverlay extends Overlay {
	
	GeoPoint[] vet;
    Point[] points;
    private float[] fVet;
    MapView mapView;

	
	public RouteOverlay(MapView mapView, GeoPoint[] v, boolean shadow){
		 	vet = v;
	        points = new Point[v.length];
	        fVet = new float[(v.length)*2];
	        this.mapView = mapView;
		
		
	}
	
    public void draw(Canvas canvas, MapView mapv, boolean shadow) {

        super.draw(canvas, mapv, shadow);

        mapv = this.mapView;
        
        
        //set all the points to a point.
        for(int i = 0; i < points.length; i++) {
            points[i] = new Point();
        }

        //convert from the array of geoPoints to the array of points using the projection.
        for(int i = 0; i < vet.length; i++){
      	mapView.getProjection().toPixels(vet[i], points[i]);

        }

        //convert the point to the float array
        for(int i = 0; i < points.length; i++) {
        
        	fVet[2*i] = points[i].x;
        	fVet[(2*i)+1]  = points[i].y;
        }
    

        //create a array of int colors.
        int[] colorArray = new int[points.length];

        for(int i = 0; i < points.length; i++) {
            colorArray[i] = Color.RED;
        }

        //if we are drawing a shadow, then dont draw anything
        Paint mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);
        
        Path path = new Path();
        
        path.moveTo(fVet[0], fVet[1]);
        
        for (int i = 1; i < points.length; i++){
        	
        	path.lineTo(fVet[i*2], fVet[i*2+1]);
        	
        }
        
        canvas.drawPath(path, mPaint);
        
        
    }
}
