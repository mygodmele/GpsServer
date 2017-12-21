package com.hgs.gpsserver.inputmessage;

import com.hgs.common.dbwrapper.DbSaveManager;
import com.hgs.common.dbwrapper.TaxiInfoWrapper;
import com.hgs.common.utility.DateUtil;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.device.GpsDevice;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 
 * @description:心跳消息
 *
 * @author yinz
 */
public class HeartBeatInputMessage extends InputMessage {
	
	public HeartBeatInputMessage(byte[] buffer) {
		this.setByteBuffer(buffer);
		this.setMessageType(InputMessageType.HeartBeat);
	}

	@Override
	public String getMessageName() {
		return this.getMessageType().name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		BaseRealtimeData realtimeData = new BaseRealtimeData() {
			
			@Override
			public void update(InputMessage inputMessage) {
				// TODO Auto-generated method stub
				
			}
		};
		realtimeData.setLastUpdateTime(this.createTime);
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		return realtimeData;
	}

	@Override
	public void postRun() {
		GpsDevice device = (GpsDevice) this.getSender();
		TaxiInfoWrapper wrapper = new TaxiInfoWrapper();
		wrapper.setInterphonePsn(device.getImei());
		wrapper.setLastUpdateTime(DateUtil.getCurDate());
		wrapper.setId(device.getId());
		DbSaveManager.instance.cache(wrapper);
	}
	
	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Platform, getRealtimeData());
	}

}
