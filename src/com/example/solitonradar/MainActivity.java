package com.example.solitonradar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import java.util.*;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.nifty.cloud.mb.*;
import android.app.AlertDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
/*追加*/
import com.google.android.gms.maps.model.PolygonOptions;
import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import android.location.Criteria;
import android.location.LocationListener;


public class MainActivity extends ActionBarActivity {

	PlayersPosition pp;
	private GoogleMap mMap;
	private static final int        LOCATION_TIME_OUT       = 10000; //10秒
    private CustomLocationManager   mCustomLocationManager;
    private Location                mCurrentLocation;

    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//サーバーに接続するための処理
		NCMB.initialize(this, "480c1f99d7b45ae9459d50f303e95af736fe32392b914235b624c542d54ccf10", "29f491b4e283238a7ea6c18c1b369d9b39f8d507d7c3e30325fb48c4e14515e4");

		pp = new PlayersPosition();
		mCustomLocationManager = new CustomLocationManager(getApplicationContext());
  		//pp.setMyPosition(0, 0.01, 0.30);//現在位置を取得してきてそれを入力してあげてサーバーに送る
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();//地図作成する
		/*
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.net);
        setContentView(imageView);
		 */

		/*String line = "test" + pp.allPlayersData.size();
		Log.i(this.getClass().getName(), "Reading allPlayersData...");
		Log.i(this.getClass().getName(), "allPlayersData.size() == " + pp.allPlayersData.size());
		for(int i=0; i<pp.allPlayersData.size(); i++){
			PlayerData pd = pp.allPlayersData.get(i);
			line += pd.getMacAddress() + "\n";
		}
		TextView _helloWorldWord = new TextView(this);
		_helloWorldWord.setText(line); 
		setContentView(_helloWorldWord);
		*/
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMapIfNeeded() {//XMｌにmapを表示させるための初期動作するための関数（ちょっとはっきり分かってない）
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {//地図を表示させる関数（中心位置や縮尺を選べる）
		LatLng latLng = new LatLng(35.049497, 135.780738);
		float zoom = 17;
		//初期位置の設定latLngが緯度経度，zoomで縮尺指定
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		
		//試しにアイコン設置
		MakeIcon mi = new MakeIcon();
		mMap.addMarker(mi.CreateIcon(1,latLng));
		
		// 画像及び位置情報設定
		/*GroundOverlayOptions options1 = new GroundOverlayOptions();
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.net);
		options1.image(bitmap);
		options1.anchor(0.5f,0.5f);
		options1.position(new LatLng(35.049497, 135.780738), 600.0f, 600.0f);
		// マップに画像をオーバーレイ
		GroundOverlay overlay = mMap.addGroundOverlay(options1);
		overlay.setTransparency(0.3f);*/
		
		/*追加 ポリゴンの描写用*/
		// 設定
		//PolygonOptions options2 = new PolygonOptions();
		// 描画する座標を設定
		//options2.addAll(createRectangle(new LatLng(35.049497, 135.780738), 0.0015, 0.0012));
		// 抜き
		//options.addHole(createRectangle(new LatLng(-22, 128), 1, 1));		
		//options2.fillColor(0x110000FF);// 塗り		
		//options2.strokeColor(0xFF0000FF);// 線
		//options2.strokeWidth(5); // 線幅
		//mMap.addPolygon(options2); // 描画
		
	}

	
	private void getCurrentLocation(){
        mCustomLocationManager.getNowLocationData(LOCATION_TIME_OUT,
                new CustomLocationManager.LocationCallback() {
                 
            // Timeoutすると実行
            @Override
            public void onTimeout() {
                Toast.makeText(getApplicationContext(),
                 "Time out", Toast.LENGTH_SHORT).show();
            }
 
            // 位置情報が得られると実行
            @Override
            public void onComplete(Location location) {
                if(location != null){
                    mCurrentLocation = location;
                    LatLng LL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(LL).title("(｀・ω・´)"));
                    Log.d("LoAR", "Current Lat, Long;"
                        + mCurrentLocation.getLatitude()+","
                        + mCurrentLocation.getLongitude());
                }
            }
        });
    }
	
	// 位置情報の取得を開始
    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }
 
    @Override
    protected void onPause() {
        super.onPause();
    }
    
	/*追加 ポリゴンの描写*/
	/*private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
				new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
	}*/
}




