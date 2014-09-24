package com.example.solitonradar;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlayCircle {

	public GroundOverlayOptions CreateCircle(LatLng latlng){
		// マップに貼り付ける BitmapDescriptor を生成
		BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.circle);
		 
		// 貼り付ける設定
		GroundOverlayOptions options = new GroundOverlayOptions();
		options.image(descriptor);
		options.anchor(0.5f, 0.5f);
		options.position(latlng, 50, 50);//円の直径指定(単位m)
		options.image(descriptor); 
		return(options);
	}


}