package com.hgs.gpsserver.inputmessage;


import java.io.UnsupportedEncodingException;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Device;
import com.hgs.common.db.DeviceMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.Byte2Hex;
import com.hgs.common.utility.JT808Util;
import com.hgs.common.utility.StringUtil;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.common.RegisterResult;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.RegisterRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 
 * @description:终端注册消息
 *
 * @author yinz
 */
public class RegisterInputMessage extends InputMessage {

	public RegisterInputMessage(byte[] buffer) {
		this.setMessageType(InputMessageType.Register);
		this.setByteBuffer(buffer);
	}
	
	@Override
	public String getMessageName() {
		
		return InputMessageType.Register.name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		RegisterRealtimeData realtimeData = new RegisterRealtimeData();
		realtimeData.setLastUpdateTime(this.createTime);
		
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		
		//处理消息体
		//省域ID
		realtimeData.provinceId = byteArray.cutHexString(2);
		
		//市县域ID
		realtimeData.cityId = byteArray.cutHexString(2);
		
		//制造商编码
		realtimeData.manufacturerId = byteArray.cutHexString(5);
		
		//终端型号
		realtimeData.deviceType = byteArray.cutHexString(20);
		
		//终端ID(此处有所改动，部标协议为7字节，此处改为4字节与对讲机psn保持一致,后面3个字节补0)
		//realtimeData.deviceId = new String(byteArray.cutArray(7));
		realtimeData.deviceId = byteArray.cutUnsignedInteger(4) + "";
		byteArray.skip(3);
		//车牌颜色
		realtimeData.lpColor = byteArray.cutByte();
		
		//车牌标识
		realtimeData.lpFlag =  new String(byteArray.cutArray(byteArray.length() - 2),"gbk");
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		RegisterRealtimeData realtimeData = (RegisterRealtimeData) this.getRealtimeData();
		SqlSession session = null;
		try {
			session = DAOWrapper.getSession();
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			//增加手机号修改 05-27
			String deviceId = realtimeData.getDeviceId();
			Device device = null;
			if(deviceId.startsWith("1")){
				device = mapper.selectByPsn(deviceId);
			}else{
				deviceId = "%" + deviceId + "%";
				device = mapper.selectByPhone(deviceId);
			}
			//车辆不存在
			if(device == null) {
				realtimeData.setRegistResult(RegisterResult.VehicleNotExist);
				logger.warn("register psn<{}> faild,vehicle not exist",realtimeData.getDeviceId());
				return;
			}
			//车辆已注册
			if(!StringUtil.isBlank(device.getAuthId())) {
				realtimeData.setRegistResult(RegisterResult.VehicleAlreadyRegister);
				logger.warn("register psn<{}> faild,vehicle has registed",realtimeData.getDeviceId());
				return;
			}
			//车辆未注册，执行注册操作
			realtimeData.setAuthId(JT808Util.genAuthId(realtimeData.getDeviceId()));
			
			//鉴权码存库
			device.setAuthId(realtimeData.getAuthId());
			device.setLicensePlate(realtimeData.lpFlag);
			mapper.update(device);
			session.commit();
			realtimeData.setRegistResult(RegisterResult.Success);
		} catch(Exception e) {
			logger.error(e.getMessage());
			//出错则注册未成功
			session.rollback();
			realtimeData.setRegistResult(RegisterResult.Failure);
		} finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Register, getRealtimeData());
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String psn = "15957413893";
		System.out.println(Byte2Hex.Bytes2HexString(Byte2Hex.int2bytes(306781980)));
	}
}
