package com.hgs.gpsserver.inputmessage;

import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.DeviceCommonRespRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;

/**
 * 终端通用应答消息
 * @author yinz
 *
 */
public class DeviceCommonRespInputMessage extends InputMessage {

	public DeviceCommonRespInputMessage(byte[] buff) {
		this.setByteBuffer(buff);
		this.setMessageType(InputMessageType.DeviceCommonResponse);
	}
	
	@Override
	public String getMessageName() {
		return InputMessageType.DeviceCommonResponse.name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		DeviceCommonRespRealtimeData realtimeData = new DeviceCommonRespRealtimeData();
		realtimeData.setLastUpdateTime(this.createTime);
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		
		//应答流水号
		realtimeData.setResponseNum(byteArray.cutHexString(2));
		//应答ID
		realtimeData.setResponseID(byteArray.cutHexString(2));
		//结果
		realtimeData.setResult(byteArray.cutByte());
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		// TODO 考虑如何处理终端通用应答
	}

	@Override
	public OutputMessage getSendMessage() {
		return null;
	}

}
