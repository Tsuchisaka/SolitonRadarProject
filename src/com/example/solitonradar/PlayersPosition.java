﻿package com.example.solitonradar;

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
import android.util.Log;

public class PlayersPosition {
	private PlayerData mydata;//自分のマックアドレスと座標と向きと役割の情報を保持（詳細はPlayerData参照）
	public ArrayList<PlayerData> allPlayersData = new ArrayList<PlayerData>();//全員のデーターを保存するためのリスト
	
	public PlayersPosition(){//初期化したい内容を書き込む
		mydata = new PlayerData();
		mydata.setMacAddress(MacAddress.getMacAddressString());
	}
	
	public String getMacAddress(){return mydata.getMacAddress();}
	
	public void setMyPosition(final int direction, final double longitude, final double latitude){
		//PlayerPositionクラスのMacAddressフィールドから自分のMacAddressを探す
		//クエリ獲得でAPIリクエスト消費
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("PlayerPosition");
		query.whereEqualTo("MacAddress", mydata.getMacAddress());
		query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> result, NCMBException e){
            	NCMBObject obj;
                if (result.isEmpty() != true){
                    //MacAddressが見つかった	
                	obj = result.get(0);
                	Log.i(this.getClass().getName(), "MacAddress was found.");
                } else {
                    //MacAddressが見つからなかった
                	obj = new NCMBObject("PlayerPosition");
                	obj.put("MacAddress", mydata.getMacAddress());
                	Log.i(this.getClass().getName(), "New MacAddress.");
                }
                Log.i(this.getClass().getName(), "Rewriting DB...");
            	obj.put("longgitude", longitude);
            	obj.put("latitude", latitude);
            	obj.put("direction", direction);
            	obj.put("SNAKE", mydata.getIsSnake());
            	//データ書き込みでAPIリクエスト消費
            	obj.saveEventually();
            }
        });
		
		//queryからゲームの情報（MacAddress:MASTER）と各プレイヤの情報を読み取る
		NCMBQuery<NCMBObject> query2 = NCMBQuery.getQuery("PlayerPosition");
		query2.whereNotEqualTo("MacAdress", " ");
		query2.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> result, NCMBException e){
            	allPlayersData.clear();
            	Log.i(this.getClass().getName(), "Reading DB...");
            	Log.i(this.getClass().getName(), "result.size() == " + result.size());
                for(int i=0; i<result.size();i++){
                	NCMBObject obj = result.get(i);
                	if(mydata.getIsSnake() == true && obj.getString("MacAddress") == "MASTER"){
                		obj.put("direction", mydata.getTime());
                		Log.i(this.getClass().getName(), "Rewrite MASTER Time.");
                		//データ書き込みでAPIリクエスト消費
                		obj.saveEventually();
                	}
                	PlayerData pd = new PlayerData();
                	pd.setMacAddress(obj.getString("MacAddress"));
                	pd.setIsSnake(obj.getBoolean("SNAKE"));
                	pd.setTime(obj.getInt("direction"));
                	pd.setGameState(obj.getBoolean("SNAKE"));
                	pd.setCoordinate(obj.getInt("direction"), obj.getDouble("longitude"), obj.getDouble("latitude"));
                	allPlayersData.add(pd);
                	Log.i(this.getClass().getName(), "Got " + pd.getMacAddress());
                }
                
                //残り時間とゲームの状況を同期
                for(int i=0; i<allPlayersData.size(); i++){
                	PlayerData pd = allPlayersData.get(i);
                	if (pd.getMacAddress() == "MASTER"){
                		mydata.setTime(pd.getTime());
                		mydata.setGameState(pd.getGameState());
                		break;
                	}
                }
            }
        });
	}
	
	public boolean seeSnakesForm(PlayerData snake, PlayerData genome, boolean isSnakeRunning){
		double snakerange;
		double findrange = 0.00002;
		snakerange = Math.sqrt(
				(snake.getLatitude() - genome.getLatitude()) * (snake.getLatitude() - genome.getLatitude())
				+(snake.getLongitude() - genome.getLongitude()) * (snake.getLongitude() - genome.getLongitude())
				);
		if(snakerange <= findrange && isSnakeRunning){
			return true;
		}
		return false;
	}
	
	public boolean hearSnakesFootsteps(PlayerData snake, PlayerData genome){
		double snakerange;
		double findrange = 0.00003;
		int dir;
		snakerange = Math.sqrt(
				(snake.getLatitude() - genome.getLatitude()) * (snake.getLatitude() - genome.getLatitude())
				+(snake.getLongitude() - genome.getLongitude()) * (snake.getLongitude() - genome.getLongitude())
				);
		if(snakerange <= findrange){
			if(snake.getLatitude() >= genome.getLatitude()){
				dir = (int)(Math.acos(genome.getLongitude() - snake.getLongitude()) + 0.5);//四捨五入
			}else{
				dir = (int)(Math.acos(genome.getLongitude() - snake.getLongitude()) + 0.5);//四捨五入
				dir = 360 - dir;
			}
			if(genome.getDirection() + 45 > 359){
				if((genome.getDirection() - 45 - 180)%360 <= (dir+180)%360 && (dir+180)%360 <= (genome.getDirection() + 45-180)%360){
					return true;
				}
			}else if(genome.getDirection() - 45 < 0){
				if((genome.getDirection() - 45+180)%360 <= (dir+180)%360 && (dir+180)%360 <= (genome.getDirection() + 45+180)%360){
					return true;
				}
			}else{
				if(genome.getDirection() - 45 <= dir && dir <= genome.getDirection() + 45){
					return true;
				}
			}
		}
		return false;
	}
	
}
		