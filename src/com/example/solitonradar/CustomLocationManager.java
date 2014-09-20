package com.example.solitonradar;

import com.nifty.cloud.mb.LocationCallback;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

public class CustomLocationManager implements LocationListener {
	  // �O���[�o���ϐ�LocationManager�ȊO�͌���o�ꂵ�܂�
	  private LocationManager     mLocationManager;
	  private LocationCallback    mLocationCallback;
	  private Handler             mHandler            = new Handler();
	  private static final int    NETWORK_TIMEOUT     = 5000;
	   
	  // 1.�R���X�g���N�^��LocationManager���쐬
	  public CustomLocationManager(Context context) {
	      mLocationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
	  }
	 
	  // 2.�w��̕��@�iProvider;GPS�Ȃǁj�ňʒu�����擾���J�n
	  public void startLocation(String provider) {
	      mLocationManager.requestLocationUpdates(provider, 0, 0, this);
	  }

	// 1.Callback���\�b�hMainActivity��location��n���B
	  public static interface LocationCallback {
	      //����łĂ���onLocationChanged�ŌĂяo���܂��B
	      public void onComplete(Location location);
	       
	      //networkTimeOutRun�ŌĂяo���܂��B
	      public void onTimeout();
	  }
	   
	  // 2. ���̃��\�b�h��MainActivity����Ăяo���Ĉʒu�����擾���܂�
	  public void getNowLocationData(int delayMillis, LocationCallback locationCallback) {
	      this.mLocationCallback = locationCallback;
	      mHandler.postDelayed(gpsTimeOutRun, delayMillis);
	      startLocation(LocationManager.GPS_PROVIDER);
	  }
	   
	  // 3�D�ʒu���̎擾����߂܂��B
	  public void removeUpdate() {
	      mLocationManager.removeUpdates(this);
	  }
	   
	  // 4. GPS�ňʒu���擾��Timeout������Network�ł̎擾�ɐ؂�ւ��܂�
	  private Runnable gpsTimeOutRun = new Runnable() {
	      @Override
	      public void run() {
	          removeUpdate();
	          mHandler.postDelayed(networkTimeOutRun, NETWORK_TIMEOUT);
	          startLocation(LocationManager.NETWORK_PROVIDER);
	      }
	  };
	   
	  // 5�DNetwork�ł̎擾�Ɏ��s������Callback��Timeout���Ăяo���܂��B
	  private Runnable networkTimeOutRun = new Runnable() {
	      @Override
	      public void run() {
	          removeUpdate();
	          if (mLocationCallback != null) {
	              mLocationCallback.onTimeout();
	          }
	      }
	  };  
	 
	@Override
	public void onLocationChanged(Location location) {
		// 1.�n���h���[����Timeout��������菜��
	     mHandler.removeCallbacks(gpsTimeOutRun);
	     mHandler.removeCallbacks(networkTimeOutRun);
	      
	     // 2.�擾�����ʒu����Callback��onComplete�֓n���܂��B
	     if (this.mLocationCallback != null) {
	         this.mLocationCallback.onComplete(location);
	     }
	      
	     // 3.��Ɉʒu�����X�V�������Ƃ��͕s�v
	     removeUpdate();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	  
}