package com.example.solitonradar;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.content.Intent;
import android.os.Bundle;
import com.nifty.cloud.mb.*;

/*なしじる*/
public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//サーバーに接続するための処理
		NCMB.initialize(this, "480c1f99d7b45ae9459d50f303e95af736fe32392b914235b624c542d54ccf10", "29f491b4e283238a7ea6c18c1b369d9b39f8d507d7c3e30325fb48c4e14515e4");
		// res/layout/top.xml を初期画面に
		setContentView(R.layout.activity_main);
		setTitle("HAS Radar");

		ImageButton btn1 = (ImageButton) findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 画面上の RadioGroup を取得,チェックされている RadioButton を取得
				RadioGroup rg = (RadioGroup)findViewById(R.id.role);
				Intent intent = new Intent(MainActivity.this,MakeMap.class );
				switch(rg.getCheckedRadioButtonId()){
				case R.id.runaway:
					intent.putExtra("Role",true);
					break;
				case R.id.hunter:
					intent.putExtra("Role",false);
					break;
				}
				startActivity(intent);
			}
		});
	}

}




