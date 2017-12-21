package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class JT808GpsRealtimeData extends BaseRealtimeData {
	
	private String alarmFlag; //报警标志
	private String statu;  //状态
	private double latitude; //百度纬度
	private double longitude; //百度经度
	private double lat; //维度
    private double lng; //经度
	private double altitude;  //海拔高度
	private double speed;  //速度
	private int direction; //方向
	private String dateTimer; //gps时间(yyyy/MM/dd HH:mm:ss)
	
	private String taxiState;  //车辆满空载状态
	private byte emerAlarmState; //紧急告警标志位
	
	//额外附加信息
	private double mileage;  //里程

	@Override
	public void update(InputMessage inputMessage) {
		// TODO Auto-generated method stub

	}

	public byte getEmerAlarmState() {
		return emerAlarmState;
	}

	public void setEmerAlarmState(byte emerAlarmState) {
		this.emerAlarmState = emerAlarmState;
	}


	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTaxiState() {
		return taxiState;
	}

	public void setTaxiState(String taxiState) {
		this.taxiState = taxiState;
	}

	public String getAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getDateTimer() {
		return dateTimer;
	}

	public void setDateTimer(String dateTimer) {
		this.dateTimer = dateTimer;
	}

	public double getMileage() {
		return mileage;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}
	
	

}
