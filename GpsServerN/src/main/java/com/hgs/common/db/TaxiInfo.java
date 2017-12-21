package com.hgs.common.db;

public class TaxiInfo{
	private Integer id;
	private String taxiLicense;
	private String interphonePsn;
	private String passengerState;
	private Float currentLng;
	private Float currentLat;
	private Float lng;
	private Float lat;
	private Float currentSpeed;
	private Integer currentDirection;
	private String gpsReceiveTime;
	private Float mileage;
	private String isAlert;
	private String authCode;
	private String lastUpdateTime;

	public TaxiInfo(){
	}

	public TaxiInfo(Integer id){
		this.id = id;
	}
	
	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setTaxiLicense(String value) {
		this.taxiLicense = value;
	}
	
	public String getTaxiLicense() {
		return this.taxiLicense;
	}
	public void setInterphonePsn(String value) {
		this.interphonePsn = value;
	}
	
	public String getInterphonePsn() {
		return this.interphonePsn;
	}
	
	public String getPassengerState() {
		return passengerState;
	}

	public void setPassengerState(String passengerState) {
		this.passengerState = passengerState;
	}

	public void setCurrentLng(Float value) {
		this.currentLng = value;
	}
	
	public Float getCurrentLng() {
		return this.currentLng;
	}
	public void setCurrentLat(Float value) {
		this.currentLat = value;
	}
	
	public Float getCurrentLat() {
		return this.currentLat;
	}
	public void setCurrentSpeed(Float value) {
		this.currentSpeed = value;
	}
	
	public Float getCurrentSpeed() {
		return this.currentSpeed;
	}
	public void setCurrentDirection(Integer value) {
		this.currentDirection = value;
	}
	
	public Integer getCurrentDirection() {
		return this.currentDirection;
	}
	public void setGpsReceiveTime(String value) {
		this.gpsReceiveTime = value;
	}
	
	public String getGpsReceiveTime() {
		return this.gpsReceiveTime;
	}
	public void setMileage(Float value) {
		this.mileage = value;
	}
	
	public Float getMileage() {
		return this.mileage;
	}
	public void setIsAlert(String value) {
		this.isAlert = value;
	}
	
	public String getIsAlert() {
		return this.isAlert;
	}
	public void setAuthCode(String value) {
		this.authCode = value;
	}
	
	public String getAuthCode() {
		return this.authCode;
	}
	public void setLastUpdateTime(String value) {
		this.lastUpdateTime = value;
	}
	
	public String getLastUpdateTime() {
		return this.lastUpdateTime;
	}
	
}

