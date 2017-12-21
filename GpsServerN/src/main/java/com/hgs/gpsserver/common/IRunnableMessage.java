package com.hgs.gpsserver.common;


public interface IRunnableMessage extends Runnable{
	public String getMessageName();

	@Override
	public void run();
}
