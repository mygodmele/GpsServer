package com.hgs.gpsserver.common;

import org.slf4j.Logger;

import com.hgs.common.executorpool.OutputExecutorPool;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.device.GpsDevice;
import com.hgs.gpsserver.message.pojo.HandsharkRealtimeData;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageUtil;

public abstract class InputMessage extends BaseMessage {
	private BaseRealtimeData realtimeData;
	protected int delay = 0;
	protected long executeTime = 0;
	protected ByteArrayUtility byteArray;
	protected ISender sender;
	protected InputMessageType messageType;
	protected Logger logger = InputMessageModule.instance.getLogger();
	protected boolean isValid = true;
	public InputMessage() {
		super();
	}
	
	public BaseRealtimeData getRealtimeData() {
		return this.realtimeData;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setByteBuffer(byte[] byteBuffer) {
		this.byteArray = new ByteArrayUtility(byteBuffer);
	}

	/**
	 * @return the sender
	 */
	public ISender getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(ISender sender) {
		this.sender = sender;
	}

	/**
	 * @return the messageType
	 */
	public InputMessageType getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(InputMessageType messageType) {
		this.messageType = messageType;
	}

	public void getQueueDuration() {
	}

	@Override
	public abstract String getMessageName();

	@Override
	public void run() {
		try {
			if (!preRun()) {
				return;
			}
			this.realtimeData = decode();
			postRun();
			doRun();
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

	public abstract BaseRealtimeData decode() throws Exception;

	/**
	 * Subclass must implement this method to generate message sent to device.
	 * 
	 * @return OutputMessage instance. If return null, no message sent to
	 *         device.
	 */
	public abstract OutputMessage getSendMessage();

	//TODO需判断，看是否需要发送响应消息
	public void doRun() {
		if (!(this.sender instanceof GpsDevice)) {
			return;
		}

		logger.debug("Start to run message of Device <{}>", this.sender.getSenderId());
		GpsDevice device = (GpsDevice) this.sender;

		OutputMessage outputMessage = getSendMessage();//创建一个对应的输出对象
		if (outputMessage == null) {
			return;
		}
		outputMessage.setInputMessage(this);
		device.prepareOutputMessage(outputMessage);
	}

	public boolean preRun() {
		executeTime = System.currentTimeMillis();
		return true;
	}

	public void postRun() {
	}
}
