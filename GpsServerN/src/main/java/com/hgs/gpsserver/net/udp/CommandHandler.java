package com.hgs.gpsserver.net.udp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.hgs.common.executorpool.InputExecutorPool;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.common.CommandMessage;
import com.hgs.gpsserver.inputmessage.CommandMessageFactory;
import com.hgs.gpsserver.module.NetModule;
import com.hgs.gpsserver.outputmessage.WebServer;

public class CommandHandler extends IoHandlerAdapter {
	
	static final String WEB_SERVER = "WEB_SERVER";
	
	public static final String encodType = "UTF-8";
	
	private final static Logger logger = NetModule.instance.getLogger();
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		final WebServer webServer = new WebServer();
		webServer.setSession(session);
		session.setAttribute(WEB_SERVER, webServer);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) {
		try {
			WebServer webServer = (WebServer) session.getAttribute(WEB_SERVER);
			if (message == null) {
				return;
			}
			IoBuffer msg = (IoBuffer)message;
			byte[] buffer = new byte[msg.limit()];
			msg.get(buffer);
			logger.debug("Delimited message:" + new String(buffer,"UTF-8"));
			CommandMessage command = CommandMessageFactory.instance.createInputMessage(buffer);
			if(command == null) {
				logger.error("parse msg<{}> failed!",new String(buffer,"UTF-8"));
				 return;
			}
			command.setSender(webServer);
			InputExecutorPool.cmdPool.execute(command);
			//command.run();
		}
		catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
