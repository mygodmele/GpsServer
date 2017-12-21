package com.hgs.gpsserver.common;

import org.apache.mina.core.buffer.IoBuffer;

public interface IReceiver {
	public void sendMessage(IoBuffer buffer);
}
