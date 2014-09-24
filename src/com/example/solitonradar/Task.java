package com.example.solitonradar;

import java.util.TimerTask;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.content.Context;

public class Task extends TimerTask{
	private Handler handler;
	private Context context;
	private MakeMap mm;
	public Task(Context context, MakeMap makemap) {
		handler = new Handler();
		this.context = context;
		mm = makemap;
	}

	@Override
	public void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				//�����������͂����̒��ɏ����Ă��������D���͗�ł������ɃR�����g���o���悤�ɂȂ��Ă��܂��D
				Log.d("now running! :) ","run !");
				if(mm.mode == 1){
					mm.pp.mydata.setCoordinate(mm.mOrientationListener.getAzimuth(),
							mm.pp.mydata.getLongitude(), mm.pp.mydata.getLatitude());
					mm.pp.setMyPosition(mm.pp.mydata.getDirection(), 
							mm.pp.mydata.getLongitude(), mm.pp.mydata.getLatitude());
				}
				else if(mm.mode == 1) mm.bc.BotMove();
				else if(mm.mode == 2 && mm.SceneStart == true){
					mm.bc.BotMoveForSet1();
				}else if(mm.mode == 3 && mm.SceneStart == true){
					mm.bc.BotMoveForSet2();
				}
				LatLng latLng = new LatLng(35.049497, 135.780738);
				mm.ViweMap(latLng);
			}
		});
	}
}
