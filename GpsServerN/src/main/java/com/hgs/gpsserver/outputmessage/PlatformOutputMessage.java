package com.hgs.gpsserver.outputmessage;

import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.AuthRealtimeData;

public class PlatformOutputMessage extends OutputMessage {

	public BaseRealtimeData data;
	
	public PlatformOutputMessage(Object obj) {
		data = (BaseRealtimeData) obj;
		messageType = OutputMessageType.Platform;
	}
	
	@Override
	public String getMessageName() {
		return OutputMessageType.Platform.name();
	}

	@Override
	public void encode() {
		buffer = JT808Util.genPlatformCommonOutputMessage(data);
	}

}
