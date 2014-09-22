package com.example.solitonradar;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import android.graphics.Color;



public class PolygonFlash {

	public PolygonOptions Polygon(LatLng latlng){
		/*’Ç‰Á ƒ|ƒŠƒSƒ“‚Ì•`Ê—p*/
		// İ’è
		PolygonOptions options = new PolygonOptions();
		// •`‰æ‚·‚éÀ•W‚ğİ’è
		options.addAll(createRectangle(latlng, 0.0015, 0.0012)); 
		// “h‚è 
		options.fillColor(0x110000FF); 
		// ü
		options.strokeColor(0xFF0000FF); 
		// ü•
		options.strokeWidth(5); 
		return(options);
	}

	/*’Ç‰Á ƒ|ƒŠƒSƒ“‚Ì•`Ê*/
	private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
	}

}