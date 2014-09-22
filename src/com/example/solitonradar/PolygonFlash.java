package com.example.solitonradar;

import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import android.graphics.Color;



public class PolygonFlash {

	public PolygonOptions Polygon(LatLng latlng){
		/*�ǉ� �|���S���̕`�ʗp*/
		// �ݒ�
		PolygonOptions options = new PolygonOptions();
		// �`�悷����W��ݒ�
		options.addAll(createRectangle(latlng, 0.0015, 0.0012)); 
		// �h�� 
		options.fillColor(0x110000FF); 
		// ��
		options.strokeColor(0xFF0000FF); 
		// ����
		options.strokeWidth(5); 
		return(options);
	}

	/*�ǉ� �|���S���̕`��*/
	private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
	}

}