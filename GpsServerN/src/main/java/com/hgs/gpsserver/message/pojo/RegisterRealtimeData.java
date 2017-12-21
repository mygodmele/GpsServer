package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.RegisterResult;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class RegisterRealtimeData extends BaseRealtimeData {
	
	public String provinceId;   //省域ID
	public String cityId;       //市县域ID
	public String manufacturerId; //制造商ID
	public String deviceType;     //终端型号
	public String deviceId;      //终端ID
	public byte lpColor;       //车牌颜色
	public String lpFlag;       //车牌标识
	public String authId;      //生成鉴权码ID，用于生成回复消息
	public RegisterResult registResult = RegisterResult.Failure; //注册结果
	

	public String getProvinceId() {
		return provinceId;
	}



	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}



	public String getCityId() {
		return cityId;
	}



	public void setCityId(String cityId) {
		this.cityId = cityId;
	}



	public String getManufacturerId() {
		return manufacturerId;
	}



	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}



	public String getDeviceType() {
		return deviceType;
	}



	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}



	public String getDeviceId() {
		return deviceId;
	}



	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public byte getLpColor() {
		return lpColor;
	}



	public void setLpColor(byte lpColor) {
		this.lpColor = lpColor;
	}



	public String getLpFlag() {
		return lpFlag;
	}



	public void setLpFlag(String lpFlag) {
		this.lpFlag = lpFlag;
	}



	public String getAuthId() {
		return authId;
	}



	public void setAuthId(String authId) {
		this.authId = authId;
	}


	public RegisterResult getRegistResult() {
		return registResult;
	}



	public void setRegistResult(RegisterResult registResult) {
		this.registResult = registResult;
	}


	@Override
	public void update(InputMessage inputMessage) {
		// TODO Auto-generated method stub

	}

}
