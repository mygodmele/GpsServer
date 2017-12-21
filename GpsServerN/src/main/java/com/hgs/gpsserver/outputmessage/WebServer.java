package com.hgs.gpsserver.outputmessage;

import java.net.InetAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import com.hgs.gpsserver.common.IReceiver;
import com.hgs.gpsserver.common.ISender;

public class WebServer implements ISender, IReceiver {
	private InetAddress ipAddress;
	private IoSession session;

	public IoSession getSession() {
    	return session;
    }

	public void setSession(IoSession session) {
    	this.session = session;
    }

	/**
	 * 
	 */
	public WebServer() {
		
	}

	/**
     * 向WebServer发送响应消息。
     */
    public void notifyResponse(OutputMessage outputMessage, NotifyResult result) {
	    if(outputMessage == null) {
	    	return;
	    }
	    
	    this.sendMessage(outputMessage.getIoBuffer());
    }

    public String getSenderId() {
	    return ipAddress.toString();
    }

    /**
     * 向WebServer发送响应消息。
     */
	public void sendMessage(IoBuffer buffer) {
		if (session == null) {
			return;
		}

		if (!session.isConnected()) {
			return;
		}
		buffer.flip();
		session.write(buffer);
    }
}
