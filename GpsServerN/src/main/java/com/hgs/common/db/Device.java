package com.hgs.common.db;

public class Device {

	private Integer id;
	private String psn;
	private String authId;
	private String licensePlate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPsn() {
		return psn;
	}
	public void setPsn(String psn) {
		this.psn = psn;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}	
	
}
