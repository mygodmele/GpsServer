package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class DeviceCommonRespRealtimeData extends BaseRealtimeData {

	private String responseNum; //应答流水号
	private String responseID; //应答ID
	private byte result;   //结果
	
	
	public String getResponseNum() {
		return responseNum;
	}


	public void setResponseNum(String responseNum) {
		this.responseNum = responseNum;
	}


	public String getResponseID() {
		return responseID;
	}


	public void setResponseID(String responseID) {
		this.responseID = responseID;
	}


	public byte getResult() {
		return result;
	}


	public void setResult(byte result) {
		this.result = result;
	}


	@Override
	public void update(InputMessage inputMessage) {
		// TODO Auto-generated method stub

	}

}
