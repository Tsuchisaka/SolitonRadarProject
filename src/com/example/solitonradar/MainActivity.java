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


public class MainActivity extends ActionBarActivity {

	PlayersPosition pp;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NCMB.initialize(this, "480c1f99d7b45ae9459d50f303e95af736fe32392b914235b624c542d54ccf10", "29f491b4e283238a7ea6c18c1b369d9b39f8d507d7c3e30325fb48c4e14515e4");
        
        pp = new PlayersPosition();
        pp.setMyPosition(0, 0.01, 0.30);
        setContentView(R.layout.activity_main);
        /*
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.net);
        setContentView(imageView);
        */
        
        String line = "test" + pp.allPlayersData.size();
        Log.i(this.getClass().getName(), "Reading allPlayersData...");
        Log.i(this.getClass().getName(), "allPlayersData.size() == " + pp.allPlayersData.size());
        for(int i=0; i<pp.allPlayersData.size(); i++){
        	PlayerData pd = pp.allPlayersData.get(i);
        	line += pd.getMacAddress() + "\n";
        }
        TextView _helloWorldWord = new TextView(this);
        _helloWorldWord.setText(line); 
        setContentView(_helloWorldWord);
        
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
}
