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
		Bitmap bmp = Bitmap.createBitmap(sight);
		Matrix mat = new Matrix();
		mat.postRotate(radian);//(mOrientationListener.getAzimuth());
		Bitmap rotateBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);  

		BitmapDescriptor descriptor = null;
		GroundOverlayOptions options = new GroundOverlayOptions(); 
		if(0<=radian && radian<=90){

			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);
			float y = 0;
			double a = Math.sin(Math.toRadians(radian));
			double b = Math.cos(Math.toRadians(radian));
			double yy=b/(a+b);
			y=(float)yy;

			options.anchor(0, y); 
			//options.anchor(0, 1);
			options.position(latlng, 20.0f, 20.0f); 

			if(true){
				Log.d("Tag","bpm.width="+bmp.getWidth()+"  bpm.height=" +bmp.getHeight() + " width="+rotateBmp.getWidth()+" height="+rotateBmp.getHeight());
			}

		}else if(90<radian&&radian<=180){

			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float x = 0;
			double a = Math.sin(Math.toRadians(radian-90));
			double b = Math.cos(Math.toRadians(radian-90));
			double xx=a/(a+b);
			x=(float)xx;

			options.anchor(x, 0); 
			//options.anchor(0, 1);
			options.position(latlng, 20.0f, 20.0f); 

		}else if(180<radian&&radian<=270){
			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float y = 0;

			double a = Math.sin(Math.toRadians(radian-180));
			double b = Math.cos(Math.toRadians(radian-180));
			double yy=a/(a+b);
			y=(float)yy;

			options.anchor(1, y); 
			//options.anchor(0, 1);
			options.position(latlng, 20.0f, 20.0f); 

		}else{
			descriptor = BitmapDescriptorFactory.fromBitmap(rotateBmp);

			float x = 0;
			double a = Math.sin(Math.toRadians(radian-270));
			double b = Math.cos(Math.toRadians(radian-270));
			double xx=b/(a+b);
			x=(float)xx;

			options.anchor(x, 1); 
			//options.anchor(0, 1);
			options.position(latlng, 20.0f, 20.0f); 
		}

		options.image(descriptor); 
		return(options);
	}


}