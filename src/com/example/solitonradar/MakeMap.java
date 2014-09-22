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
import android.content.res.Resources;

import com.google.android.gms.maps.model.PolygonOptions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import android.location.Criteria;
import android.location.LocationListener;

public class MakeMap  extends FragmentActivity{
	PlayersPosition pp;
	private GoogleMap mMap;
	private static final int        LOCATION_TIME_OUT       = 10000; //10�b
    private CustomLocationManager   mCustomLocationManager;
    private Location                mCurrentLocation;
    private OrientationListener mOrientationListener;
    private Bitmap sightImage;

    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		pp = new PlayersPosition();
		Resources resources = getResources();
		mCustomLocationManager = new CustomLocationManager(getApplicationContext());
		mOrientationListener = new OrientationListener();
		mOrientationListener.resume(getApplicationContext());
		sightImage = BitmapFactory.decodeResource(resources, R.drawable.radian0);
  		//pp.setMyPosition(0, 0.01, 0.30);//���݈ʒu���擾���Ă��Ă������͂��Ă����ăT�[�o�[�ɑ���
		setContentView(R.layout.map);
		setUpMapIfNeeded();//�n�}�쐬����
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

	private void setUpMap() {//�n�}��\��������֐��i���S�ʒu��k�ڂ�I�ׂ�j
		LatLng latLng = new LatLng(35.049497, 135.780738);
		float zoom = 17;
		//�����ʒu�̐ݒ�latLng���ܓx�o�x�Czoom�ŏk�ڎw��
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		
		//�����ɃA�C�R���ݒu
		MakeIcon mi = new MakeIcon();
		mMap.addMarker(mi.CreateIcon(1,latLng));
		
		//���E�͈͓\��t��
		  OverlaySight ms = new OverlaySight();
		  GroundOverlay overlay = mMap.addGroundOverlay(ms.CreateSight(1,latLng, sightImage)); 
		  overlay.setTransparency(0.5f); 
		  
		  
		  /*�ǉ� �|���S���̕`�ʗp*/
		  PolygonFlash pf = new PolygonFlash();
		  mMap.addPolygon(pf.Polygon(latLng)); // �`��
		
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
                    mCurrentLocation = location;
                    LatLng LL = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                    MakeIcon miMP = new MakeIcon();
            		mMap.addMarker(miMP.CreateIcon(1,LL));
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