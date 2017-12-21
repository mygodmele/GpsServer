package com.hgs.common.db;


public class TaxiHistoryInfo{
	private Integer id;
	private Integer taxiId;
	private String interphonePsn;
	private Float historicalLng;
	private Float historicalLat;
	private Float lng;
	private Float lat;
	private Float historicalSpeed;
	private Integer historicalDir;
	private String gpsReceiveTime;

	public TaxiHistoryInfo(){
	}

	public TaxiHistoryInfo(Integer id){
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
	public void setTaxiId(Integer value) {
		this.taxiId = value;
	}
	
	public Integer getTaxiId() {
		return this.taxiId;
	}
	public void setInterphonePsn(String value) {
		this.interphonePsn = value;
	}
	
	public String getInterphonePsn() {
		return this.interphonePsn;
	}
	public void setHistoricalLng(Float value) {
		this.historicalLng = value;
	}
	
	public Float getHistoricalLng() {
		return this.historicalLng;
	}
	public void setHistoricalLat(Float value) {
		this.historicalLat = value;
	}
	
	public Float getHistoricalLat() {
		return this.historicalLat;
	}
	public void setHistoricalSpeed(Float value) {
		this.historicalSpeed = value;
	}
	
	public Float getHistoricalSpeed() {
		return this.historicalSpeed;
	}
	public void setHistoricalDir(Integer value) {
		this.historicalDir = value;
	}
	
	public Integer getHistoricalDir() {
		return this.historicalDir;
	}
	public void setGpsReceiveTime(String value) {
		this.gpsReceiveTime = value;
	}
	
	public String getGpsReceiveTime() {
		return this.gpsReceiveTime;
	}
	
}

