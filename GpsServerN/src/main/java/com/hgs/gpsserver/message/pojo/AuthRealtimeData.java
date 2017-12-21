package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class AuthRealtimeData extends BaseRealtimeData {

	public String authId; //鉴权码
	
	@Override
	public void update(InputMessage inputMessage) {
		// TODO Auto-generated method stub

	}

}
