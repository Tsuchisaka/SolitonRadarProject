package com.example.solitonradar;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MakeIcon{
	public MarkerOptions CreateIcon(int RGO,LatLng latlng){//アイコンをつくる．RGOの中に指定したい色を入力（１：赤 ２：緑 ３：黄色）latlongに表示したい座標を入力
		MarkerOptions Icon =new MarkerOptions();
		Icon.position(latlng);
		Icon.anchor(0.5f, 0.5f);
		switch(RGO){
		case 1:
			Icon.icon(BitmapDescriptorFactory.fromResource(R.drawable.red1));
			break;
		case 2:
			Icon.icon(BitmapDescriptorFactory.fromResource(R.drawable.gr1));
			break;
		case 3:
			Icon.icon(BitmapDescriptorFactory.fromResource(R.drawable.o1));
			break;
		default:
		}
		return(Icon);//アイコンをつくるための情報をかえすaddMarker（）の引数に入れると使えます！
	}

}
