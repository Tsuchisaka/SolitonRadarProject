package com.example.solitonradar;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

public class BotController {
	//リストの先頭にゲーム情報、末尾にスネークの情報が入る
	public ArrayList<PlayerData> allBotData = new ArrayList<PlayerData>();
	private Random rnd = new Random();
	
	//botの人数と初期位置の座標を指定して起動する
	public BotController(int numberOfPlayers, LatLng baseLocation){
		if(numberOfPlayers < 0)numberOfPlayers*=-1;
		else if(numberOfPlayers == 0)numberOfPlayers++;
		
		for(int i=0; i<=numberOfPlayers;i++){
			PlayerData pd = new PlayerData();
			if(i==0){
				pd.setMacAddress("MASTER");
				pd.setIsSnake(false);
				pd.setCoordinate(0, baseLocation.longitude, baseLocation.latitude);
			}else if(i==numberOfPlayers){
				pd.setMacAddress("Snake");
				pd.setIsSnake(true);
				pd.setCoordinate(0, baseLocation.longitude, baseLocation.latitude);
			}else{
				pd.setMacAddress("Genome Soldier" + i);
				pd.setIsSnake(false);
				int angle = (45 * i) % 360;
				double longitude = baseLocation.longitude;
				double latitude = baseLocation.latitude;
				double range = 0.0000001;
				if(i % 4 == 0){
					longitude += range * i;  
				}else if(i%4==1){
					longitude -= range * i;
				}else if(i%4==2){
					latitude += range * i;
				}else{
					latitude -= range * i;
				}
				pd.setCoordinate(angle, longitude, latitude);
			}
			pd.setTime(300);
		}
	}

	//テストゲームスタート時に実行する
	public void GameStart(){
		for(int i=0;i<allBotData.size();i++){
			PlayerData pd = allBotData.get(i);
			pd.setGameState(true);
		}
	}

	//botを操作する
	public void BotMove(){
		double moverange = 0.000001;
		int dir = 0;
		double lon = 0;
		double lat = 0;
		//スネークの位置のヒントが出されているかのチェックを行う
		
		//スネークの移動方向を決定する
		//スネークが走るかどうかを決定する
		//スネークの移動位置を求める
		
		for(int i=1; i<allBotData.size()-1; i++){
			PlayerData pdg = allBotData.get(i);
			if(false){
				//スネークの位置のヒントが出ている場合の処理
			}else{
				//出ていない場合の処理
				//正面左右90度ずつの角度の範囲に進む確率を他の2倍に設定してある
				int ran = rnd.nextInt(540);
				//プレイヤの正面に対して相対的な進行方向を決める
				if(ran <= 180){
					dir = ran/2;
				}else if(ran >= 360){
					dir = (ran + 180)/2;
				}else{
					dir -= 90;
				}
				//絶対的な進行方向に変換する
				dir = (pdg.getDirection() + dir) % 360;
				
			}
		}
	}




}
