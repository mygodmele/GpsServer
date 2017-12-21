package com.hgs.gpsserver.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;

import com.hgs.common.utility.ClientUtil;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.DeviceManager;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageCenter;


/**
 * All commands that sent from web server must inherit from this class.
 * 
 *
 */
public abstract class CommandMessage extends BaseMessage {
	protected boolean invalideMsg; //消息是否有效
	
	protected Logger logger = InputMessageModule.instance.getLogger();
	protected byte[] byteBuffer;
	protected ISender sender;

	public ISender getSender() {
    	return sender;
    }

	public void setSender(ISender sender) {
    	this.sender = sender;
    }

	public byte[] getByteBuffer() {
    	return byteBuffer;
    }

	public void setByteBuffer(byte[] byteBuffer) {
    	this.byteBuffer = byteBuffer;
    }

	public String getMessageName() {
		return "";
	}
	
	/**
	 * 子类实现这个函数用来返回消息里的deviceId.
	 * 
	 * @return
	 */
	public abstract String getDeviceId();
	
	/**
	 * 子类实现这个函数进行解析。
	 * @param message
	 */
	public abstract void decode();
	
	/**
	 * 返回发送给设备的信息。
	 * 
	 * @return null，表示没有消息发给设备。
	 */
	public abstract OutputMessage getOutputMessageToDevice();
	
	/**
	 * 返回发送给webserver的信息。
	 * 
	 * @return null，表示没有响应消息发给webserver
	 */
	public abstract OutputMessage getOutputMessageToWebServer();

	
	public boolean checkPara(String expected, Map<String, String> paraMap, String key) {
		if (!paraMap.containsKey(key)) {
			return false;
		}
		
		String value = paraMap.get(key);
		if (!expected.equalsIgnoreCase(value)) {
			logger.warn("Error message from webserver, expected parameter <{}> is missed", key);
		}
		return true;
	}
	
	@Override
	public void run() {
		decode();
		String deviceId = this.getDeviceId();
		
		/*GpsDevice device = (GpsDevice) DeviceManager.instance.getDevice(deviceId);
		if(device == null) {
			logger.error("Failed to load device {}.", deviceId);
		}*/
		ArrayList<String> psns = new ArrayList<String>();
		
		OutputMessage toDeviceMessage = this.getOutputMessageToDevice();
		if(toDeviceMessage != null) {
			//群发消息
			switch (toDeviceMessage.isMultiple()) {
			case OutputMessage.IsBroadcast:
				Iterator<Map.Entry<String, BaseDevice>> deviceIter = DeviceManager.instance.getDeviceIter();
				while (deviceIter.hasNext()) {
					Map.Entry<String, BaseDevice> entry = deviceIter.next();
					BaseDevice dev = entry.getValue();
					toDeviceMessage.addReceiver(dev);
					OutputMessageCenter.instance.newOutputMessage(toDeviceMessage);
				}
				break;
			case OutputMessage.IsMulticast:
				for(String id : deviceId.split(",")) {
					if(!DeviceManager.instance.isOnline(id)) {
						psns.add(id);
						logger.warn("device <{}> is offline,send text msg in multi faild.", id);
						continue;
					}
					BaseDevice dev = DeviceManager.instance.getDevice(id);
					toDeviceMessage.addReceiver(dev);
				}
				OutputMessageCenter.instance.newOutputMessage(toDeviceMessage);
				break;
			case OutputMessage.IsSingleCast:
				if(!DeviceManager.instance.isOnline(deviceId)) {
					psns.add(deviceId);
					logger.warn("device <{}> is offline,send text msg faild.", deviceId);
				} else {
					BaseDevice dev = DeviceManager.instance.getDevice(deviceId);
					toDeviceMessage.addReceiver(dev);
					OutputMessageCenter.instance.newOutputMessage(toDeviceMessage);
				}
				break;
			}
		}
		
		if(psns.size() > 0){ //修改成只发送成功
			for(String s : psns){
				if(s.length() == 9){
					s = "0" + s;
				}
				ClientUtil.sendMsg(s, (byte)0x00);
			}
		}
		
		/*
		OutputMessage toWebServerMessage = this.getOutputMessageToWebServer();
		if(toWebServerMessage != null){
			toWebServerMessage.addReceiver((IReceiver)this.sender);
			toWebServerMessage.run();
		}*/
		/*
		if(toWebServerMessage != null) {
			if(this.sender != null) {
				if(this.sender instanceof WebServer) {
					WebServer webServer = (WebServer) this.sender;
					webServer.sendMessage(toWebServerMessage.getIoBuffer());
				}
			}
		}
		*/
	}
}
