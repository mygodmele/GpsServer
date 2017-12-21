package com.hgs.gpsserver.module;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.LoggerFactory;

import com.hgs.common.utility.AttributeUtil;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.net.tcp.HgsProtocolCodecFactory;
import com.hgs.gpsserver.net.tcp.ProtocolHandlerTcp;
import com.hgs.gpsserver.net.udp.CommandHandler;
import com.hgs.gpsserver.net.udp.ProtocolHandlerUdp;

public class NetModule extends BaseModule {
	private IoAcceptor gpsAcceptor;
	private IoAcceptor controlAcceptor;
	
	public final static NetModule instance = new NetModule();
	
	public NetModule() {
		logger = LoggerFactory.getLogger(NetModule.class);
	}
	
	@Override
	public boolean startService() {
		if (isStarted) {
			logger.warn("Net Module is being started duplicately");
			return true;
		}
		try {
			if(AttributeUtil.isTcp()) {
				logger.debug("tcp server started...");
				//打开tcp通道
				startTcpServer();
			} else {
				logger.debug("udp serevr started...");
				startUdpServer();
			}
			//打开udp通道
			startUdpCmd();
		} catch(IOException e) {
			FileLogger.printStackTrace(e);
			return false;
		}
		
		logger.debug("Listening gps msg on port {}" , AttributeUtil.getGpsPort());
		logger.debug("Listening command on port {}" , AttributeUtil.getCmdPort());
		logger.debug("Listening web on port {}" , AttributeUtil.getWebPort());
		isStarted = true;
		return true;
	}
	
	public void startTcpServer() throws IOException {
		gpsAcceptor = new NioSocketAcceptor();
		gpsAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HgsProtocolCodecFactory()));
		//gpsAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
		gpsAcceptor.setHandler(new ProtocolHandlerTcp());
		SocketSessionConfig cnf = (SocketSessionConfig) gpsAcceptor.getSessionConfig();
		cnf.setIdleTime(IdleStatus.BOTH_IDLE, 180);
		cnf.setSoLinger(0);
		cnf.setReuseAddress(true);
		gpsAcceptor.bind(new InetSocketAddress(AttributeUtil.getGpsPort()));
		gpsAcceptor.bind(new InetSocketAddress(AttributeUtil.getWebPort()));
	}
	
	public void startUdpServer() throws IOException {
		gpsAcceptor = new NioDatagramAcceptor();
		gpsAcceptor.setHandler(new ProtocolHandlerUdp());
		DatagramSessionConfig dcfg = (DatagramSessionConfig) gpsAcceptor.getSessionConfig();  
        dcfg.setReadBufferSize(4096);// 设置接收最大字节默认2048  
        dcfg.setMaxReadBufferSize(65536);  
        dcfg.setReceiveBufferSize(1024);// 设置输入缓冲区的大小  
        dcfg.setSendBufferSize(1024);// 设置输出缓冲区的大小  
        dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用  
		gpsAcceptor.bind(new InetSocketAddress(AttributeUtil.getGpsPort()));
	}
	
	public void startUdpCmd() throws IOException {
		controlAcceptor = new NioDatagramAcceptor();
		controlAcceptor.setHandler(new CommandHandler());
		controlAcceptor.bind(new InetSocketAddress(AttributeUtil.getCmdPort()));
	}

	@Override
	public boolean stopService() {
		return true;
	}

}
