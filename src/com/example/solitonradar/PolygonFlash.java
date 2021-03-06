package com.example.solitonradar;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import android.graphics.Color;



public class PolygonFlash {

	public PolygonOptions Polygon(LatLng latlng){
		/*追加 ポリゴンの描写用*/
		// 設定
		PolygonOptions options = new PolygonOptions();
		// 描画する座標を設定
		options.addAll(createRectangle(latlng, 0.0015, 0.0012)); 
		// 塗り 
		options.fillColor(0x110000FF); 
		// 線
		options.strokeColor(0xFF0000FF); 
		// 線幅
		options.strokeWidth(5); 
		return(options);
	}

	/*追加 ポリゴンの描写*/
	private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
	}

}