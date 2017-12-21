package com.hgs.gpsserver.device;

import com.hgs.common.db.Device;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.device.DbLoader.DbLoadType;


public class GpsDevice extends BaseDevice {
	
	public GpsDevice(String authId) {
		super(authId);
	}

	@Override
	public boolean loadFromDb() {
		try {
			Device dev = (Device) DbLoader.instance.loadObjFromDb(deviceImei, DbLoadType.Device);
			if(dev == null) {
				return false;
			}
			
			//JT808需要设置IMEI(psn)
			setId(dev.getId());
			setAuthId(dev.getAuthId());
			//setUserId(dev.getUserId());
			//setEnterId(dev.getEnterId());
		}catch(Exception e) {
			FileLogger.printStackTrace(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean saveToDb() {
		return false;
	}

	@Override
	public void updateRealtimeData(BaseRealtimeData realtimeData) {
		
	}

	@Override
	public void onCheckState() {
		
	}
	
}
