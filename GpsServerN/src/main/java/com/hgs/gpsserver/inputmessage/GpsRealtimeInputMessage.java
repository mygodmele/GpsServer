package com.hgs.gpsserver.inputmessage;

import com.hgs.common.dbwrapper.DbSaveManager;
import com.hgs.common.dbwrapper.Mtd02GpsInfoExtWrapper;
import com.hgs.common.dbwrapper.Mtd02GpsInfoWrapper;
import com.hgs.common.dbwrapper.Mtd03GpsHistoryExtWrapper;
import com.hgs.common.dbwrapper.Mtd03GpsHistoryWrapper;
import com.hgs.common.utility.DateUtil;
import com.hgs.common.utility.GeoPoint;
import com.hgs.common.utility.PackageUtil;
import com.hgs.gpsserver.common.GlobalSetting;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.device.GpsDevice;
import com.hgs.gpsserver.message.pojo.GPSRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

public class GpsRealtimeInputMessage extends InputMessage{
	//TODO 方便硬件测试
	private boolean operate_ext = false;
	
	public GpsRealtimeInputMessage(byte[] buffer) {
		this.setMessageType(InputMessageType.GpsData);
		this.setByteBuffer(buffer);
	}

	//解析gps消息
	@Override
    public BaseRealtimeData decode(){
		try {

			GPSRealtimeData realtimeData = new GPSRealtimeData();
			realtimeData.setLastUpdateTime(this.createTime);
			//包头
			byteArray.skip(2);
			
			//主信令
			realtimeData.mainCmd = byteArray.cutByte();
			
			//包长
			byteArray.skip(2);
			
			//终端序列号
			realtimeData.deviceSN = byteArray.cutHexString(4);
			
			//子信令
			realtimeData.childCmd = byteArray.getByte();
			
			//日期时间
			byte[] gpsTime = byteArray.cutArray(6);
			String dateTimer = DateUtil.formatData(null, null, PackageUtil.bcdToString(gpsTime));
			realtimeData.setDataTimer(dateTimer);
			
			//纬度
			byte[] weidu = byteArray.cutArray(4);
			String latitude = PackageUtil.ConvertToLocation(weidu);
			
			//经度
			byte[] jindu = byteArray.cutArray(4);
			String longitude = PackageUtil.ConvertToLocation(jindu);
			
			if(!GeoPoint.isValid(Double.parseDouble(latitude), Double.parseDouble(longitude))) {
				logger.warn("Device <"+realtimeData.deviceSN+"> reports abnormal point:" + latitude + ", " + longitude);
				this.isValid = false;
				//TODO 硬件测试，需删除
				//realtimeData.setLatitude(Double.parseDouble(latitude) + "");
				//realtimeData.setLongitude(Double.parseDouble(longitude) + "");
			} else {
				double[] bdPoint = GeoPoint.wgs2bd(Double.parseDouble(latitude), Double.parseDouble(longitude));
				realtimeData.setLatitude(bdPoint[0] + "");
				realtimeData.setLongitude(bdPoint[1] + "");
			}
			
			//速度
			byte[] sudu = byteArray.cutArray(2);
			String speed = PackageUtil.bcdToString(sudu);
			realtimeData.setSpeed(Integer.parseInt(speed));
			
			//方向
			byte[] fangxiang = byteArray.cutArray(2);
			String direction = PackageUtil.bcdToString(fangxiang);
			int dic = Integer.parseInt(direction);
			if(dic < 0 || dic > 359) {
				// TODO 暂时不判断方向的准确性
				//this.isValid = false;
				realtimeData.setDirection(dic%360);
				logger.warn("Device <"+realtimeData.deviceSN+"> reports abnormal point,direction<{}>",direction);
			} else {
				realtimeData.setDirection(dic);
			}
			
			//定位标志
			byte psFlag = byteArray.cutByte();
			realtimeData.postionlogo = PackageUtil.GetBit(psFlag, 7);
			//未定位消息视为无效
			if(realtimeData.postionlogo != 1) {
				logger.warn("Device <"+realtimeData.deviceSN+"> reports abnormal point,positionlogo<{}>",realtimeData.postionlogo);
				this.isValid = false;
			} 
			realtimeData.error = genGpsState(psFlag);
			realtimeData.powerstate = genPowerState(psFlag);
			realtimeData.protocollogo = new StringBuilder().append(PackageUtil.GetBit(psFlag, 2))
					.append(PackageUtil.GetBit(psFlag, 1)).append(PackageUtil.GetBit(psFlag, 0)).toString();
			
			//里程
			realtimeData.mileage = byteArray.cutUnsignedInteger(3);
			
			//车辆状态 st1
			byte st1 = byteArray.cutByte();
			realtimeData.acc = PackageUtil.GetBit(st1, 7);
			realtimeData.st1d6 = PackageUtil.GetBit(st1, 6);
			realtimeData.st1d5 = PackageUtil.GetBit(st1, 5);
			realtimeData.st1d4 = PackageUtil.GetBit(st1, 4);
			realtimeData.st1d3 = PackageUtil.GetBit(st1, 3);
			realtimeData.st1d2 = PackageUtil.GetBit(st1, 3);
			realtimeData.st1d1 = PackageUtil.GetBit(st1, 1);
			realtimeData.st1d0 = PackageUtil.GetBit(st1, 0);
			
			//报警状态 st2
			byte st2 = byteArray.cutByte();
			realtimeData.st2d7 = PackageUtil.GetBit(st2, 7);
			realtimeData.st2d6 = PackageUtil.GetBit(st2, 6);
			realtimeData.st2d5 = PackageUtil.GetBit(st2, 5);
			realtimeData.st2d4 = PackageUtil.GetBit(st2, 4);
			realtimeData.st2d3 = PackageUtil.GetBit(st2, 3);
			realtimeData.st2d2 = PackageUtil.GetBit(st2, 3);
			realtimeData.st2d1 = PackageUtil.GetBit(st2, 1);
			realtimeData.st2d0 = PackageUtil.GetBit(st2, 0);
			
			//GPRS st3
			byte st3 = byteArray.cutByte();
			realtimeData.st3d7 = PackageUtil.GetBit(st3, 7);
			realtimeData.st3d6 = PackageUtil.GetBit(st3, 6);
			realtimeData.st3d5 = PackageUtil.GetBit(st3, 5);
			String csqTmp = new StringBuilder().append(PackageUtil.GetBit(st3, 4)).append(PackageUtil.GetBit(st3, 3))
					.append(PackageUtil.GetBit(st3, 2)).append(PackageUtil.GetBit(st3, 1))
					.append(PackageUtil.GetBit(st3, 0)).toString();
			realtimeData.csq = Integer.parseInt(csqTmp, 2);
			
			//外设状态 st4
			byte st4 = byteArray.cutByte();
			realtimeData.st4d7 = PackageUtil.GetBit(st4, 7);
			realtimeData.st4d6 = PackageUtil.GetBit(st4, 6);
			realtimeData.st4d5 = PackageUtil.GetBit(st4, 5);
			realtimeData.st4d4 = PackageUtil.GetBit(st4, 4);
			realtimeData.st4d3 = PackageUtil.GetBit(st4, 3);
			realtimeData.st4d2 = PackageUtil.GetBit(st4, 3);
			realtimeData.st4d1 = PackageUtil.GetBit(st4, 1);
			realtimeData.st4d0 = PackageUtil.GetBit(st4, 0);
			
			//终端设备状态     
			realtimeData.sendtime = Integer.parseInt(byteArray.cutHexString(2),16);
			realtimeData.stoptime = Integer.parseInt(byteArray.cutHexString(1),16);
			realtimeData.overspeedtime = Integer.parseInt(byteArray.cutHexString(1),16);
			realtimeData.fence = Integer.parseInt(byteArray.cutHexString(1),16);
			realtimeData.v6 = byteArray.cutHexString(1);
			realtimeData.v7 = byteArray.cutHexString(1);
			realtimeData.v8 = byteArray.cutHexString(1);
			
			realtimeData.crc = byteArray.cutByte();
			return realtimeData;
		
		} catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public String genGpsState(byte flag) {
		if(PackageUtil.GetBit(flag, 6) == 1) {
			if(PackageUtil.GetBit(flag, 5) == 1) {
				return GlobalSetting.GpsState.STATE_NORMAL;
			} else {
				return GlobalSetting.GpsState.STATE_SHORT;
			}
		} else {
			if(PackageUtil.GetBit(flag, 5) == 1) {
				return GlobalSetting.GpsState.STATE_OPEN;
			} else {
				return GlobalSetting.GpsState.STATE_FAULT;
			}
		}
	}
	
	public String genPowerState(byte flag) {
		if(PackageUtil.GetBit(flag, 4) == 1) {
			if(PackageUtil.GetBit(flag, 3) == 1) {
				return GlobalSetting.PowerState.STATE_NORMAL;
			} else {
				return GlobalSetting.PowerState.STATE_LEAKAGE;
			}
		} else {
			if(PackageUtil.GetBit(flag, 3) == 1) {
				return GlobalSetting.PowerState.STATE_HIGHORLOW;
			} else {
				return "";
			}
		}
	}
	
	
	//更新实时数据，插入历史记录
	public void postRun() {
		saveLatestData();
		//TODO 方便硬件测试
		if(isValid) {
			saveEventData();
		}
	}
	
	public void saveEventData() {
		insertEventData();
		if(operate_ext) {
			insertEventDataExt();
		}
	}
	
	public void insertEventData() {
		GpsDevice device = (GpsDevice) this.getSender();
		GPSRealtimeData realtimeData = (GPSRealtimeData) this.getRealtimeData();
		if(realtimeData == null) {
			logger.error("parse result realtimeData is null");
			return;
		}
		Mtd03GpsHistoryWrapper wrapper = new Mtd03GpsHistoryWrapper();
		
		wrapper.setK01UserId(device.getUserId().toString());
		wrapper.setLatitude(Float.parseFloat(realtimeData.getLatitude()));
		wrapper.setLongitude(Float.parseFloat(realtimeData.getLongitude()));
		wrapper.setGpsArriveTime(realtimeData.getDataTimer());
		wrapper.setSpeed(realtimeData.getSpeed() + "");
		wrapper.setDirection(realtimeData.getDirection() + "");
		wrapper.setPostionlogo(realtimeData.postionlogo + "");
		wrapper.setMileage(realtimeData.mileage + "");
		
		DbSaveManager.instance.cache(wrapper);
	}
	
	public void insertEventDataExt() {
		GpsDevice device = (GpsDevice) this.getSender();
		GPSRealtimeData realtimeData = (GPSRealtimeData) this.getRealtimeData();
		
		Mtd03GpsHistoryExtWrapper wrapper = new Mtd03GpsHistoryExtWrapper();
		
		wrapper.setK01UserId(device.getUserId().toString());
		wrapper.setError(realtimeData.error);
		wrapper.setPowerstate(realtimeData.powerstate);
		wrapper.setProtocollogo(realtimeData.protocollogo);
		
		wrapper.setAcc(realtimeData.acc + "");
		wrapper.setSt1d6(realtimeData.st1d6 + "");
		wrapper.setSt1d5(realtimeData.st1d5 + "");
		wrapper.setSt1d4(realtimeData.st1d4 + "");
		wrapper.setSt1d3(realtimeData.st1d3 + "");
		wrapper.setSt1d2(realtimeData.st1d2 + "");
		wrapper.setSt1d1(realtimeData.st1d1 + "");
		wrapper.setSt1d0(realtimeData.st1d0 + "");
		
		wrapper.setSt2d7(realtimeData.st2d7 + "");
		wrapper.setSt2d6(realtimeData.st2d6 + "");
		wrapper.setSt2d5(realtimeData.st2d5 + "");
		wrapper.setSt2d4(realtimeData.st2d4 + "");
		wrapper.setSt2d3(realtimeData.st2d3 + "");
		wrapper.setSt2d2(realtimeData.st2d2 + "");
		wrapper.setSt2d1(realtimeData.st2d1 + "");
		wrapper.setSt2d0(realtimeData.st2d0 + "");
		
		wrapper.setSt3d7(realtimeData.st3d7 + "");
		wrapper.setSt3d6(realtimeData.st3d6 + "");
		wrapper.setSt3d5(realtimeData.st3d5 + "");
		wrapper.setCsq(realtimeData.csq + "");
		
		wrapper.setSt4d7(realtimeData.st4d7 + "");
		wrapper.setSt4d6(realtimeData.st4d6 + "");
		wrapper.setSt4d5(realtimeData.st4d5 + "");
		wrapper.setSt4d4(realtimeData.st4d4 + "");
		wrapper.setSt4d3(realtimeData.st4d3 + "");
		wrapper.setSt4d2(realtimeData.st4d2 + "");
		wrapper.setSt4d1(realtimeData.st4d1 + "");
		wrapper.setSt4d0(realtimeData.st4d0 + "");
		
		wrapper.setSendtime(realtimeData.sendtime + "");
		wrapper.setStoptime(realtimeData.stoptime + "");
		wrapper.setOverspeedtime((double) realtimeData.overspeedtime);
		wrapper.setFence(realtimeData.fence + "");
		wrapper.setV6(realtimeData.v6);
		wrapper.setV7(realtimeData.v7);
		wrapper.setV8(realtimeData.v8);
		
		DbSaveManager.instance.cache(wrapper);
	}
	
	public void updateGpsInfo() {
		GpsDevice device = (GpsDevice) this.getSender();
		GPSRealtimeData realtimeData = (GPSRealtimeData) this.getRealtimeData();
		Mtd02GpsInfoWrapper wrapper = new Mtd02GpsInfoWrapper();
		if(realtimeData != null) {
			if(this.isValid){
				float lat = Float.parseFloat(realtimeData.getLatitude());
				float lon = Float.parseFloat(realtimeData.getLongitude());
				wrapper.setLatitude(lat);
				wrapper.setLongitude(lon);
				wrapper.setDirection(realtimeData.getDirection() + "");
				wrapper.setGpsArriveTime(realtimeData.getDataTimer());
			}
			wrapper.setSpeed(realtimeData.getSpeed() + "");
			wrapper.setPostionlogo(realtimeData.postionlogo + "");
			wrapper.setMileage(realtimeData.mileage + "");
		}
		wrapper.setLastUpdateTime(DateUtil.convertToDateStr(this.createTime));
		wrapper.setK01UserId(device.getUserId().toString());
		wrapper.setDevicePsn(device.getImei());
		DbSaveManager.instance.cache(wrapper);
	}
	
	public void updateGpsExt() {
		GpsDevice device = (GpsDevice) this.getSender();
		GPSRealtimeData realtimeData = (GPSRealtimeData) this.getRealtimeData();
		Mtd02GpsInfoExtWrapper wrapper = new Mtd02GpsInfoExtWrapper();
		wrapper.setK01UserId(device.getUserId().toString());
		wrapper.setError(realtimeData.error);
		wrapper.setPowerstate(realtimeData.powerstate);
		wrapper.setProtocollogo(realtimeData.protocollogo);
		wrapper.setAcc(realtimeData.acc + "");
		wrapper.setSt1d6(realtimeData.st1d6 + "");
		wrapper.setSt1d5(realtimeData.st1d5 + "");
		wrapper.setSt1d4(realtimeData.st1d4 + "");
		wrapper.setSt1d3(realtimeData.st1d3 + "");
		wrapper.setSt1d2(realtimeData.st1d2 + "");
		wrapper.setSt1d1(realtimeData.st1d1 + "");
		wrapper.setSt1d0(realtimeData.st1d0 + "");
		
		wrapper.setSt2d7(realtimeData.st2d7 + "");
		wrapper.setSt2d6(realtimeData.st2d6 + "");
		wrapper.setSt2d5(realtimeData.st2d5 + "");
		wrapper.setSt2d4(realtimeData.st2d4 + "");
		wrapper.setSt2d3(realtimeData.st2d3 + "");
		wrapper.setSt2d2(realtimeData.st2d2 + "");
		wrapper.setSt2d1(realtimeData.st2d1 + "");
		wrapper.setSt2d0(realtimeData.st2d0 + "");
		
		wrapper.setSt3d7(realtimeData.st3d7 + "");
		wrapper.setSt3d6(realtimeData.st3d6 + "");
		wrapper.setSt3d5(realtimeData.st3d5 + "");
		wrapper.setCsq(realtimeData.csq + "");
		
		wrapper.setSt4d7(realtimeData.st4d7 + "");
		wrapper.setSt4d6(realtimeData.st4d6 + "");
		wrapper.setSt4d5(realtimeData.st4d5 + "");
		wrapper.setSt4d4(realtimeData.st4d4 + "");
		wrapper.setSt4d3(realtimeData.st4d3 + "");
		wrapper.setSt4d2(realtimeData.st4d2 + "");
		wrapper.setSt4d1(realtimeData.st4d1 + "");
		wrapper.setSt4d0(realtimeData.st4d0 + "");
		
		wrapper.setSendtime(realtimeData.sendtime + "");
		wrapper.setStoptime(realtimeData.stoptime + "");
		wrapper.setOverspeedtime((double) realtimeData.overspeedtime);
		wrapper.setFence(realtimeData.fence + "");
		wrapper.setV6(realtimeData.v6);
		wrapper.setV7(realtimeData.v7);
		wrapper.setV8(realtimeData.v8);
		DbSaveManager.instance.cache(wrapper);
	}
	public void saveLatestData() {
		updateGpsInfo();
		if(operate_ext) {
			updateGpsExt();
		}
	}

	@Override
	public String getMessageName() {
		return "RealtimeInputMessage";
	}

	@Override
	public OutputMessage getSendMessage() {
		GPSRealtimeData data = (GPSRealtimeData) getRealtimeData();
		if(data.st3d6 == 0) {
			return null;
		}
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.GpsData, getRealtimeData());
	}
}
