package com.example.solitonradar;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class OverlayCircle {

	public GroundOverlayOptions CreateCircle(LatLng snake, LatLng genome){
		double snakerange;
		int dir;
		snakerange = Math.sqrt(
				(snake.latitude - genome.latitude) * (snake.latitude - genome.latitude)
				+(snake.longitude - genome.longitude) * (snake.longitude - genome.longitude)
				);
		double distance = (snake.latitude - genome.latitude) / snakerange;
		if(snake.longitude >= genome.longitude){
			dir = (int)(Math.toDegrees(Math.acos(distance)) + 0.5);//�l�̌ܓ�
		}else{
			dir = (int)(Math.toDegrees(Math.acos(distance)) + 0.5);//�l�̌ܓ�
			dir = 360 - dir;
		}
		double r = 0.0002;//���a
		LatLng latlng = new LatLng(genome.latitude + r * Math.cos(Math.toRadians(dir)),genome.longitude + r * Math.sin(Math.toRadians(dir)));
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