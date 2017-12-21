package com.hgs.gpsserver.inputmessage;

import java.text.DecimalFormat;

import com.hgs.common.dbwrapper.DbSaveManager;
import com.hgs.common.dbwrapper.TaxiHistoryInfoWrapper;
import com.hgs.common.dbwrapper.TaxiInfoWrapper;
import com.hgs.common.utility.Byte2Hex;
import com.hgs.common.utility.DateUtil;
import com.hgs.common.utility.GeoPoint;
import com.hgs.common.utility.JT808Util;
import com.hgs.common.utility.MongoDBUtil;
import com.hgs.common.utility.PackageUtil;
import com.hgs.common.utility.StringUtil;
import com.hgs.gpsserver.common.Constants;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.device.GpsDevice;
import com.hgs.gpsserver.message.pojo.JT808GpsRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 
 * @description:位置消息上报
 *
 * @author yinz
 */
public class JT808GpsRealtimeInputMessage extends InputMessage {

	public JT808GpsRealtimeInputMessage(byte[] buffer) {
		this.setMessageType(InputMessageType.JT808GpsData);
		this.setByteBuffer(buffer);
	}
	
	@Override
	public String getMessageName() {
		return this.getMessageType().name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		JT808GpsRealtimeData realtimeData = new JT808GpsRealtimeData();
		realtimeData.setLastUpdateTime(this.createTime);
		//标识头
		byteArray.skip(1);
		
		//处理消息头
		
		JT808Util.handMsgHeader(byteArray, realtimeData);
		
		//处理消息体
		//报警标志
		byte[] tempAlarmFlagBt = byteArray.getArray(4); 
		realtimeData.setAlarmFlag(byteArray.cutHexString(4));
		
		//紧急告警标志位
		realtimeData.setEmerAlarmState((byte) JT808Util.GetBit(Byte2Hex.bytes2int(tempAlarmFlagBt), 0));
		
		//状态
		byte[] tempStatuBt = byteArray.getArray(4);
		realtimeData.setStatu(byteArray.cutHexString(4));
		
		//是否空载
		if(JT808Util.GetBit(Byte2Hex.bytes2int(tempStatuBt), 8) != 0 || JT808Util.GetBit(Byte2Hex.bytes2int(tempStatuBt), 9) != 0) {
			realtimeData.setTaxiState("1");
		} else {
			realtimeData.setTaxiState("0");
		}
		
		DecimalFormat format = new DecimalFormat("#.######");
		//纬度&&经度
		double lat = Double.parseDouble(format.format(byteArray.cutUnsignedInteger(4) * 1.0/ 1000000));
		double lng = Double.parseDouble(format.format(byteArray.cutUnsignedInteger(4) * 1.0/ 1000000));
		if(JT808Util.GetBit(Byte2Hex.bytes2int(tempStatuBt), 2) == 1) {
			//南纬
			lat = -1 * lat;
		}
		
		if(JT808Util.GetBit(Byte2Hex.bytes2int(tempStatuBt), 3) == 1) {
			//西经
			lng = -1 * lng;
		}
		if(!GeoPoint.isValid(lat, lng)) {
			BaseDevice device = (BaseDevice) getSender();
			logger.warn("Device reports abnormal point:" + lat + ", " + lng);
			this.isValid = false;
		} else {
			double[] bdPoint = GeoPoint.wgs2bd(lat, lng);
			logger.warn("wgs point : <{}>", lat + "," + lng);
			logger.warn("baidu point : <{}>", bdPoint[0] + "," + bdPoint[1]);
			realtimeData.setLatitude(bdPoint[0]);
			realtimeData.setLongitude(bdPoint[1]);
			realtimeData.setLat(lat);
			realtimeData.setLng(lng);
		}
		//海拔高度
		realtimeData.setAltitude(byteArray.cutUnsignedInteger(2));
		
		//速度
		realtimeData.setSpeed(byteArray.cutUnsignedInteger(2));
		
		//方向 大端显示 对调两个字节
		realtimeData.setDirection(byteArray.cutUnsignedIntegerTurnDiff(2) % 360);
		
		//时间
		String tempDateTime = PackageUtil.bcdToString(byteArray.cutArray(6));
		if(tempDateTime.length() > 12){
			logger.warn("error date time : <{}>", tempDateTime);
			this.isValid = false;
		}
		/*String date = DateUtil.formatData(null, null, tempDateTime);
		if(StringUtil.isBlank(date)){
			realtimeData.setDateTimer(null);
		}else{
			realtimeData.setDateTimer(date);
		}*/
		realtimeData.setDateTimer(DateUtil.getServerDate());
		
		//位置附加信息
		byte[] extraData = byteArray.cutArray(byteArray.length() - 2);
		if(extraData != null && extraData.length > 0) {
			JT808Util.handAddressExtraData(extraData, realtimeData);
		}
		
		return realtimeData;
	}

	@Override
	public void postRun() {
		updateLatestData();
		if(isValid) {
			saveHistoryData();
		}
	}
	
	private void updateLatestData() {
		GpsDevice device = (GpsDevice) this.getSender();
		JT808GpsRealtimeData realtimeData = (JT808GpsRealtimeData) getRealtimeData();
		
		device.setEncType(realtimeData.getDataEncType());
		device.setDevPhone(realtimeData.getPhone());
		
		TaxiInfoWrapper wrapper = new TaxiInfoWrapper();
		wrapper.setId(device.getId());
		wrapper.setInterphonePsn(device.getImei());
		if(isValid) {
			wrapper.setCurrentLat((float) realtimeData.getLatitude());
			wrapper.setCurrentLng((float) realtimeData.getLongitude());
			wrapper.setLat((float)realtimeData.getLat());
			wrapper.setLng((float)realtimeData.getLng());
			wrapper.setGpsReceiveTime(realtimeData.getDateTimer());
		}
		wrapper.setMileage((float) realtimeData.getMileage());
		wrapper.setCurrentDirection(realtimeData.getDirection());
		wrapper.setCurrentSpeed((float) realtimeData.getSpeed());
		wrapper.setLastUpdateTime(DateUtil.getCurDate());
		wrapper.setPassengerState(realtimeData.getTaxiState());
		wrapper.setIsAlert(realtimeData.getEmerAlarmState() != 0 ? "1" : "0");
		DbSaveManager.instance.cache(wrapper);
		
	}

	private void saveHistoryData() {
		GpsDevice device = (GpsDevice) this.getSender();
		JT808GpsRealtimeData realtimeData = (JT808GpsRealtimeData) getRealtimeData();
		TaxiHistoryInfoWrapper wrapper = new TaxiHistoryInfoWrapper();
		wrapper.setTaxiId(device.getId());
		wrapper.setInterphonePsn(device.getImei());
		wrapper.setHistoricalLat((float) realtimeData.getLatitude());
		wrapper.setHistoricalLng((float) realtimeData.getLongitude());
		wrapper.setLat((float)realtimeData.getLat());
		wrapper.setLng((float)realtimeData.getLng());
		wrapper.setHistoricalSpeed((float) realtimeData.getSpeed());
		wrapper.setHistoricalDir((int) realtimeData.getDirection());
		wrapper.setGpsReceiveTime(realtimeData.getDateTimer());
		//DbSaveManager.instance.cache(wrapper);
		MongoDBUtil.saveOne(device.getId(), (float) realtimeData.getLatitude(), (float) realtimeData.getLongitude(), null, (float)realtimeData.getSpeed(), realtimeData.getDirection(), 1, 0);
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.Platform, getRealtimeData());
	}
	
	public static void main(String[] args) {
		byte[] a1 = new byte[]{0x17,0x04,0x05,0x17,0x4b,0x7e};
		byte[] a2 = new byte[]{0x17,0x04,0x05,0x17,0x30,0x49};
		String tempDateTime1 = PackageUtil.bcdToString(a1);
		String tempDateTime2 = PackageUtil.bcdToString(a2);
		String b1 = DateUtil.formatData(null, null, tempDateTime1);
		String b2 = DateUtil.formatData(null, null, tempDateTime2);
		System.out.println(tempDateTime1);
		System.out.println(tempDateTime2);
		System.out.println(tempDateTime1.length());
		System.out.println(tempDateTime2.length());
		System.out.println(b1);
		System.out.println(b2);
	}
}
