package com.hgs.gpsserver.outputmessage;

import org.apache.mina.core.buffer.IoBuffer;

public class ResponseWebOutputMessage extends OutputMessage {

	public byte[] myBuffer = null;
	
	public ResponseWebOutputMessage(byte[] buff) {
		this.myBuffer = buff;
	}
	
	@Override
	public String getMessageName() {
		return "ResponseWebOutputMessage";
	}

	@Override
	public void encode() {
		buffer = IoBuffer.allocate(myBuffer.length).setAutoExpand(true);
		buffer.put(myBuffer);
	}

}
