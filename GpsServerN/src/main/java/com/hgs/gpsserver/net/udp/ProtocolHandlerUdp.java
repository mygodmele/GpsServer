package com.hgs.gpsserver.net.udp;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;

import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.inputmessage.InputMessageCenter;
import com.hgs.gpsserver.module.NetModule;

public class ProtocolHandlerUdp extends IoHandlerAdapter {

	private final static Logger logger = NetModule.instance.getLogger();
	
	public void sessionCreated(IoSession session) throws Exception {
		try {
			InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
	        if (logger.isDebugEnabled()) {
	        	logger.debug("IoSession created from {}", address.getAddress() + ":" + address.getPort());
	        }
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}

	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		try {
			InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
			if (logger.isDebugEnabled()) {
				logger.debug("IoSession opened from {}", address.getAddress() + ":" + address.getPort());
			}
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        	if (logger.isDebugEnabled()) {
	        	InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
				logger.debug("Close idle session for {}", address.getAddress() + ":" + address.getPort());
	        }
	        session.close(true);
    }
	
	public void sessionClosed(IoSession session) throws Exception {
		try {
			InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
			if (logger.isDebugEnabled()) {
				logger.debug("IoSession closed for {}", address.getAddress() + ":" + address.getPort());
			}
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
	}
	
    public void exceptionCaught(IoSession session, Throwable cause){
        logger.warn("exceptionCaught: {}", cause.getMessage());
    }
	
	@Override
	public void messageReceived(IoSession session, Object message) {
		if (message == null) {
			return;
		}
		IoBuffer msg = (IoBuffer)message;
		byte[] buffer = new byte[msg.limit()];
		msg.get(buffer);
		try {
			InputMessageCenter.instance.newMessage(session, buffer);
		}
		catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}
}
