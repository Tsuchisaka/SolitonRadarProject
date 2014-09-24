package com.example.solitonradar;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.solitonradar.R.id;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.UiSettings;


public class MakeMap  extends FragmentActivity{
	public PlayersPosition pp;
	public BotController bc;
	private GoogleMap mMap;
	private static final int        LOCATION_TIME_OUT       = 10000; //10秒
	private CustomLocationManager   mCustomLocationManager;
	private Location                mCurrentLocation;
	public OrientationListener mOrientationListener;
	private Bitmap sightImageGreen, sightImageRed, sightImageYellow, sightImageBlue, sightImageSquare;//mapで表示する視界範囲の画像を用意しておく
	private long repeatInterval = 3000;//繰り返しの間隔（単位：msec）
	public int mode = 2;
	//mode:0 自分の位置情報をサーバで共有するゲームの通常モード
	//mode:1 サーバを介さず、自分の位置情報も取得しないbotモード
	//mode:2 デモ撮影用モード1
	//mode:3 デモ撮影用モード2
	private int mSoundId;
	private SoundPool mSoundPool;
	public boolean IamSnake = true;//
	public boolean SceneStart = false;//デモ撮影用のシーンを開始する

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pp = new PlayersPosition();
		LatLng latLng = new LatLng(35.049497, 135.780738);
		if(mode == 1) bc = new BotController(5,latLng);
		else if(mode == 2)bc = new BotController(1);
		else if(mode == 3)bc = new BotController(2);
		Resources resources = getResources();
		mCustomLocationManager = new CustomLocationManager(getApplicationContext());
		mOrientationListener = new OrientationListener();
		mOrientationListener.resume(getApplicationContext());
		sightImageGreen = BitmapFactory.decodeResource(resources, R.drawable.sightgreen);
		sightImageRed = BitmapFactory.decodeResource(resources, R.drawable.sightred);
		sightImageYellow = BitmapFactory.decodeResource(resources, R.drawable.sightyellow);
		sightImageBlue = BitmapFactory.decodeResource(resources, R.drawable.sightblue);
		sightImageSquare = BitmapFactory.decodeResource(resources, R.drawable.sightsquare);

		//pp.setMyPosition(0, 0.01, 0.30);//現在位置を取得してきてそれを入力してあげてサーバーに送る
		
		//地図作成する
		setContentView(R.layout.map);
		setUpMapIfNeeded();

		//役割取得・・・runawayはtrue，hunterはfalse
		Intent intent = getIntent();
		IamSnake = intent.getBooleanExtra("Role",false);
		ImageButton captured = (ImageButton) findViewById(R.id.captured);
		intent.putExtra("Role",false);
		captured.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mode == 0 || mode == 1){
					Intent intent = new Intent(MakeMap.this,WinningOrLosing.class );
					startActivity(intent);
				}else if(mode == 2 || mode == 3){
					SceneStart = true;
				}
			}
		});
		
		
		
		//一定時間ごと（今は500msec）に処理を行う．したい処理はTask.javaの中のrunに書いてください
		Timer timer = new Timer();
		TimerTask timerTask = new Task(this, this);
		timer.scheduleAtFixedRate(timerTask, 0, repeatInterval);
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

	public void setUpMap() {//地図を表示させる関数（中心位置や縮尺を選べる）
		/*スクロール操作禁止*/
		/*UiSettings settings = mMap.getUiSettings();
		settings.setScrollGesturesEnabled(false);*/

		LatLng latLng = new LatLng(35.049497, 135.780738);
		float zoom = 17;
		//初期位置の設定latLngが緯度経度，zoomで縮尺指定
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
	}

	public void ViweMap(LatLng latlng) {//地図を表示させる関数（中心位置や縮尺を選べる）
		LatLng latLng = new LatLng(35.049497, 135.780738);

		UiSettings settings = mMap.getUiSettings();
		/*スクロール操作禁止*/
		//settings.setScrollGesturesEnabled(false);
		// 回転ジェスチャー禁止
		settings.setRotateGesturesEnabled(false);
		mMap.clear();
		/*常に自分を真ん中に表示するには以下１行のコメントアウトはずす*/
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		
		if(mode == 0){
			int indexSnake = 0;
			for(int i=1; i<bc.allBotData.size();i++){
				PlayerData pd = pp.allPlayersData.get(i);
				if(pd.getIsSnake() == true){
					indexSnake = i;
					break;
				}
			}
			
			TextView timer = (TextView) findViewById(id.tap_text);
			int time = pp.allPlayersData.get(indexSnake).getTime();
			timer.setText(time + " sec left");
			time -= (int)(repeatInterval / 1000);
			pp.allPlayersData.get(indexSnake).setTime(time);
			
			for(int i=1; i<pp.allPlayersData.size();i++){
				PlayerData pd = pp.allPlayersData.get(i);
				LatLng ll = new LatLng(pd.getLatitude(),pd.getLongitude());
				MakeIcon icon = new MakeIcon();
				if(i==indexSnake){
					if(IamSnake == true) mMap.addMarker(icon.CreateIcon(2,ll));
				}else if(pp.mydata.getMacAddress() == pd.getMacAddress()){
					mMap.addMarker(icon.CreateIcon(3,ll));
				}else{
					mMap.addMarker(icon.CreateIcon(1,ll));
				}
				OverlaySight ms1 = new OverlaySight();
				if(i!=indexSnake){
					if(pp.seeSnakesForm(bc.allBotData.get(indexSnake), pd)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageRed));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);
					}else if(pp.hearSnakesFootsteps(bc.allBotData.get(indexSnake), pd, bc.isSnakeRunning)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageYellow));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);

						//警戒区域 ：角度はその人から見てスネークがいる方角を代入する
						OverlayCircle oc = new OverlayCircle();
						GroundOverlay overlayradar2 = mMap.addGroundOverlay(oc.CreateCircle(
								new LatLng(bc.allBotData.get(indexSnake).getLatitude(),bc.allBotData.get(indexSnake).getLongitude()),
								new LatLng(pd.getLatitude(),pd.getLongitude())));
						overlayradar2.setTransparency(0.01f);

					}else{
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
						overlay1.setTransparency(0.5f);
					} 
				}else{
					if(IamSnake == true){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageBlue));
						overlay1.setTransparency(0.5f);
					}
				}
			}
		}
		else if(mode == 1 || mode == 2 || mode == 3){
			/*
			//スネークを発見する判定のテストセット
			PlayerData p = bc.allBotData.get(5);
			p.setCoordinate(45, p.getLongitude(), p.getLatitude());
			p = bc.allBotData.get(1);
			p.setIsSnake(true);
			p = bc.allBotData.get(bc.allBotData.size()-1);
			p.setIsSnake(false);
			pp.seeSnakesForm(bc.allBotData.get(1), bc.allBotData.get(5));
			//テストセットここまで
			 */
			int indexSnake = 0;
			for(int i=1; i<bc.allBotData.size();i++){
				PlayerData pd = bc.allBotData.get(i);
				if(pd.getIsSnake() == true){
					indexSnake = i;
					break;
				}
			}
			
			TextView timer = (TextView) findViewById(id.tap_text);
			int time = bc.allBotData.get(indexSnake).getTime();
			timer.setText(time + " sec left");
			time -= (int)(repeatInterval / 1000);
			bc.allBotData.get(indexSnake).setTime(time);
			
			//botをマップに表示させる
			for(int i=1; i<bc.allBotData.size();i++){
				PlayerData pd = bc.allBotData.get(i);
				LatLng ll = new LatLng(pd.getLatitude(),pd.getLongitude());
				MakeIcon icon = new MakeIcon();
				if(i==indexSnake){
					if(IamSnake == true) mMap.addMarker(icon.CreateIcon(2,ll));
				}else if(i == 5){
					mMap.addMarker(icon.CreateIcon(3,ll));
				}else{
					mMap.addMarker(icon.CreateIcon(1,ll));
				}
				OverlaySight ms1 = new OverlaySight();
				if(i!=indexSnake){
					if(pp.seeSnakesForm(bc.allBotData.get(indexSnake), pd)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageRed));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);
					}else if(pp.hearSnakesFootsteps(bc.allBotData.get(indexSnake), pd, bc.isSnakeRunning)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageYellow));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);

						//警戒区域 ：角度はその人から見てスネークがいる方角を代入する
						OverlayCircle oc = new OverlayCircle();
						GroundOverlay overlayradar2 = mMap.addGroundOverlay(oc.CreateCircle(
								new LatLng(bc.allBotData.get(indexSnake).getLatitude(),bc.allBotData.get(indexSnake).getLongitude()),
								new LatLng(pd.getLatitude(),pd.getLongitude())));
						overlayradar2.setTransparency(0.01f);

					}else{
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
						overlay1.setTransparency(0.5f);
					} 
				}else{
					if(IamSnake == true){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageBlue));
						overlay1.setTransparency(0.5f);
					}
				}
			}
		}
		/*
                // マップに画像をオーバーレイ
		OverlayRadar or = new OverlayRadar();
		GroundOverlay overlayradar = mMap.addGroundOverlay(or.CreateRadar());
		overlayradar.setTransparency(0.55f);
		 */
		/*
		//追加 ポリゴンの描写用
		PolygonFlash pf = new PolygonFlash();
		mMap.addPolygon(pf.Polygon(latLng)); // 描画
		 */
	}


	private void getCurrentLocation(){
		mCustomLocationManager.getNowLocationData(LOCATION_TIME_OUT,
				new CustomLocationManager.LocationCallback() {

			// Timeoutすると実行
			@Override
			public void onTimeout() {
				if(mode == 0){
					Toast.makeText(getApplicationContext(),
							"Time out", Toast.LENGTH_SHORT).show();
				}
			}

			// 位置情報が得られると実行
			@Override
			public void onComplete(Location location) {
				if(location != null){
					if(mode == 0){
						/*
						//すべてのオーバーレイを削除
						mMap.clear();

						mCurrentLocation = location;
						LatLng LL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

						//地図上にボットとポリゴン再描写
						ViweMap(LL);

						MakeIcon miMP = new MakeIcon();
						mMap.addMarker(miMP.CreateIcon(1,LL));

						OverlaySight ms = new OverlaySight();
						GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(1,LL, sightImageGreen)); 
						overlay.setTransparency(0.5f);
						*/
						
						pp.mydata.setCoordinate(pp.mydata.getDirection(), 
								mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
						
						Log.d("LoAR", "Current Lat, Long;"
								+ mCurrentLocation.getLatitude()+","
								+ mCurrentLocation.getLongitude());
					}
				}
			}
		});
	}

	// 位置情報の取得を開始
	@Override
	protected void onResume() {
		super.onResume();
		getCurrentLocation();
		// 予め音声データを読み込む
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundId = mSoundPool.load(getApplicationContext(), R.raw.notice, 0);
	}

	@Override
	protected void onPause() {
		mOrientationListener.pause();
		super.onPause();
		// リリース
		mSoundPool.release();
	}

}
