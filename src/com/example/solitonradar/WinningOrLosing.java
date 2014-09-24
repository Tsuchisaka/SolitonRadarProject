package com.example.solitonradar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class WinningOrLosing extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//役割取得・・・runawayはtrue，hunterはfalse
		Intent intent = getIntent();
		Boolean role = intent.getBooleanExtra("Role",false);
	
		if(role == true){
			setContentView(R.layout.agent_win);
			ImageButton back1 = (ImageButton) findViewById(R.id.back1);
			back1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WinningOrLosing.this,MainActivity.class );
					startActivity(intent);
				}
			});
		}
		else{
			setContentView(R.layout.guard_win);
			ImageButton back2 = (ImageButton) findViewById(R.id.back2);
			back2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WinningOrLosing.this,MainActivity.class );
					startActivity(intent);
				}
			});
		}

	}
}
/*
 Intent intent = new Intent(MakeMap.this,WinningOrLosing.class );
 intent.putExtra("Role",true);
 startActivity(intent);
 */
