package com.ww.start;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class polygonOverlay extends Overlay {
    //this is the array of vertices we need to draw
    GeoPoint[] vet;
    Point[] points;
    private float[] fVet;
    MapView mapView;
    Polygon polygon;
    int[] location;
    Point pLocation;
    GeoPoint myLocation;
    ArrayList<PolyVPair> polygons = new ArrayList<PolyVPair>();
    ArrayList<Polygon> polyForPNP = new ArrayList<Polygon>();
    private int numberOfPolys;
    ArrayList<Path> polyPaths = new ArrayList<Path>();
    int[] polygonSides;
    int currentURN;
	Drawable drawable;
	Context context;


  
    
   //everything is converted to graphics land
  
   
 
    //get the vertices
//for one polygon
    public polygonOverlay(GeoPoint[] v, MapView mapView, boolean shadow) {
        vet = v;
        points = new Point[v.length];
        fVet = new float[(v.length)*2];
        this.mapView = mapView;
       
      //  myLoc = new float[2];
       
        
    }
    
    //for multiple polygons
    
    //for multiple polygons
	    

	public polygonOverlay(ArrayList<PolyVPair> a, MapView mapView, boolean shadow, Context context){
    	polygons = a;
    	this.mapView = mapView;
    	this.context = context;
    	
    	drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
    	
    	
    	if(polygons.size() > 0){
    		numberOfPolys = 1;
    	}
    	
    	
    	
    	int firstn = 0;
    	int n = 0;
    	//work out number of polygons
    	
    	for(int i = 0; i < polygons.size(); i++){
    		if(i+1 < polygons.size()){
    		
    			firstn = polygons.get(i).getURN();
    			n = polygons.get(i+1).getURN();
    		//System.out.println(n);
    		if(n != firstn){
    			numberOfPolys++;
    			
    			firstn = n;
    			
    			}
    		}
    
    	}
    	
    	//create an array holding the number of sides for each polygon
    	
    	System.out.println("nop " + numberOfPolys);
    	
    	polygonSides = new int[numberOfPolys];
    	
    	int sideCounter = 1;
    	int arraycounter = 0;
    	
    	for(int i = 0; i < polygons.size(); i++){
    		
    		if(i+1 < polygons.size()){
    			firstn = polygons.get(i).getURN();
    			n = polygons.get(i+1).getURN();
    			
    		
    			if(firstn != n){
    				
    				polygonSides[arraycounter] = sideCounter;
    			
    		
    				firstn = n;
    			
    				arraycounter++;
    		
    				sideCounter = 1;
    			
    				//.out.println(n);
    			}
    			else{
    				sideCounter++;
    				}
    			}
    		
    		
    
    	}
    	
		polygonSides[arraycounter] = sideCounter;   		
		
		for (int i = 0; i < polygonSides.length; i++){
			System.out.println(polygonSides[i]);
		}
    	
    	
      	
    	//set up the geoPoints
    	
    	vet = new GeoPoint[polygons.size()];
    	
    	for (int i = 0; i < polygons.size(); i++){
    		
    		vet[i] = polygons.get(i).getGeoPoint();
    		
    	}
    	
    	points = new Point[vet.length];
    	fVet = new float[(vet.length)*2];
    	
    	
   }
    
    //this is how we draw it.
    @Override
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

        
        Paint mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(100);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);

        //draw polygon on screen

           
      int counter = 0;
        
   
        Path path = new Path();
        
        int k = 1;
        
        path.moveTo(fVet[0], fVet[1]);
        
      for (int i = 1; i < points.length; i++){
    	  
    
    	  try{
        	if( k  < polygonSides[counter] ){
        		
        		path.lineTo(fVet[i*2],fVet[(i*2)+1]);
        		k++;
        	}
        	
        	else{
        		path.moveTo(fVet[i*2],fVet[(i*2)+1]);
        		//i=i-1;
        		counter++;
        		k = 1;
        		
        	
        	}
    	  }
    	  catch(ArrayIndexOutOfBoundsException e){
    		 System.out.println("ArrayIndexOutOfBounds");
    	  }
     	
        }
      
      canvas.drawPath(path, mPaint);
      
      //get points for poly and build them for query
      
     /** counter = 0;
      
      k = 0;
      
      int xCoords[] = new int[polygonSides[counter]];
      
      int yCoords[] = new int[polygonSides[counter]];
    
      for (int i = 0; i < points.length; i++){
    	  
    	   
    	  
        	if( k  < polygonSides[counter] ){
        		
        		yCoords[k] = (int)fVet[(i*2)];
        		xCoords[k] = (int)fVet[(i*2)+ 1];
        		k++;
        	}
        	
        	else{
        		
        		polygon = new Polygon(counter+1 ,yCoords, xCoords, k);
        		
        		int contains = polygon.contains((int)conMyLocation()[0], (int)conMyLocation()[1]);
        		
        		currentURN = contains;
        		//System.out.println("contains " + contains);
        		
        		//System.out.println("number of sides = " + k);
        		
        		k = 0;
        		xCoords = new int[polygonSides[counter]];
        	      
        	    yCoords = new int[polygonSides[counter]];
        		
        	    counter++;
        	
        	}
        	
    	  }**/
    	
     
      
    }
    
    public class Polygon
    {
        // Polygon coordinates.
        private int[] polyY, polyX;
        int tempURN;

        // Number of sides in the polygon.
        private int polySides;

        /**
         * Default constructor.
         * @param px Polygon y coods.
         * @param py Polygon x coods.
         * @param ps Polygon sides count.
         * 
         */
        public Polygon( int Urn,int[] px, int[] py, int ps )
        {
            polyX = px;
            polyY = py;
            polySides = ps;
            tempURN = Urn;
        }

        /**
         * Checks if the Polygon contains a point.
         * @see "http://alienryderflex.com/polygon/"
         * @param x Point horizontal pos.
         * @param y Point vertical pos.
         * @return Point is in Poly flag.
         */
        public int contains( int x, int y )
        {
            boolean c = false;
            int i, j = 0;
            for (i = 0, j = polySides - 1; i < polySides; j = i++) {
                if (((polyY[i] > y) != (polyY[j] > y))
                    && (x < (polyX[j] - polyX[i]) * (y - polyY[i]) / (polyY[j] - polyY[i]) + polyX[i]))
                c = !c;
            }
            if(c == true){
            return tempURN;
            }
            
            return -1;
        } 
        
        
    }
    
    
    
    public void setLocation(GeoPoint location){
    	    	
     	myLocation = location;
    	
    	
    	
  }
    
    private float[] conMyLocation(){
    	float [] myLoc = new float[2];
    	Point point = new Point();
    	mapView.getProjection().toPixels(myLocation,point);
    	myLoc[0] = point.x;
    	myLoc[1] = point.y;
    	
    	return myLoc;
    	
    }
    
    public int getcurrentURN(){
    	int  counter = 0;
        int URN = -1; 
        int k = 0;
         
         int xCoords[] = new int[polygonSides[counter]];
         
         int yCoords[] = new int[polygonSides[counter]];
        
       
         for (int i = 0; i < points.length; i++){
        	 
           	if( k  < polygonSides[counter] ){
           		
           		yCoords[k] = (int)fVet[(i*2)];
           		xCoords[k] = (int)fVet[(i*2)+ 1];
           		//System.out.println("Here");
           		k++;
           	}
           	
           	else{
           		//System.out.println("polycounter " + polygonSides[counter]);
           		
           		//System.out.println("Here 2");
           		i = i - 1;
           	    
           		polygon = new Polygon(counter+1,yCoords, xCoords, k);
           		
           		
           		int contains = polygon.contains((int)conMyLocation()[0], (int)conMyLocation()[1]);
           		if (contains != -1){
           			URN = contains;
           		}
           		//System.out.println("contains " + contains);
           		
           		//System.out.println("number of sides = " + k);
           	
           		k = 0;
           		
           		counter++;
           	   
           		xCoords = new int[polygonSides[counter]];
           	      
           	    yCoords = new int[polygonSides[counter]];
           	    
                

           		
           	
           	}
           	
       	  }
       	
    	return URN;
    }
    


  
    
    
    
   

}