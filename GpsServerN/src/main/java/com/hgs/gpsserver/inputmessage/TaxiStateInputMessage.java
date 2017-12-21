package com.hgs.gpsserver.inputmessage;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.TaxiStateRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

public class TaxiStateInputMessage extends InputMessage {

	public TaxiStateInputMessage(byte[] buffer) {
		this.setMessageType(InputMessageType.TaxiState);
		this.setByteBuffer(buffer);
	}
	
	@Override
	public String getMessageName() {
		return "taxi state";
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		TaxiStateRealtimeData realtimeData = new TaxiStateRealtimeData();
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
		
		//是否空载
		realtimeData.isIdle = byteArray.cutByte();
		
		//校验位
		realtimeData.crc = (byte) byteArray.getByteAt(10);
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		// TODO 执行存数据库操作
		
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.TaxiState, getRealtimeData());
	}

}
