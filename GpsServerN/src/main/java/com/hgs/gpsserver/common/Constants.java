package com.hgs.gpsserver.common;

public class Constants {
	
	public static final String PSN_KEY = "authId";
	
	public static final int HEADER_LEN = 4; //for xingan protocol

	public static class SysAttr {
		public static final String IS_TCP = "isTcp";
		public static final String GPS_PORT = "gpsPort";
		public static final String CMD_PORT = "cmdPort";
		public static final String WEB_PORT = "webPort";
		public static final String WEB_PORT_SERVER = "webPortServer";
		public static final String IN_MSG_COUNT = "threadForInputMessage";
		public static final String OUT_MSG_COUNT = "threadForOutputMessage";
		public static final String SAVE_DB_COUNT = "threadForSaveToDb";
		public static final String MAX_DEV_COUNT = "MaxDeviceCount";
		public static final String MIN_MSG_COUNT = "MinDeviceCount";
		public static final String MONGODB_ACC = "mongodbAccount";
		public static final String MONGODB_PWD = "mongodbPwd";
	}
}
