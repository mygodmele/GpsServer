package com.hgs.gpsserver.inputmessage;

import com.hgs.common.utility.ClientUtil;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.OrderRespRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;

/**
 * 
 * @description:电招订单应答
 *
 * @author yinz
 */
public class DZOrderRespInputMessage extends InputMessage {

	public DZOrderRespInputMessage(byte[] buff) {
		this.setByteBuffer(buff);
		this.setMessageType(InputMessageType.DZOrderResponse);
	}
	
	@Override
	public String getMessageName() {
		return InputMessageType.DZOrderResponse.name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		OrderRespRealtimeData realtimeData = new OrderRespRealtimeData();
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		
		//应答流水号
		realtimeData.setRespMsgNum(byteArray.cutHexString(5));
		//结果
		realtimeData.setResult(byteArray.cutByte());
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		// TODO 需考虑如何处理该消息
	}

	@Override
	public OutputMessage getSendMessage() {
		OrderRespRealtimeData realtimeData = (OrderRespRealtimeData) getRealtimeData();
		ClientUtil.sendMsg(realtimeData.getRespMsgNum(), realtimeData.getResult());
		return null;
	}

}
