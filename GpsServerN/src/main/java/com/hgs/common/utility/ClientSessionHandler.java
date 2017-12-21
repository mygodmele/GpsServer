package com.hgs.common.utility;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSessionHandler extends IoHandlerAdapter{
	//要发送给服务端的消息  
    private final String psn;
    private final byte res;  
    private Logger logger = LoggerFactory.getLogger(ClientSessionHandler.class);

    public ClientSessionHandler(String psn, byte res){  
        this.psn = psn;
        this.res = res;
    }  

    //当与服务端链接成功时Session会被创建，同时会触发该方法  
    public void sessionOpened(IoSession session) throws Exception {  
        //发送消息给服务端  
    	IoBuffer buf = IoBuffer.allocate(10);
    	byte[] arr = Byte2Hex.HexString2Bytes(psn);
    	buf.put(arr);
    	//buf.put(res);
    	buf.put((byte)0xbb);
    	buf.flip();
        session.write(buf) ;  
        logger.warn("发送消息 <{}> 给服务端", psn); 
    }  

    //当接收到服务端发送来的消息时，会触发该方法  
    @Override  
    public void messageReceived(IoSession session, Object message)  
    throws Exception {  
        session.close(false) ;  
    }  

    @Override  
    public void exceptionCaught(IoSession session, Throwable cause)  
    throws Exception {  
        session.close(false) ;  //当发送异常，就关闭session  
    }  
    
    public static void main(String[] args) {
		String s = "161012345";
		byte[] a = Byte2Hex.HexString2Bytes(s);
		IoBuffer buf = IoBuffer.allocate(10);
		buf.put(a);
		buf.put((byte)0xbb);
		buf.flip();
		String b = Byte2Hex.Bytes2HexString(buf.array());
		System.out.println(b.substring(0, b.toUpperCase().indexOf("BB")));
	}
}
