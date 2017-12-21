package com.hgs.gpsserver.net.tcp;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;

import com.hgs.common.utility.AttributeUtil;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.inputmessage.InputMessageCenter;
import com.hgs.gpsserver.inputmessage.WebInputMessage;
import com.hgs.gpsserver.module.NetModule;

public class ProtocolHandlerTcp extends IoHandlerAdapter {
	private final static Logger logger = NetModule.instance.getLogger();
	
	public void sessionCreated(IoSession session) throws Exception {
		try {
			InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
			
			/*SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();  
	        cfg.setReceiveBufferSize(1024);  
	        cfg.setReadBufferSize(1024);  
	        cfg.setIdleTime(IdleStatus.BOTH_IDLE, 180);
	        //cfg.setKeepAlive(true);  
	        cfg.setSoLinger(0);*/ 		
	        
	        if (logger.isDebugEnabled()) {
	        	logger.debug("IoSession created from {}", address.getAddress() + ":" + address.getPort());
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
		
		try {
			if (getBindPort(session) == AttributeUtil.getWebPort()) {
				WebInputMessage.instance.releaseCache(message);
			}else{
				//开始处理接收到的消息
				InputMessageCenter.instance.newMessage(session, message);
			}
		}
		catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}
	
	private int getBindPort(IoSession session){
		InetSocketAddress socketAddress = (InetSocketAddress) session.getLocalAddress();
		return socketAddress.getPort();
	}
}
