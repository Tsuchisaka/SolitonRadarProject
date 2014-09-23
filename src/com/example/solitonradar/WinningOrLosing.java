package com.example.solitonradar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WinningOrLosing extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//役割取得・・・runawayはtrue，hunterはfalse
		Intent intent = getIntent();
		Boolean role = intent.getBooleanExtra("Role",false);
		if(role == true){
			setContentView(R.layout.agent_win);
		}
		else{
			setContentView(R.layout.guard_win);
		}

	}
}
/*
 Intent intent = new Intent(MakeMap.this,WinningOrLosing.class );
 intent.putExtra("Role",true);
 startActivity(intent);
 */
