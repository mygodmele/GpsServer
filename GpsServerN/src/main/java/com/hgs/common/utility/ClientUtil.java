package com.hgs.common.utility;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientUtil {
	
	public static void sendMsg(String psn, byte res){
		
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(new ClientSessionHandler(psn, res));
		ConnectFuture connectFuture =  
	            connector.connect(new InetSocketAddress("127.0.0.1", AttributeUtil.getWebPortServer()));
		//阻塞等待，直到链接服务器成功，或被中断  
        connectFuture.awaitUninterruptibly();
        IoSession session = connectFuture.getSession() ;  
        
        //阻塞，直到session关闭  
        session.getCloseFuture().awaitUninterruptibly() ;  
          
        //关闭链接  
        connector.dispose() ;
	}
}
