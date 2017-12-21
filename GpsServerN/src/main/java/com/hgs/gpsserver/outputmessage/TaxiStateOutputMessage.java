package com.hgs.gpsserver.outputmessage;

import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.message.pojo.TaxiStateRealtimeData;

public class TaxiStateOutputMessage extends OutputMessage {

	public TaxiStateRealtimeData data;
	
	public TaxiStateOutputMessage(Object obj) {
		if(obj instanceof TaxiStateRealtimeData) {
			data = (TaxiStateRealtimeData) obj;
			messageType = OutputMessageType.TaxiState;
		}
	}
	
	@Override
	public String getMessageName() {
		return OutputMessageType.TaxiState.name();
	}

	@Override
	public void encode() {
		byte[] protobuf = new byte[]{data.crc,data.mainCmd,data.childCmd};
		buffer = OutputMessageUtil.genOutPutMessage(protobuf, (byte)0x21, (short) 5);
	}

}
