package com.hgs.gpsserver.inputmessage;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Device;
import com.hgs.common.db.DeviceMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.Constants;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.MsgCommonResult;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.device.ComonDevice;
import com.hgs.gpsserver.device.GpsDevice;
import com.hgs.gpsserver.message.pojo.AuthRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 终端鉴权消息
 * @author yinz
 *
 */
public class AuthInputMessage extends InputMessage {

	public AuthInputMessage(byte[] buffer) {
		this.setMessageType(InputMessageType.Authentication);
		this.setByteBuffer(buffer);
	}
	
	@Override
	public String getMessageName() {
		return InputMessageType.Authentication.name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		
		AuthRealtimeData realtimeData = new AuthRealtimeData();
		realtimeData.setLastUpdateTime(this.createTime);
		
		//标识头
		byteArray.skip(1);
		
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		
		//鉴权码
		realtimeData.authId = byteArray.cutHexString(byteArray.length() - 2);
		
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		//需鉴权并记录鉴权结果（用于生成平台通用应答消息）
		AuthRealtimeData realtimeData = (AuthRealtimeData) getRealtimeData();
		SqlSession session = null;
		Device dev = null;
		try {
			session = DAOWrapper.getSession();
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			dev = mapper.selectByAuthId(realtimeData.authId);
			if(dev == null) {
				logger.warn("device authentication failed,authId <{}>!", realtimeData.authId);
				realtimeData.setHandresult(MsgCommonResult.Failure);
				return;
			}
			realtimeData.setHandresult(MsgCommonResult.Success);
		} catch(Exception e) {
			logger.error("auth got wrong", e);
			logger.error("fatal error occurred when Authentication device<{}>",realtimeData.authId);
			session.rollback();
			realtimeData.setHandresult(MsgCommonResult.Failure);
		} finally {
			if(session != null) {
				session.close();
			}
		}
		ComonDevice.cachePsnMsg(realtimeData.getPhone(), dev.getPsn());
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Platform, getRealtimeData());
	}

}
