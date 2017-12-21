package com.hgs.gpsserver.outputmessage;

import com.hgs.common.utility.Byte2Hex;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.message.pojo.RegisterRealtimeData;

public class RegisterRealtimeDataOutputMessage extends OutputMessage {

	public RegisterRealtimeData data;
	
	public RegisterRealtimeDataOutputMessage(Object obj) {
		if(obj instanceof RegisterRealtimeData) {
			data = (RegisterRealtimeData) obj;
			messageType = OutputMessageType.Register;
		}
	}
	
	@Override
	public String getMessageName() {
		return OutputMessageType.Register.name();
	}

	@Override
	public void encode() {
		buffer = JT808Util.genRegisterReplyOutputMessage(data);
		String bufStr = Byte2Hex.Bytes2HexString(buffer.array());
		logger.debug("register return message is <{}> ",bufStr);
	}

}
