package com.hgs.gpsserver.common;

public abstract class BaseMessage implements IRunnableMessage {
	public long createTime;

	@Override
	public abstract void run();

	public BaseMessage() {
		createTime = System.currentTimeMillis();
	}
}
