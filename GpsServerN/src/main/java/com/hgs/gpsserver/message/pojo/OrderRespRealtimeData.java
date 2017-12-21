package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class OrderRespRealtimeData extends BaseRealtimeData {

	private String respMsgNum;
	private byte result;
	
	
	public String getRespMsgNum() {
		return respMsgNum;
	}


	public void setRespMsgNum(String respMsgNum) {
		this.respMsgNum = respMsgNum;
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
