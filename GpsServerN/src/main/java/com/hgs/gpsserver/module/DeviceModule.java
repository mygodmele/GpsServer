package com.hgs.gpsserver.module;


import org.slf4j.LoggerFactory;

public class DeviceModule extends BaseModule{
	public final static DeviceModule instance = new DeviceModule();

	public DeviceModule() {
		logger = LoggerFactory.getLogger(DeviceModule.class);
	}

	@Override
    public boolean startService() {
		if (isStarted) {
			logger.warn("Device Module is being started duplicately");
			return true;
		}
	    isStarted = true;
	    return true;
    }

	@Override
    public boolean stopService() {
	    return true;
    }
}
