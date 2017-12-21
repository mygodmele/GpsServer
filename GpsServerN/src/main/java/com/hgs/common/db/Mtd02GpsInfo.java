package com.hgs.common.db;

public class Mtd02GpsInfo{
	private Integer pukId;
	private String k01UserId;
	private Float longitude;
	private Float latitude;
	private String gpsArriveTime;
	private String speed;
	private String direction;
	private String postionlogo;
	private String mileage;
	private String lastUpdateTime;
	
	private String devicePsn;

	public Mtd02GpsInfo(){
	}

	public Mtd02GpsInfo(Integer pukId){
		this.pukId = pukId;
	}

	public void setPukId(Integer value) {
		this.pukId = value;
	}
	
	public Integer getPukId() {
		return this.pukId;
	}
	public void setK01UserId(String value) {
		this.k01UserId = value;
	}
	
	public String getK01UserId() {
		return this.k01UserId;
	}
	public void setLongitude(Float value) {
		this.longitude = value;
	}
	
	public Float getLongitude() {
		return this.longitude;
	}
	public void setLatitude(Float value) {
		this.latitude = value;
	}
	
	public Float getLatitude() {
		return this.latitude;
	}
	public void setGpsArriveTime(String value) {
		this.gpsArriveTime = value;
	}
	
	public String getGpsArriveTime() {
		return this.gpsArriveTime;
	}
	public void setSpeed(String value) {
		this.speed = value;
	}
	
	public String getSpeed() {
		return this.speed;
	}
	public void setDirection(String value) {
		this.direction = value;
	}
	
	public String getDirection() {
		return this.direction;
	}
	public void setPostionlogo(String value) {
		this.postionlogo = value;
	}
	
	public String getPostionlogo() {
		return this.postionlogo;
	}
	public void setMileage(String value) {
		this.mileage = value;
	}
	
	public String getMileage() {
		return this.mileage;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getDevicePsn() {
		return devicePsn;
	}

	public void setDevicePsn(String devicePsn) {
		this.devicePsn = devicePsn;
	}
	
}

