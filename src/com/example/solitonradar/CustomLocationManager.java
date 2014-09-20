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
	  // グローバル変数LocationManager以外は後程登場します
	  private LocationManager     mLocationManager;
	  private LocationCallback    mLocationCallback;
	  private Handler             mHandler            = new Handler();
	  private static final int    NETWORK_TIMEOUT     = 5000;
	   
	  // 1.コンストラクタでLocationManagerを作成
	  public CustomLocationManager(Context context) {
	      mLocationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
	  }
	 
	  // 2.指定の方法（Provider;GPSなど）で位置情報を取得を開始
	  public void startLocation(String provider) {
	      mLocationManager.requestLocationUpdates(provider, 0, 0, this);
	  }

	// 1.CallbackメソッドMainActivityへlocationを渡す。
	  public static interface LocationCallback {
	      //後程でてくるonLocationChangedで呼び出します。
	      public void onComplete(Location location);
	       
	      //networkTimeOutRunで呼び出します。
	      public void onTimeout();
	  }
	   
	  // 2. このメソッドをMainActivityから呼び出して位置情報を取得します
	  public void getNowLocationData(int delayMillis, LocationCallback locationCallback) {
	      this.mLocationCallback = locationCallback;
	      mHandler.postDelayed(gpsTimeOutRun, delayMillis);
	      startLocation(LocationManager.GPS_PROVIDER);
	  }
	   
	  // 3．位置情報の取得をやめます。
	  public void removeUpdate() {
	      mLocationManager.removeUpdates(this);
	  }
	   
	  // 4. GPSで位置情報取得がTimeoutしたらNetworkでの取得に切り替えます
	  private Runnable gpsTimeOutRun = new Runnable() {
	      @Override
	      public void run() {
	          removeUpdate();
	          mHandler.postDelayed(networkTimeOutRun, NETWORK_TIMEOUT);
	          startLocation(LocationManager.NETWORK_PROVIDER);
	      }
	  };
	   
	  // 5．Networkでの取得に失敗したらCallbackのTimeoutを呼び出します。
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
		// 1.ハンドラーからTimeout処理を取り除く
	     mHandler.removeCallbacks(gpsTimeOutRun);
	     mHandler.removeCallbacks(networkTimeOutRun);
	      
	     // 2.取得した位置情報をCallbackのonCompleteへ渡します。
	     if (this.mLocationCallback != null) {
	         this.mLocationCallback.onComplete(location);
	     }
	      
	     // 3.常に位置情報を更新したいときは不要
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