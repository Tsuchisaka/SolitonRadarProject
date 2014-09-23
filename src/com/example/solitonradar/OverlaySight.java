package com.example.solitonradar;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlaySight {


	public GroundOverlayOptions CreateSight(int radian, LatLng latlng, Bitmap sight){
		int rad = (radian - 45 + 360) % 360;
		Bitmap bmp = Bitmap.createBitmap(sight);
		Matrix mat = new Matrix();
		mat.postRotate(rad);//(mOrientationListener.getAzimuth());
		Bitmap rotateBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);  

		BitmapDescriptor descriptor = null;
		GroundOverlayOptions options = new GroundOverlayOptions(); 
		if(0<=rad && rad<=90){

			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);
			float y,D = 0;
			double a = Math.sin(Math.toRadians(rad));
			double b = Math.cos(Math.toRadians(rad));
			double yy=b/(a+b);
			y=(float)yy;
			double d=(a+b)*20;
			D=(float)d;
			
			options.anchor(0, y); 
			options.position(latlng, D, D); 

			if(true){
				Log.d("Tag","bpm.width="+bmp.getWidth()+"  bpm.height=" +bmp.getHeight() + " width="+rotateBmp.getWidth()+" height="+rotateBmp.getHeight());
			}

		}else if(90<rad&&rad<=180){

			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float x,D = 0;
			double a = Math.sin(Math.toRadians(rad-90));
			double b = Math.cos(Math.toRadians(rad-90));
			double xx=a/(a+b);
			x=(float)xx;
			double d=(a+b)*20;
			D=(float)d;

			options.anchor(x, 0); 
			options.position(latlng, D, D);  

		}else if(180<rad&&rad<=270){
			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float y,D = 0;

			double a = Math.sin(Math.toRadians(rad-180));
			double b = Math.cos(Math.toRadians(rad-180));
			double yy=a/(a+b);
			y=(float)yy;
			double d=(a+b)*20;
			D=(float)d;

			options.anchor(1, y); 
			options.position(latlng, D, D);  

		}else{
			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float x,D = 0;
			double a = Math.sin(Math.toRadians(rad-270));
			double b = Math.cos(Math.toRadians(rad-270));
			double xx=b/(a+b);
			x=(float)xx;
			double d=(a+b)*20;
			D=(float)d;

			options.anchor(x, 1); 
			options.position(latlng, D, D);  
		}

		options.image(descriptor); 
		return(options);
	}


}