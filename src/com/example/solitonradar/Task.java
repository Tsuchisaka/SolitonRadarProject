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
				//したい処理はここの中に書いてください．今は例でｌｏｇにコメントを出すようになっています．
				Log.d("now running! :) ","run !");
				mm.bc.BotMove();
				LatLng latLng = new LatLng(35.049497, 135.780738);
				mm.ViweMap(latLng);
			}
		});
	}
}
