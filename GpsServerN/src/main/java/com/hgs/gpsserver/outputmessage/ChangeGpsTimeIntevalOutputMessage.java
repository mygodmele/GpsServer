package com.hgs.gpsserver.outputmessage;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.SystemParam;
import com.hgs.common.db.SystemParamMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.Byte2Hex;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.DeviceManager;

public class ChangeGpsTimeIntevalOutputMessage extends OutputMessage {

	private String devImei;
	public static final int gpsDefInteval = -1;
	public ChangeGpsTimeIntevalOutputMessage(String devImei) {
		this.devImei = devImei;
	}
	@Override
	public String getMessageName() {
		return OutputMessageType.ChangeGpsTimeInteval.name();
	}

	@Override
	public void encode() {
		short gpsInteval = (short) loadGpsIntevalFromDb();
		byte[] timeInteval = Byte2Hex.short2bytes(gpsInteval);
		
		byte[] devImeiByte = Byte2Hex.HexString2Bytes(devImei);
		byte[] protobuf = new byte[timeInteval.length + devImeiByte.length];
		System.arraycopy(devImeiByte, 0, protobuf, 0, devImeiByte.length);
		System.arraycopy(timeInteval, 0, protobuf, devImeiByte.length, timeInteval.length);
		if(gpsInteval != -1) {
			buffer = OutputMessageUtil.genOutPutMessage(protobuf, (byte)0x34, (short) 8);
		}
	}
	
	private int loadGpsIntevalFromDb() {
		BaseDevice dev = DeviceManager.instance.getDevice(Integer.parseInt(devImei, 16) + "");
		Integer enterId = dev.getEnterId();
		SqlSession session = null;
		try {
			session = DAOWrapper.getSession();
			SystemParamMapper mapper = session.getMapper(SystemParamMapper.class);
			SystemParam obj = mapper.selectByEnterpriseId(enterId);
			if(obj == null) {
				return gpsDefInteval;
			}
			return obj.getGpsTimeInteval();
		} catch(Exception e) {
			return gpsDefInteval;
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
}
