package com.hgs.gpsserver.common;

public class GlobalSetting {

	public static class ThreadCount {
		public static int OutputSchedule         = 5;
		public static int LoadDeviceFromDb       = 50;
	}
	
	public static class GpsState {
		public static String STATE_NORMAL = "GPS正常";
		public static String STATE_SHORT = "GPS天线短路";
		public static String STATE_OPEN = "GPS天线开路";
		public static String STATE_FAULT = "GPS模块故障";
	}
	
	public static class PowerState {
		public static String STATE_NORMAL = "正常";
		public static String STATE_LEAKAGE = "主电源掉电";
		public static String STATE_HIGHORLOW = "主电源过高或过低";
	}

}
