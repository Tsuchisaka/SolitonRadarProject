package com.example.solitonradar;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlayCircle {

	public GroundOverlayOptions CreateCircle(LatLng latlng){
		// �}�b�v�ɓ\��t���� BitmapDescriptor �𐶐�
		BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.circle);
		 
		// �\��t����ݒ�
		GroundOverlayOptions options = new GroundOverlayOptions();
		options.image(descriptor);
		options.anchor(0.5f, 0.5f);
		options.position(latlng, 50, 50);//�~�̒��a�w��(�P��m)
		options.image(descriptor); 
		return(options);
	}


}