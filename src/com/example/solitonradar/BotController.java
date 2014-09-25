package com.example.solitonradar;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

public class BotController extends PlayersPosition{
	//リストの先頭にゲーム情報、末尾にスネークの情報が入る
	public ArrayList<PlayerData> allBotData = new ArrayList<PlayerData>();
	private Random rnd = new Random();
	private LatLng LatestSnakeLocation;//最新のスネークの居場所
	public boolean isSnakeRunning = false;//スネークが走ったかどうか
	private int sceneNum = 0;
	public String MyName = "";//ViewMapで自分を識別するための名前
	
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
				LatestSnakeLocation = new LatLng(baseLocation.latitude,baseLocation.longitude);
			}else{
				pd.setMacAddress("Genome Soldier" + i);
				pd.setIsSnake(false);
				int angle = (45 * i) % 360;
				double longitude = baseLocation.longitude;
				double latitude = baseLocation.latitude;
				double range = 0.00022;
				if(i % 4 == 0){
					longitude += range * i;
					//latitude += range * (rnd.nextInt(3) -1);
				}else if(i%4==1){
					longitude -= range * i;
					//latitude += range * (rnd.nextInt(3) -1);
				}else if(i%4==2){
					latitude += range * i;
					//longitude += range * (rnd.nextInt(3) -1);
				}else{
					latitude -= range * i;
					//longitude += range * (rnd.nextInt(3) -1);
				}
				pd.setCoordinate(angle, longitude, latitude);
			}
			pd.setTime(300);
			allBotData.add(pd);
		}
	}
	
	public BotController(int setNum){
		int numberOfPlayers = 2;
		int angle = 0;
		 
		if(setNum == 1){
			LatLng baseLocation = new LatLng(35.048471, 135.780973);
			numberOfPlayers = 2;
			for(int i=0; i<=numberOfPlayers;i++){
				PlayerData pd = new PlayerData();
				if(i==0){
					pd.setMacAddress("MASTER");
					pd.setIsSnake(false);
					pd.setCoordinate(0, baseLocation.longitude, baseLocation.latitude);
				}else if(i==numberOfPlayers){
					pd.setMacAddress("Snake");
					pd.setIsSnake(true);
					angle = 260;
					pd.setCoordinate(angle, baseLocation.longitude, baseLocation.latitude);
					LatestSnakeLocation = new LatLng(baseLocation.latitude,baseLocation.longitude);
				}else{
					pd.setMacAddress("Genome Soldier" + i);
					if(i == 1)MyName = pd.getMacAddress();
					pd.setIsSnake(false);
					double longitude = 135.780813;
					double latitude = 35.048607;
					angle = 185;
					pd.setCoordinate(angle, longitude, latitude);
				}
				pd.setTime(300);
				allBotData.add(pd);
			}
		}else if(setNum == 2){
			LatLng baseLocation = new LatLng(35.048618, 135.781390);
			numberOfPlayers = 2;
			for(int i=0; i<=numberOfPlayers;i++){
				PlayerData pd = new PlayerData();
				if(i==0){
					pd.setMacAddress("MASTER");
					pd.setIsSnake(false);
					pd.setCoordinate(0, baseLocation.longitude, baseLocation.latitude);
				}else if(i==numberOfPlayers){
					pd.setMacAddress("Snake");
					pd.setIsSnake(true);
					angle = 350;
					pd.setCoordinate(angle, baseLocation.longitude, baseLocation.latitude);
					LatestSnakeLocation = new LatLng(baseLocation.latitude,baseLocation.longitude);
				}else{
					pd.setMacAddress("Genome Soldier" + i);
					if(i == 1)MyName = pd.getMacAddress();
					pd.setIsSnake(false);
					double longitude = 135.781448;
					double latitude = 35.048539;
					angle = 330;
					pd.setCoordinate(angle, longitude, latitude);
				}
				pd.setTime(300);
				allBotData.add(pd);
			}
		}else if(setNum == 3){
			LatLng baseLocation = new LatLng(35.048911, 135.781539);
			numberOfPlayers = 2;
			for(int i=0; i<=numberOfPlayers;i++){
				PlayerData pd = new PlayerData();
				if(i==0){
					pd.setMacAddress("MASTER");
					pd.setIsSnake(false);
					pd.setCoordinate(0, baseLocation.longitude, baseLocation.latitude);
				}else if(i==numberOfPlayers){
					pd.setMacAddress("Snake");
					pd.setIsSnake(true);
					angle = 0;
					pd.setCoordinate(angle, baseLocation.longitude, baseLocation.latitude);
					LatestSnakeLocation = new LatLng(baseLocation.latitude,baseLocation.longitude);
				}else{
					pd.setMacAddress("Genome Soldier" + i);
					if(i == 1)MyName = pd.getMacAddress();
					pd.setIsSnake(false);
					double longitude = 135.781534;
					double latitude = 35.048846;
					angle = 180;
					pd.setCoordinate(angle, longitude, latitude);
				}
				pd.setTime(600);
				allBotData.add(pd);
			}
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
		double moverange = 0.00001;
		int dir = 0;
		double lon = 0;//経度
		double lat = 0;//緯度
		boolean findsnake = false;
		//スネークの位置のヒントが出されているかのチェックを行う
		PlayerData snake = allBotData.get(allBotData.size()-1);
		for(int i=1;i<allBotData.size()-1;i++){
			PlayerData pdg = allBotData.get(i);
			if(seeSnakesForm(snake, pdg)){
				findsnake = true;
			}else if(hearSnakesFootsteps(snake,pdg, isSnakeRunning)){
				findsnake = true;
			}
		}
		
		//スネークの移動方向を決定する
		//スネークが走るかどうかを決定する
		//スネークの移動位置を求める
		findsnake = false;
		for(int i=1; i<allBotData.size()-1; i++){
			PlayerData pdg = allBotData.get(i);
			if(findsnake){
				//スネークの位置のヒントが出ている場合の処理
			}else{
				//出ていない場合の処理
				//正面左右90度ずつの角度の範囲に進む確率を他の2倍に設定してある
				int ran = rnd.nextInt(300);
				int ran2 = rnd.nextInt(2);
				//プレイヤの正面に対して相対的な進行方向を決める
				if(ran <= 150){
					dir = ran/15;
				}else if(ran >= 310){
					dir = (int)((double)ran/2 + 0.5) -65;
				}else{
					dir = ran - 310 + 90;
				}
				if(ran2 == 1)dir = 360 - dir;
				//絶対的な進行方向に変換する
				dir = (pdg.getDirection() + dir) % 360;
				//移動後の位置を更新する
				lon = pdg.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
				lat = pdg.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
				pdg.setCoordinate(dir, lon, lat);
			}
		}
	}

	//セット1のbotを操作する
	public void BotMoveForSet1(){
		double lon = 0;
		double lat = 0;
		int dir = 0;
		double SpeedWalk = 0.00002;
		double SpeedRun = 0.00006;
		double SpeedStay = 0.0;
		double moverange = SpeedWalk;
		PlayerData gen = allBotData.get(1);
		PlayerData snk = allBotData.get(2);
		if(sceneNum == 0)snk.setTime(300);
		if(sceneNum < 5){
			isSnakeRunning = false;
			moverange = SpeedWalk;
			dir = gen.getDirection();
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
		}
		else if(sceneNum < 6){
			moverange = SpeedWalk;
			dir = 235;
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
		}
		else if(sceneNum < 7){
			moverange = SpeedWalk;
			dir = 260;
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
			dir = 270;
			lon = snk.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = snk.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			snk.setCoordinate(dir, lon, lat);
		}
		else if(sceneNum < 8){
			moverange = SpeedWalk;
			dir = 260;
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
			moverange = SpeedRun;
			isSnakeRunning = true;
			dir = 315;
			lon = snk.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = snk.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			snk.setCoordinate(dir, lon, lat);
		}
		else if(sceneNum < 13){
			moverange = SpeedStay;
			dir = 90;
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
			moverange = SpeedRun;
			dir = 350;
			lon = snk.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = snk.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			snk.setCoordinate(dir, lon, lat);
		}
		else{
			sceneNum = -1;
			isSnakeRunning = false;
			gen.setCoordinate(185, 135.780813, 35.048607);
			snk.setCoordinate(260, 135.780973, 35.048471);
		}
		sceneNum++;
	}

	public void BotMoveForSet2(){
		double lon = 0;
		double lat = 0;
		int dir = 0;
		double SpeedWalk = 0.00002;
		double SpeedRun = 0.00006;
		double SpeedStay = 0.0;
		double moverange = SpeedWalk;
		PlayerData gen = allBotData.get(1);
		PlayerData snk = allBotData.get(2);
		if(sceneNum == 0)snk.setTime(50);
		if(sceneNum < 3){
			isSnakeRunning = false;
		}
		else if(sceneNum < 7){
			moverange = SpeedWalk;
			dir = 330;
			lon = gen.getLongitude() + moverange * Math.sin(Math.toRadians(dir));
			lat = gen.getLatitude() + moverange * Math.cos(Math.toRadians(dir));
			gen.setCoordinate(dir, lon, lat);
		}
		else if(sceneNum < 20){
			
		}
		else{
			sceneNum = -1;
			isSnakeRunning = false;
			snk.setCoordinate(350, 135.781390, 35.048618);
			gen.setCoordinate(330, 135.781448, 35.048539);
		}
		sceneNum++;
	}
	
	public void BotMoveForSet3(){
		double lon = 0;
		double lat = 0;
		int dir = 0;
		double SpeedWalk = 0.00002;
		double SpeedRun = 0.00006;
		double SpeedStay = 0.0;
		double moverange = SpeedWalk;
		PlayerData gen = allBotData.get(1);
		PlayerData snk = allBotData.get(2);
		if(sceneNum == 0)snk.setTime(600);
		if(sceneNum < 2){
			isSnakeRunning = false;
			gen.setCoordinate(110, gen.getLongitude(), gen.getLatitude());
		}
		else if(sceneNum < 3){
			gen.setCoordinate(30, gen.getLongitude(), gen.getLatitude());
		}
		else if(sceneNum < 4){
			gen.setCoordinate(330, gen.getLongitude(), gen.getLatitude());
		}
		else if(sceneNum < 6){
			gen.setCoordinate(0, gen.getLongitude(), gen.getLatitude());
		}
		else if(sceneNum < 7){
			gen.setCoordinate(180, gen.getLongitude(), gen.getLatitude());
		}
		else{
			sceneNum--;
		}
		sceneNum++;
	}
}
