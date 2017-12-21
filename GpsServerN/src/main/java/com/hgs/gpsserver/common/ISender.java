package com.hgs.gpsserver.common;

import com.hgs.gpsserver.outputmessage.NotifyResult;
import com.hgs.gpsserver.outputmessage.OutputMessage;


public interface ISender {
	public void notifyResponse(OutputMessage outputMessage, NotifyResult result);
	public String getSenderId();
}
