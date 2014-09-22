package com.example.solitonradar;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlaySight {
	public GroundOverlayOptions CreateSight(int radian, LatLng latlng){
		BitmapDescriptor descriptor = null;
		GroundOverlayOptions options = new GroundOverlayOptions(); 
		switch(radian){
		case 1:
			descriptor = BitmapDescriptorFactory.fromResource(R.drawable.radian0); 
			options.anchor(0, 1); 
			options.position(latlng, 20.0f, 20.0f); 
			break;
		case 2:
			descriptor = BitmapDescriptorFactory.fromResource(R.drawable.radian90); 
			options.anchor(0, 0); 
			options.position(latlng, 20.0f, 20.0f); 
			break;
		case 3:
			descriptor = BitmapDescriptorFactory.fromResource(R.drawable.radian180); 
			options.anchor(1, 0); 
			options.position(latlng, 20.0f, 20.0f); 
			break;
		case 4:
			descriptor = BitmapDescriptorFactory.fromResource(R.drawable.radian270); 
			options.anchor(1, 1); 
			options.position(latlng, 20.0f, 20.0f); 
			break;
		default:
		}

		options.image(descriptor); 
		return(options);
	}


}