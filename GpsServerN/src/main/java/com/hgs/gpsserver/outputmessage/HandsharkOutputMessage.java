package com.hgs.gpsserver.outputmessage;


import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.message.pojo.HandsharkRealtimeData;

public class HandsharkOutputMessage extends OutputMessage {

	public HandsharkRealtimeData data;
	
	public HandsharkOutputMessage(Object obj) {
		if(obj instanceof HandsharkRealtimeData) {
			data = (HandsharkRealtimeData) obj;
			messageType = OutputMessageType.Hands;
		}
	}
	@Override
	public String getMessageName() {
		return OutputMessageType.Hands.name();
	}

	@Override
	public void encode() {
		byte[] protobuf = new byte[]{data.crc,data.mainCmd,data.childCmd};
		buffer = OutputMessageUtil.genOutPutMessage(protobuf, (byte)0x21, (short) 5);
	}
	
}
