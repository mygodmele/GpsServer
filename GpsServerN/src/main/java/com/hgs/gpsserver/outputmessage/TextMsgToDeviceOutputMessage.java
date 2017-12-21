package com.hgs.gpsserver.outputmessage;

import org.slf4j.Logger;

import com.hgs.common.cmd.CmdObj;
import com.hgs.common.utility.JT808Util;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.module.OutputMessageModule;

public class TextMsgToDeviceOutputMessage extends OutputMessage {

	private CmdObj obj;  //指令内容
	
	protected Logger logger = InputMessageModule.instance.getLogger();
	
	public TextMsgToDeviceOutputMessage(Object data) {
		if(data instanceof CmdObj) {
			this.obj = (CmdObj) data;
			this.messageType = OutputMessageType.TextMessage;
		}
	}
	
	@Override
	public String getMessageName() {
		return this.messageType.name();
	}

	
	public CmdObj getObj() {
		return obj;
	}


	public void setObj(CmdObj obj) {
		this.obj = obj;
	}


	@Override
	public void encode() {
		BaseDevice b = (BaseDevice) this.receiverList.get(0);
		this.buffer = JT808Util.genCommonTextOutputMessage(obj, (BaseDevice) this.receiverList.get(0));
	}

}
