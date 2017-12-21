package com.hgs.gpsserver.outputmessage;


import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.message.pojo.GPSRealtimeData;

public class GpsRealTimeDataOutputMessage extends OutputMessage {

	public GPSRealtimeData data;
	public GpsRealTimeDataOutputMessage(Object obj) {
		if(obj instanceof GPSRealtimeData) {
			data = (GPSRealtimeData) obj;
			messageType = OutputMessageType.GpsData;
		}
	}
	
	@Override
	public String getMessageName() {
		return OutputMessageType.GpsData.name();
	}

	@Override
	public void encode() {
		byte[] protobuf = new byte[]{data.crc,data.mainCmd,data.childCmd};
		buffer = OutputMessageUtil.genOutPutMessage(protobuf, (byte)0x21, (short) 5);
	}

}
