package com.hgs.gpsserver.inputmessage;

import java.io.UnsupportedEncodingException;

import com.hgs.common.cmd.CmdObj;
import com.hgs.gpsserver.common.CommandMessage;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.ResponseWebOutputMessage;


public class CommandMessageFactory {
	public static final CommandMessageFactory instance = new CommandMessageFactory();

	public CommandMessage createInputMessage(byte[] buffer) {
		CommandMessage message = null;
		String command = null;
		try {
			command = new String(buffer,"gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CmdObj obj = CmdObj.decode(command);
		if(obj != null) {
			switch (obj.getCmdType()) {
			case 0:message = new WebserverInputMessage(buffer);break;                 //下发文本消息
			case 1:message = new WebServerPublishOrderInputMessage(buffer);break;     //电招订单下发
			default:break;
			}
		}
		return message;
	}

	
	public OutputMessage creatOutputMessage(byte[] buffer) {
		return new ResponseWebOutputMessage(buffer);
	}
}
