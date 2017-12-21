package com.hgs.gpsserver.inputmessage;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.HandsharkRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

public class HandShakeInputMessage extends InputMessage {

    public HandShakeInputMessage(byte[] buffer) {
    	this.setMessageType(InputMessageType.Hands);
		this.setByteBuffer(buffer);
	}
	
	@Override
	public BaseRealtimeData decode() throws Exception {
		HandsharkRealtimeData realtimeData = new HandsharkRealtimeData();
		//包头
		byteArray.skip(2);
		
		//主信令
		realtimeData.mainCmd = byteArray.cutByte();
		
		//包长
		byteArray.skip(2);
		
		//终端序列号
		realtimeData.deviceSN = byteArray.cutHexString(4);
		
		//子信令
		realtimeData.childCmd = byteArray.getByte();
		
		//校验位
		realtimeData.crc = (byte) byteArray.getByteAt(10);
		
		return realtimeData;
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Hands, getRealtimeData());
	}

	@Override
	public String getMessageName() {
		return "handshark message";
	}
}
