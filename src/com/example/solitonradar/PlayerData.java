package com.example.solitonradar;

public class PlayerData {
	private String mac = "";
	private boolean roleSnake = false;
	private int time = 0;
	private boolean state = false;
	private int dir = 0;
	private double lon = 0;
	private double lat = 0;
	
	public void setMacAddress(String value){mac = value;}
	
	public void setIsSnake(boolean value){roleSnake = value;}
	
	public void setTime(int value){time = value;}
	
	public void setGameState(boolean value){state = value;}
	
	public String getMacAddress(){return mac;}
	
	public boolean getIsSnake(){return roleSnake;}
	
	public int getTime(){return time;}
	
	public boolean getGameState(){return state;}
	
	public void setCoordinate(int direction, double longitude, double latitude){
		dir = direction;
		lon = longitude;
		lat = latitude;
	}
	
	public int getDirection(){return dir;}
	
	public double getLongitude(){return lon;}
	
	public double getLatitude(){return lat;}
}
