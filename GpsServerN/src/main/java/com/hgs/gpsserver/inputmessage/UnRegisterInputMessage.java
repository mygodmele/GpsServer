package com.hgs.gpsserver.inputmessage;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Device;
import com.hgs.common.db.DeviceMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.MsgCommonResult;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 
 * @description:终端注销注册消息
 *
 * @author yinz
 */
public class UnRegisterInputMessage extends InputMessage {

	public UnRegisterInputMessage(byte[] buffer) {
		this.setByteBuffer(buffer);
		this.setMessageType(InputMessageType.UnRegister);
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
				
			}
		};
		
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		BaseRealtimeData realtimeData = this.getRealtimeData();
		// 清空鉴权码
		SqlSession session = null;
		try {
			session = DAOWrapper.getSession();
			BaseDevice device = (BaseDevice) this.getSender();
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			mapper.clearAuthId(device.getId());
			session.commit();
			realtimeData.setHandresult(MsgCommonResult.Success);
		} catch(Exception e) {
			realtimeData.setHandresult(MsgCommonResult.Failure);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Platform, getRealtimeData());
	}

}
