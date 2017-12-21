package com.hgs.gpsserver.outputmessage;

import com.hgs.gpsserver.common.OutputMessageType;


public class OutputMessageFactory {
	public final static OutputMessageFactory instance = new OutputMessageFactory();
	public OutputMessage createOutputMessage(OutputMessageType type,Object data) {
		OutputMessage message = null;
		switch (type) {
			/*case Hands : message = new HandsharkOutputMessage(data); break;
			case GpsData : message = new GpsRealTimeDataOutputMessage(data); break;
			case TaxiState:message = new TaxiStateOutputMessage(data);break;*/
		case Register:message = new RegisterRealtimeDataOutputMessage(data);break;
		case Platform:message = new PlatformOutputMessage(data);break;
		case DZGrabOrderResp:message = new DZGrabOrderRespOutputMessage(data);break;
		case PublishOrder:message = new PublishOrderOutputMessage(data);break;
		case TextMessage:message = new TextMsgToDeviceOutputMessage(data);break;
		}
		return message;
	}
}
