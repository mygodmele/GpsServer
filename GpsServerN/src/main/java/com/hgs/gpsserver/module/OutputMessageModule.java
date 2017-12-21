package com.hgs.gpsserver.module;

import org.slf4j.LoggerFactory;

public class OutputMessageModule extends BaseModule{

	public OutputMessageModule() {
		logger = LoggerFactory.getLogger(OutputMessageModule.class);
	}
	
	public final static OutputMessageModule instance = new OutputMessageModule();

	@Override
    public boolean startService() {
		if (isStarted) {
			logger.warn("OutputMessage Module is being started duplicately");
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
