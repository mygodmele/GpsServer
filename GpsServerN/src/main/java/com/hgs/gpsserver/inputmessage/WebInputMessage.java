package com.hgs.gpsserver.inputmessage;

import org.slf4j.Logger;

import com.hgs.common.utility.Byte2Hex;
import com.hgs.gpsserver.device.DeviceManager;
import com.hgs.gpsserver.module.InputMessageModule;

public class WebInputMessage {
	
	public static final WebInputMessage instance = new WebInputMessage();
	protected Logger logger = InputMessageModule.instance.getLogger();
	
	private WebInputMessage() {}
	
	public void releaseCache(Object message){
		String deviceImei = Byte2Hex.bytes2int((byte[]) message) + "";
		logger.debug(">>>>>>>>>>> Release deviceImei from DeviceManager deviceId <{}>", deviceImei);
		DeviceManager.instance.releaseDevice(deviceImei);
	}
	
}
