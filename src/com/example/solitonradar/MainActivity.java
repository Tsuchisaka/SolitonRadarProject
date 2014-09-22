package com.example.solitonradar;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import com.nifty.cloud.mb.*;



public class MainActivity extends ActionBarActivity {
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//サーバーに接続するための処理
		NCMB.initialize(this, "480c1f99d7b45ae9459d50f303e95af736fe32392b914235b624c542d54ccf10", "29f491b4e283238a7ea6c18c1b369d9b39f8d507d7c3e30325fb48c4e14515e4");
		// res/layout/top.xml を初期画面に
		setContentView(R.layout.activity_main);
		setTitle("画面1");

		Button btn1 = (Button) findViewById(R.id.btn1);

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,MakeMap.class );
				startActivity(intent);
			}
		});
	}

}




