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
			dir = (int)(Math.toDegrees(Math.acos(distance)) + 0.5);//四捨五入
		}else{
			dir = (int)(Math.toDegrees(Math.acos(distance)) + 0.5);//四捨五入
			dir = 360 - dir;
		}
		double r = 0.0002;//半径
		LatLng latlng = new LatLng(genome.latitude + r * Math.cos(Math.toRadians(dir)),genome.longitude + r * Math.sin(Math.toRadians(dir)));
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