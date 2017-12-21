package com.hgs.gpsserver.inputmessage;

import org.slf4j.Logger;

import com.hgs.common.cmd.CmdObj;
import com.hgs.common.utility.StringUtil;
import com.hgs.gpsserver.common.CommandMessage;
import com.hgs.gpsserver.common.IReceiver;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.net.udp.CommandHandler;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;

/**
 * 
 * @description:浏览器发送的指令消息
 *
 * @author yinz
 */
public class WebserverInputMessage extends CommandMessage {
	//对应psn(单个或多个)
	private String deviceID = "";
	private CmdObj msg;
	
	private String str = null;
	Logger log = InputMessageModule.instance.getLogger();
	
	public WebserverInputMessage(byte[] buffer) {
		this.byteBuffer = buffer;
	}
	
	@Override
	public String getDeviceId() {
		return deviceID;
	}

	@Override
	public void decode() {
		try {
			str = new String(byteBuffer,CommandHandler.encodType);
			this.msg = CmdObj.decode(str);
			this.deviceID = this.msg.getDevPsn();
		} catch (Exception e) {
			invalideMsg = true;
		}
	}

	@Override
	public OutputMessage getOutputMessageToDevice() {
		OutputMessage message = OutputMessageFactory.instance.createOutputMessage(OutputMessageType.TextMessage, this.msg);
		if(StringUtil.isBlank(this.deviceID)) {
			return null;
		}
		if(this.deviceID.split(",").length > 1) {
			message.setMultiple(OutputMessage.IsMulticast);
		} else if (this.deviceID.equals("000000000000")) {
			message.setMultiple(OutputMessage.IsBroadcast);
		} else {
			message.setMultiple(OutputMessage.IsSingleCast);
		}
		return message;
	}

	@Override
	public OutputMessage getOutputMessageToWebServer() {
		String command = str;
		if(invalideMsg) {
			command += " result=error";
		} else {
			command += " result=ok";
		}
		try {
			byte[] buff = command.getBytes("gbk");
			OutputMessage msg = CommandMessageFactory.instance.creatOutputMessage(buff);
			msg.addReceiver((IReceiver) this.getSender());
			return msg;
		} catch (Exception e) {
			return null;
		}
	}

}
