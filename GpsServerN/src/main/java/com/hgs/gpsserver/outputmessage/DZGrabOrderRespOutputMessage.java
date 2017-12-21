package com.hgs.gpsserver.outputmessage;

import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.message.pojo.OrderReportRealtimeData;

public class DZGrabOrderRespOutputMessage extends OutputMessage {

	public OrderReportRealtimeData data;
	
	public DZGrabOrderRespOutputMessage(Object obj) {
		if(obj instanceof OrderReportRealtimeData) {
			data = (OrderReportRealtimeData) obj;
			this.messageType = OutputMessageType.DZGrabOrderResp;
		}
	}
	
	@Override
	public String getMessageName() {
		return this.messageType.name();
	}

	@Override
	public void encode() {
		buffer = JT808Util.genDZGrabOrderResp(data);
	}

}
