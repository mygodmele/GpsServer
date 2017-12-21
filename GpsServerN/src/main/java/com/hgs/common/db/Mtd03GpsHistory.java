package com.hgs.common.db;

public class Mtd03GpsHistory{
	private Integer pukId;
	private String k01UserId;
	private Float latitude;
	private Float longitude;
	private String gpsArriveTime;
	private String speed;
	private String direction;
	private String postionlogo;
	private String mileage;

	public Mtd03GpsHistory(){
	}

	public Mtd03GpsHistory(Integer pukId){
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
	public void setLatitude(Float value) {
		this.latitude = value;
	}
	
	public Float getLatitude() {
		return this.latitude;
	}
	public void setLongitude(Float value) {
		this.longitude = value;
	}
	
	public Float getLongitude() {
		return this.longitude;
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
}

