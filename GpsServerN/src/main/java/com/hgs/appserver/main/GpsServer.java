package com.hgs.appserver.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hgs.gpsserver.module.DbModule;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.module.NetModule;

public class GpsServer {

	private static Logger logger = LoggerFactory.getLogger(GpsServer.class);
	public static void main(String[] args) {
		
		logger.debug("Try to launch DB Module");
		if (!DbModule.instance.startService()) {
			logger.error("Fatal error occurred when try to launch DB Module");
			return;
		}
		
		logger.debug("Try to launch Input Message Module");
		if (!InputMessageModule.instance.startService() ) {
			logger.error("Fatal error occurred when try to launch Input Message Module");
			return;
		}
		
		logger.debug("Try to launch Net Module");
		if (!NetModule.instance.startService()) {
			logger.error("Fatal error occurred when try to launch Net Module");
			return;
		}
	}
}
