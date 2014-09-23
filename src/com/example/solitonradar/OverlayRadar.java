package com.example.solitonradar;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlayRadar {

	public GroundOverlayOptions CreateRadar(){
		GroundOverlayOptions options1 = new GroundOverlayOptions();
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.net);
		options1.image(bitmap);
		options1.anchor(0.5f,0.5f);
		options1.position(new LatLng(35.049497, 135.780738), 600.0f, 600.0f);
		return(options1);
	}
}

		