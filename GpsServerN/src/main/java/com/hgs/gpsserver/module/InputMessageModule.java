package com.hgs.gpsserver.module;

import org.slf4j.LoggerFactory;

import com.hgs.gpsserver.inputmessage.InputMessageCenter;

public class InputMessageModule extends BaseModule {

	public InputMessageModule() {
		logger = LoggerFactory.getLogger(InputMessageModule.class);
	}
	
	public final static InputMessageModule instance = new InputMessageModule();

	@Override
	public boolean startService() {
		if (isStarted) {
			logger.warn("InputMessage Module is being started duplicately");
			return true;
		}
		InputMessageCenter.instance.startService();
		isStarted = true;
	    return true;
	}

	@Override
	public boolean stopService() {
		return true;
	}

}
