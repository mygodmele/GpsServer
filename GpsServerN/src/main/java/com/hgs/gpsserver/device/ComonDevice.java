package com.hgs.gpsserver.device;

import java.util.HashMap;
import java.util.Map;

public class ComonDevice extends GpsDevice {
	//private static ComonDevice instance;
	
	public static Map<String,String> phone2psn = new HashMap<String, String>();

	public ComonDevice(String deviceId) {
		super(deviceId);
	}
	
	/*public static ComonDevice getInstance() {
		if(instance == null) {
			synchronized (ComonDevice.class){
				if(instance == null){
					instance = new ComonDevice("110");
				}
			}
		}
		return instance;
	}*/
	
	public static void cachePsnMsg(String phone,String authId) {
		if(phone2psn.containsKey(phone)) {
			return;
		}
		phone2psn.put(phone, authId);
	}

}
