package com.example.solitonradar;

import java.util.Timer;
import java.util.TimerTask;

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
import com.google.android.gms.maps.UiSettings;


public class MakeMap  extends FragmentActivity{
	public PlayersPosition pp;
	public BotController bc;
	private GoogleMap mMap;
	private static final int        LOCATION_TIME_OUT       = 10000; //10�b
	private CustomLocationManager   mCustomLocationManager;
	private Location                mCurrentLocation;
	private OrientationListener mOrientationListener;
	private Bitmap sightImageGreen, sightImageRed, sightImageYellow, sightImageSquare;//map�ŕ\�����鎋�E�͈͂̉摜��p�ӂ��Ă���
	private long repeatInterval = 3000;//�J��Ԃ��̊Ԋu�i�P�ʁFmsec�j

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
		sightImageSquare = BitmapFactory.decodeResource(resources, R.drawable.sightsquare);
		
		//��莞�Ԃ��Ɓi����500msec�j�ɏ������s���D������������Task.java�̒���run�ɏ����Ă�������
		Timer timer = new Timer();
		TimerTask timerTask = new Task(this, this);
		timer.scheduleAtFixedRate(timerTask, 0, repeatInterval);
		
		//pp.setMyPosition(0, 0.01, 0.30);//���݈ʒu���擾���Ă��Ă������͂��Ă����ăT�[�o�[�ɑ���
		
		//�n�}�쐬����
		setContentView(R.layout.map);
		setUpMapIfNeeded();

		//�����擾�E�E�Erunaway��true�Chunter��false
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

	private void setUpMapIfNeeded() {//XM����map��\�������邽�߂̏������삷�邽�߂̊֐��i������Ƃ͂����蕪�����ĂȂ��j
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

	public void setUpMap() {//�n�}��\��������֐��i���S�ʒu��k�ڂ�I�ׂ�j
		/*�X�N���[������֎~*/
		/*UiSettings settings = mMap.getUiSettings();
		settings.setScrollGesturesEnabled(false);*/

		LatLng latLng = new LatLng(35.049497, 135.780738);
		float zoom = 17;
		//�����ʒu�̐ݒ�latLng���ܓx�o�x�Czoom�ŏk�ڎw��
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

		/*
                //���߂�
		MakeIcon m = new MakeIcon();
		mMap.addMarker(m.CreateIcon(1,latLng));
		//���E�͈͓\��t�� ���߂�
		OverlaySight ms2 = new OverlaySight();
		GroundOverlay overlay2 = mMap.addGroundOverlay(ms2.CreateSight(90,latLng, sightImageGreen)); 
		overlay2.setTransparency(0.5f);
		//�x����� �F�p�x�͂��̐l���猩�ăX�l�[�N��������p�������� ���߂�
		OverlayHazard1 ms3 = new OverlayHazard1();
		GroundOverlay overlay3 = mMap.addGroundOverlay(ms3.CreateArea1(315,latLng, sightImageSquare)); 
		overlay3.setTransparency(0.4f);
		OverlayHazard2 ms4 = new OverlayHazard2();
		GroundOverlay overlay4 = mMap.addGroundOverlay(ms4.CreateArea2(315,latLng, sightImageSquare)); 
		overlay4.setTransparency(0.4f);
		*/
		if(bc.allBotData.size() == 0){
			//���߂�
			MakeIcon miMP = new MakeIcon();
			mMap.addMarker(miMP.CreateIcon(2,latLng));

			//���E�͈͓\��t��
			OverlaySight ms = new OverlaySight();
			GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(4,latLng, sightImageGreen)); 
			overlay.setTransparency(0.5f);

			// �}�b�v�ɉ摜���I�[�o�[���C
			OverlayRadar or = new OverlayRadar();
			GroundOverlay overlayradar = mMap.addGroundOverlay(or.CreateRadar());
			overlayradar.setTransparency(0.1f);
		}

		/*
		//�X�l�[�N�𔭌����锻��̃e�X�g�Z�b�g
		PlayerData p = bc.allBotData.get(5);
		p.setCoordinate(45, p.getLongitude(), p.getLatitude());
		p = bc.allBotData.get(1);
		p.setIsSnake(true);
		p = bc.allBotData.get(bc.allBotData.size()-1);
		p.setIsSnake(false);
		pp.seeSnakesForm(bc.allBotData.get(1), bc.allBotData.get(5));
		//�e�X�g�Z�b�g�����܂�
		*/

		int indexSnake = 0;
		for(int i=1; i<bc.allBotData.size();i++){
			PlayerData pd = bc.allBotData.get(i);
			if(pd.getIsSnake() == true){
				indexSnake = i;
				break;
			}
		}

		//bot���}�b�v�ɕ\��������
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


		/*�ǉ� �|���S���̕`�ʗp*/
		PolygonFlash pf = new PolygonFlash();
		mMap.addPolygon(pf.Polygon(latLng)); // �`��

	}

public void ViweMap(LatLng latlng) {//�n�}��\��������֐��i���S�ʒu��k�ڂ�I�ׂ�j
	        LatLng latLng = new LatLng(35.049497, 135.780738);

		UiSettings settings = mMap.getUiSettings();
		/*�X�N���[������֎~*/
		//settings.setScrollGesturesEnabled(false);
		// ��]�W�F�X�`���[�֎~
		settings.setRotateGesturesEnabled(false);
		mMap.clear();
		/*��Ɏ�����^�񒆂ɕ\������ɂ͈ȉ��P�s�̃R�����g�A�E�g�͂���*/
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

		if(bc.allBotData.size() == 0){
			//���߂�
			MakeIcon miMP = new MakeIcon();
			mMap.addMarker(miMP.CreateIcon(2,latLng));

			//���E�͈͓\��t��
			OverlaySight ms = new OverlaySight();
			GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(4,latLng, sightImageGreen)); 
			overlay.setTransparency(0.5f);

			// �}�b�v�ɉ摜���I�[�o�[���C
			OverlayRadar or = new OverlayRadar();
			GroundOverlay overlayradar = mMap.addGroundOverlay(or.CreateRadar());
			overlayradar.setTransparency(0.55f);
			
			// �~�`��
			OverlayCircle oc = new OverlayCircle();
			GroundOverlay overlayradar2 = mMap.addGroundOverlay(oc.CreateCircle(latLng));
			overlayradar2.setTransparency(0.01f);
			
		}
		/*
		//�X�l�[�N�𔭌����锻��̃e�X�g�Z�b�g
		PlayerData p = bc.allBotData.get(5);
		p.setCoordinate(45, p.getLongitude(), p.getLatitude());
		p = bc.allBotData.get(1);
		p.setIsSnake(true);
		p = bc.allBotData.get(bc.allBotData.size()-1);
		p.setIsSnake(false);
		pp.seeSnakesForm(bc.allBotData.get(1), bc.allBotData.get(5));
		//�e�X�g�Z�b�g�����܂�
	*/
		int indexSnake = 0;
		for(int i=1; i<bc.allBotData.size();i++){
			PlayerData pd = bc.allBotData.get(i);
			if(pd.getIsSnake() == true){
				indexSnake = i;
				break;
			}
		}

		//bot���}�b�v�ɕ\��������
		for(int i=1; i<bc.allBotData.size();i++){
			PlayerData pd = bc.allBotData.get(i);
			LatLng ll = new LatLng(pd.getLatitude(),pd.getLongitude());
			MakeIcon icon = new MakeIcon();
			if(i==indexSnake){
				mMap.addMarker(icon.CreateIcon(2,ll));
			}else if(i == 5){
				mMap.addMarker(icon.CreateIcon(3,ll, "" + pp.angleToSnake + ", " + pd.getDirection() + ", " + pp.testDis));
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
					
					//�x����� �F�p�x�͂��̐l���猩�ăX�l�[�N��������p��������
					OverlayHazard1 A = new OverlayHazard1();
					GroundOverlay overlay5 = mMap.addGroundOverlay(A.CreateArea1(315,latLng, sightImageSquare)); 
					overlay5.setTransparency(0.4f);
					OverlayHazard2 B = new OverlayHazard2();
					GroundOverlay overlay6 = mMap.addGroundOverlay(B.CreateArea2(315,latLng, sightImageSquare)); 
					overlay6.setTransparency(0.4f);

				}else{
					GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
					overlay1.setTransparency(0.5f);
				} 
			}else{
				GroundOverlay overlay1 = mMap.addGroundOverlay(ms1.CreateSight(pd.getDirection(),ll, sightImageGreen));
				overlay1.setTransparency(0.5f);
			}
		}
		/*
                // �}�b�v�ɉ摜���I�[�o�[���C
		OverlayRadar or = new OverlayRadar();
		GroundOverlay overlayradar = mMap.addGroundOverlay(or.CreateRadar());
		overlayradar.setTransparency(0.55f);
		*/
		/*
		//�ǉ� �|���S���̕`�ʗp
		PolygonFlash pf = new PolygonFlash();
		mMap.addPolygon(pf.Polygon(latLng)); // �`��
		*/
	}


	private void getCurrentLocation(){
		mCustomLocationManager.getNowLocationData(LOCATION_TIME_OUT,
				new CustomLocationManager.LocationCallback() {

			// Timeout����Ǝ��s
			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(),
						"Time out", Toast.LENGTH_SHORT).show();
			}

			// �ʒu��񂪓�����Ǝ��s
			@Override
			public void onComplete(Location location) {
				if(location != null){
					/*���ׂẴI�[�o�[���C���폜*/
					mMap.clear();

					mCurrentLocation = location;
					LatLng LL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

                                        /*�n�}��Ƀ{�b�g�ƃ|���S���ĕ`��*/
					ViweMap(LL);

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

	// �ʒu���̎擾���J�n
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
