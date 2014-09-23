package com.example.solitonradar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.gms.maps.model.GroundOverlay;


public class MakeMap  extends FragmentActivity{
	private PlayersPosition pp;
	private BotController bc;
	private GoogleMap mMap;
	private static final int        LOCATION_TIME_OUT       = 10000; //10秒
	private CustomLocationManager   mCustomLocationManager;
	private Location                mCurrentLocation;
	private OrientationListener mOrientationListener;
	private Bitmap sightImageGreen, sightImageRed, sightImageYellow;//mapで表示する視界範囲の画像を用意しておく

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pp = new PlayersPosition();
		LatLng latLng = new LatLng(35.049497, 135.780738);
		bc = new BotController(5,latLng);
		Resources resources = getResources();
		mCustomLocationManager = new CustomLocationManager(getApplicationContext());
		mOrientationListener = new OrientationListener();
		mOrientationListener.resume(getApplicationContext());
		sightImageGreen = BitmapFactory.decodeResource(resources, R.drawable.sightgreen);
		sightImageRed = BitmapFactory.decodeResource(resources, R.drawable.sightred);
		sightImageYellow = BitmapFactory.decodeResource(resources, R.drawable.sightyellow);
		//pp.setMyPosition(0, 0.01, 0.30);//現在位置を取得してきてそれを入力してあげてサーバーに送る
		setContentView(R.layout.map);
		setUpMapIfNeeded();//地図作成する

		//役割取得・・・runawayはtrue，hunterはfalse
		Intent intent = getIntent();
		Boolean role = intent.getBooleanExtra("Role",false);

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
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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

		if(bc.allBotData.size() == 0){
			//ためし
			MakeIcon miMP = new MakeIcon();
			mMap.addMarker(miMP.CreateIcon(2,latLng));

			//視界範囲貼り付け
			OverlaySight ms = new OverlaySight();
			GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(4,latLng, sightImageGreen)); 
			overlay.setTransparency(0.5f);
		}

		//スネークを発見する判定のテストセット
		PlayerData p = bc.allBotData.get(5);
		p.setCoordinate(270, p.getLongitude(), p.getLatitude());
		p = bc.allBotData.get(1);
		p.setIsSnake(true);
		p = bc.allBotData.get(bc.allBotData.size()-1);
		p.setIsSnake(false);
		pp.seeSnakesForm(bc.allBotData.get(1), bc.allBotData.get(5));
		//テストセットここまで

		int indexSnake = 0;
		for(int i=1; i<bc.allBotData.size();i++){
			PlayerData pd = bc.allBotData.get(i);
			if(pd.getIsSnake() == true){
				indexSnake = i;
				break;
			}
		}

		//botをマップに表示させる
		for(int i=1; i<bc.allBotData.size();i++){
			PlayerData pd = bc.allBotData.get(i);
			LatLng ll = new LatLng(pd.getLatitude(),pd.getLongitude());
			MakeIcon icon = new MakeIcon();
			if(i==indexSnake){
				mMap.addMarker(icon.CreateIcon(2,ll));
			}else if(i == 5){
				mMap.addMarker(icon.CreateIcon(3,ll, "" + pp.angleToSnake + ", " + pp.testDis));
			}else{
				mMap.addMarker(icon.CreateIcon(1,ll));
			}
			OverlaySight ms1 = new OverlaySight();
			if(i!=indexSnake){
				if(pp.seeSnakesForm(bc.allBotData.get(indexSnake), pd)){
					GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageRed));
					overlay1.setTransparency(0.5f);
				}else if(pp.hearSnakesFootsteps(bc.allBotData.get(indexSnake), pd, false)){
					GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageYellow));
					overlay1.setTransparency(0.5f);
				}else{
					GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
					overlay1.setTransparency(0.5f);
				} 
			}else{
				GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
				overlay1.setTransparency(0.5f);
			}
		}


		/*追加 ポリゴンの描写用*/
		PolygonFlash pf = new PolygonFlash();
		mMap.addPolygon(pf.Polygon(latLng)); // 描画

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
					MakeIcon miMP = new MakeIcon();
					mMap.addMarker(miMP.CreateIcon(1,LL));

					OverlaySight ms = new OverlaySight();
					GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(1,LL, sightImageGreen)); 
					overlay.setTransparency(0.5f);

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
		mOrientationListener.pause();
		super.onPause();
	}

}