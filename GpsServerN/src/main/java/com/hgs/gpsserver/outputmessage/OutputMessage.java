package com.hgs.gpsserver.outputmessage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.common.BaseMessage;
import com.hgs.gpsserver.common.IReceiver;
import com.hgs.gpsserver.common.ISender;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.module.OutputMessageModule;

public abstract class OutputMessage extends BaseMessage {
	protected List<IReceiver> receiverList = new ArrayList<IReceiver>();
	protected ISender invoker;
	protected int delay = 0;
	protected long queueTime = 0;
	protected long executeTime = 0;
	protected long expiredTime = Long.MAX_VALUE;
	protected OutputMessageType messageType;
	protected Logger logger = InputMessageModule.instance.getLogger();
	protected boolean waitForResponse = false;
	protected InputMessage inputMessage;
	protected IoBuffer buffer;
	
	public final static int IsBroadcast = 0;
	public final static int IsMulticast = 1;
	public final static int IsSingleCast = 2;
	
	private int isMultiple; //是否群发消息

	public IoBuffer getIoBuffer() {
		return buffer;
	}
	
	public OutputMessage() {
		super();
	}

	public int isMultiple() {
		return isMultiple;
	}

	public void setMultiple(int isMultiple) {
		this.isMultiple = isMultiple;
	}

	/**
	 * @return the waitForResponse
	 */
	public boolean isWaitForResponse() {
		return waitForResponse;
	}

	/**
	 * @param waitForResponse
	 *            the waitForResponse to set
	 */
	public void setWaitForResponse(boolean waitForResponse) {
		this.waitForResponse = waitForResponse;
	}

	public void addReceiver(IReceiver receiver) {
		if (receiverList.contains(receiver)) {
			return;
		}

		receiverList.add(receiver);
	}

	public void removeReceiver(IReceiver receiver) {
		if (!receiverList.contains(receiver)) {
			return;
		}

		receiverList.remove(receiver);
	}

	public void updateQueueTime() {
		queueTime = System.currentTimeMillis();
	}

	public void updateQueueTime(long time) {
		queueTime = time;
	}

	public long getQueueDuration() {
		return System.currentTimeMillis() - queueTime;
	}

	public void setExpiredTime(long time) {
		expiredTime = time;
	}

	public long getExpiredTime() {
		return executeTime;
	}

	@Override
	public abstract String getMessageName();

	public abstract void encode();

	/**
	 * By defaults, messages are sent to terminals This function shall be
	 * overridden if the receiver is not terminal
	 */
	public void addReceiverList() {
		if (inputMessage == null) {
			return;
		}
		
		ISender sender = inputMessage.getSender();
		if (!(sender instanceof BaseDevice)) {
			logger.error("Fatal error: ID <{}> is not kind of BaseDevice", sender.getSenderId());
			return;
		}
		BaseDevice device = (BaseDevice) sender;
		receiverList.add(device);
	}

	@Override
	public void run() {
		try {
			if (!preRun()) {
				return;
			}
			if(receiverList.size() > 0){
				int size_ = receiverList.size();
				for (int i=0; i<size_; i++) {
					//addReceiverList();
					encode();
					doRun();
					postRun();
					receiverList.remove(0);//除去第一个
				}
			}else{
				encode();
				addReceiverList();
				doRun();
				postRun();
			}
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

	public void doRun() {
		if (receiverList.isEmpty()) {
			//logger.error("receiverList of OutputMessage of {} is empty.", invoker.getSenderId());
			logger.error("receiverList of OutputMessage of {} is empty.");
		}
		//for (IReceiver receiver : receiverList) {
			receiverList.get(0).sendMessage(this.buffer);
		//}
	}

	public boolean preRun() {
		if (expiredTime < System.currentTimeMillis()) {
			return false;
		}

		executeTime = System.currentTimeMillis();

		return true;
	}

	public void postRun() {

	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the inputMessage
	 */
	public InputMessage getInputMessage() {
		return inputMessage;
	}

	/**
	 * @param inputMessage
	 *            the inputMessage to set
	 */
	public void setInputMessage(InputMessage inputMessage) {
		this.inputMessage = inputMessage;
	}
}
