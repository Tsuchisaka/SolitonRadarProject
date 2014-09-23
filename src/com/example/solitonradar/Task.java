package com.example.solitonradar;

import java.util.TimerTask;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.content.Context;

public class Task extends TimerTask{
	private Handler handler;
	private Context context;
	public Task(Context context) {
		handler = new Handler();
		this.context = context;
	}

	@Override
	public void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				//したい処理はここの中に書いてください．今は例でｌｏｇにコメントを出すようになっています．
				Log.d("now running! :) ","run !");
			}
		});
	}
}
