package com.example.solitonradar;

import java.util.List;
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
import com.nifty.cloud.mb.FindCallback;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBObject;
import com.nifty.cloud.mb.NCMBQuery;


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
	public int mode = 4;
	//mode:0 自分の位置情報をサーバで共有するゲームの通常モード
	//mode:1 サーバを介さず、自分の位置情報も取得しないbotモード
	//mode:2 デモ撮影用モード1
	//mode:3 デモ撮影用モード2
	//mode:4 発表用デモモード
	private int mSoundId;
	private SoundPool mSoundPool;
	public boolean IamSnake = true;//
	public boolean SceneStart = false;//デモ撮影用のシーンを開始する
	private Task timerTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pp = new PlayersPosition();
		LatLng latLng = new LatLng(35.049497, 135.780738);
		if(mode == 1) bc = new BotController(5,latLng);
		else if(mode == 2)bc = new BotController(1);
		else if(mode == 3)bc = new BotController(2);
		else if(mode == 4){
			bc = new BotController(3);
			SceneStart = true;
		}
		Resources resources = getResources();
		//bc = new BotController(5,latLng);
		//makeBotDataBaseForTest();
		mCustomLocationManager = new CustomLocationManager(getApplicationContext());
		mOrientationListener = new OrientationListener();
		mOrientationListener.resume(getApplicationContext());
		sightImageGreen = BitmapFactory.decodeResource(resources, R.drawable.sightgreen);
		sightImageRed = BitmapFactory.decodeResource(resources, R.drawable.sightred);
		sightImageYellow = BitmapFactory.decodeResource(resources, R.drawable.sightyellow);
		sightImageBlue = BitmapFactory.decodeResource(resources, R.drawable.sightblue);
		sightImageSquare = BitmapFactory.decodeResource(resources, R.drawable.sightsquare);
		
		setTitle("HAS Radar");

		//地図作成する
		setContentView(R.layout.map);
		setUpMapIfNeeded();

		//役割取得・・・runawayはtrue，hunterはfalse
		Intent intent = getIntent();
		IamSnake = intent.getBooleanExtra("Role",false);
		ImageButton captured = (ImageButton) findViewById(R.id.captured);
		captured.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mode == 0 || mode == 1 || mode == 4){
					if(mode == 0){
						timerTask.enable = false;
						pp.deleteDataBase();
					}
					Intent intent2 = new Intent(MakeMap.this,WinningOrLosing.class );
					intent2.putExtra("Role",false);
					startActivity(intent2);
				}else if(mode == 2 || mode == 3){
					SceneStart = true;
				}
			}
		});

		if(mode == 0){
			pp.makeMASTER();
			NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("PlayerPosition");
			query.whereNotEqualTo("MacAdress", " ");
			query.findInBackground(new FindCallback<NCMBObject>() {
				@Override
				public void done(List<NCMBObject> result, NCMBException e){
					Log.d("findSnake", "IamSnake="+IamSnake);
					for(int i=0; i<result.size();i++){
						NCMBObject obj = result.get(i);
						Log.d("findSnake", obj.getString("MacAddress") + " is Snake = " + obj.getBoolean("SNAKE"));
						if(obj.getString("MacAddress") == "MASTER"){
							continue;
						}
						if(obj.getBoolean("SNAKE") == true){
							Log.d("findSnake", obj.getString("MacAddress") + "is Snake");
							Log.d("findSnake", "Sorry I am not Snake :(");
							IamSnake = false;
							break;
						}
					}
				}
			});
			pp.mydata.setIsSnake(IamSnake);
		}

		//一定時間ごと（今は500msec）に処理を行う．したい処理はTask.javaの中のrunに書いてください
		Timer timer = new Timer();
		timerTask = new Task(this, this);
		timerTask.enable = true;
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
		Log.d("now viewMap :) ","run 0");
		LatLng latLng = new LatLng(pp.mydata.getLatitude(), pp.mydata.getLongitude());

		UiSettings settings = mMap.getUiSettings();
		/*スクロール操作禁止*/
		//settings.setScrollGesturesEnabled(false);
		// 回転ジェスチャー禁止
		settings.setRotateGesturesEnabled(false);
		mMap.clear();
		/*常に自分を真ん中に表示するには以下１行のコメントアウトはずす*/
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		if(mode == 0 && pp.allPlayersData.size() >= 1){
			Log.d("now viewMap :) ","run 1");
			int indexSnake = 0;
			int indexMaster = 0;
			for(int i=1; i<pp.allPlayersData.size();i++){
				PlayerData pd = pp.allPlayersData.get(i);
				if(pd.getIsSnake() == true){
					Log.d("LookingViewMap",pd.getMacAddress() + " is Snake(" + i + ")");
					indexSnake = i;
				}
				if(pd.getMacAddress() == "MASTER"){
					Log.d("LookingViewMap",pd.getMacAddress() + " is MASTER(" + i + ")");
					indexMaster = i;
				}
			}
			Log.d("now viewMap :) ","run 2");

			TextView timer = (TextView) findViewById(id.tap_text);
			int time = pp.allPlayersData.get(indexMaster).getTime();
			Log.d("LookingViewMap", "Get Time:" + time);
			timer.setText(time + " sec left");
			time -= (int)(repeatInterval / 1000);
			Log.d("LookingViewMap", "Set Time:" + time);
			pp.allPlayersData.get(indexMaster).setTime(time);
			
			if(time < 0){
				if(mode == 0 || mode == 4){
					timerTask.enable = false;
					pp.deleteDataBase();
				}
				Intent intent2 = new Intent(MakeMap.this,WinningOrLosing.class );
				intent2.putExtra("Role",true);
				startActivity(intent2);
			}

			Log.d("now viewMap :) ","run 3");

			for(int i=1; i<pp.allPlayersData.size();i++){
				PlayerData pd = pp.allPlayersData.get(i);
				Log.d("now viewMap :) ","Mac:" + pd.getMacAddress());
				Log.d("LookingViewMap", pd.getMacAddress() + " IsSnake=" + pd.getIsSnake() + " setting Icon(" + i + ")");
				Log.d("LookingViewMap", "MyData Mac:" + pp.mydata.getMacAddress());
				LatLng ll = new LatLng(pd.getLatitude(),pd.getLongitude());
				MakeIcon icon = new MakeIcon();
				if(i==indexSnake){
					if(IamSnake == true) {
						mMap.addMarker(icon.CreateIcon(2,ll));
						Log.d("LookingViewMap", "Made Snake Icon");
					}
				}else if(pp.mydata.getMacAddress() == pd.getMacAddress()){
					mMap.addMarker(icon.CreateIcon(3,ll));
					Log.d("LookingViewMap", "Made Your Icon");
				}else{
					mMap.addMarker(icon.CreateIcon(1,ll));
					Log.d("LookingViewMap", "Made Genome Icon");
				}
				Log.d("SetIcon","Icon:" + pd.getMacAddress());
				Log.d("SetIcon","Locate:" + pd.getDirection() + ", " + pd.getLatitude() + ", " + pd.getLongitude());
				OverlaySight ms1 = new OverlaySight();
				if(i!=indexSnake){
					if(pp.seeSnakesForm(pp.allPlayersData.get(indexSnake), pd)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageRed));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);
					}else if(pp.hearSnakesFootsteps(pp.allPlayersData.get(indexSnake), pd, pp.IsSnakeRunning)){
						GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageYellow));
						overlay1.setTransparency(0.5f);
						// 再生
						mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
						((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);

						//警戒区域 ：角度はその人から見てスネークがいる方角を代入する
						OverlayCircle oc = new OverlayCircle();
						GroundOverlay overlayradar2 = mMap.addGroundOverlay(oc.CreateCircle(
								new LatLng(pp.allPlayersData.get(indexSnake).getLatitude(),pp.allPlayersData.get(indexSnake).getLongitude()),
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
		else if(mode == 1 || mode == 2 || mode == 3 || mode == 4){
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
				}else if(i == 1){
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
		}else{
			TextView timer = (TextView) findViewById(id.tap_text);
			timer.setText("読み込み中");
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

	private void makeBotDataBaseForTest(){
		for(int i=0; i<bc.allBotData.size();i++){
			NCMBObject obj;
			obj = new NCMBObject("PlayerPosition");
			obj.put("MacAddress", bc.allBotData.get(i).getMacAddress());
			Log.i(this.getClass().getName(), "New MacAddress.");

			Log.i(this.getClass().getName(), "Rewriting DB...");
			obj.put("longitude", bc.allBotData.get(i).getLongitude());
			obj.put("latitude", bc.allBotData.get(i).getLatitude());
			obj.put("direction", bc.allBotData.get(i).getDirection());
			obj.put("SNAKE", bc.allBotData.get(i).getIsSnake());
			//データ書き込みでAPIリクエスト消費
			obj.saveEventually();
		}
	}

}
