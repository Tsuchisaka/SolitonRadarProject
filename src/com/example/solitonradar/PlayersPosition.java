package com.example.solitonradar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.util.*;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.nifty.cloud.mb.*;

import android.app.AlertDialog;

public class PlayersPosition {
	private String mac;
	private boolean role_snake = false;
	private int time = 0;
	private boolean state = false;
	
	public PlayersPosition(){
		mac = MacAddress.getMacAddressString();
		
	}
	
	public String getMacAddress(){return mac;}
	
	public void setMyPosition(final int angle, final double X, final double Y){
		//PlayerPositionクラスのMacAddressフィールドから自分のMacAddressを探す
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("PlayerPosition");
		query.whereEqualTo("MacAddress", mac);
		query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> result, NCMBException e){
            	String data = Integer.toString(angle) + "," + Double.toString(X) + "," + Double.toString(Y);
                if (result.isEmpty() != true){
                    //MacAddressが見つかった	
                	NCMBObject obj = result.get(0);
                	obj.put("Data", data);
                	obj.saveEventually();
                } else {
                    //MacAddressが見つからなかった
                	NCMBObject obj = new NCMBObject("PlayerPosition");
                	obj.put("MacAddress", mac);
                    obj.put("Data", data);
                    obj.saveInBackground(); 
                }
            }
        });
	}
	
	
}