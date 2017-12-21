package com.hgs.gpsserver.inputmessage;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;

public class InputMessageFactory {
	public static final InputMessageFactory instance = new InputMessageFactory();
	
	public InputMessage createInputMessage(InputMessageType type, byte[] buffer) {
		InputMessage message = null;
		switch (type) {
			/*case GpsData: message = new GpsRealtimeInputMessage(buffer); break;
			case Hands: message = new HandShakeInputMessage(buffer); break;
			case TaxiState:message = new TaxiStateInputMessage(buffer);break;*/
			case DZGrabOrderReport:message = new DZGrabOrderReportInputMessage(buffer);break;   //电招抢单数据上传
		  //  case DZOrderResponse:message = new DZOrderRespInputMessage(buffer);break;           //电招订单应答
			case DeviceCommonResponse:message = new DeviceCommonRespInputMessage(buffer);break;  //终端通用应答
			case UnRegister:message = new UnRegisterInputMessage(buffer);break;					//终端注销
			case HeartBeat:message = new HeartBeatInputMessage(buffer);break;                   //心跳
			case Register:message = new RegisterInputMessage(buffer);break;                   //终端注册
			case Authentication:message = new AuthInputMessage(buffer);break;                   //终端鉴权
			case JT808GpsData:message = new JT808GpsRealtimeInputMessage(buffer);break;        //位置消息
		}
		
		return message;
	}
}
