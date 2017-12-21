package com.hgs.gpsserver.module;

import org.slf4j.Logger;

public abstract class BaseModule {
	protected Logger logger;
	protected boolean isStarted = false;

	public abstract boolean startService();
	public abstract boolean stopService();
	
	public Logger getLogger()
	{
		return logger;
	}
}
