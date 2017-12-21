package com.hgs.gpsserver.inputmessage;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.hgs.common.executorpool.InputExecutorPool;
import com.hgs.common.utility.AttributeUtil;
import com.hgs.common.utility.Byte2Hex;
import com.hgs.common.utility.FileLogger;
import com.hgs.common.utility.PackageUtil;
import com.hgs.gpsserver.common.ByteArrayUtility;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.ComonDevice;
import com.hgs.gpsserver.device.DeviceManager;
import com.hgs.gpsserver.module.InputMessageModule;

public class InputMessageCenter {

	public static final InputMessageCenter instance = new InputMessageCenter();
	protected AtomicInteger objectCountInCache = new AtomicInteger();
	protected Logger logger = InputMessageModule.instance.getLogger();
	
	protected ConcurrentLinkedQueue<DecodeTask> taskQueue;
	protected AtomicInteger taskQueueSize = new AtomicInteger();

	private InputMessageCenter() {
	}

	public void startService() {
		if (taskQueue != null ) {
			return;
		}
		
		taskQueue = new ConcurrentLinkedQueue<DecodeTask>();
		taskQueueSize.set(AttributeUtil.getInMsgCount());
		for (int i = taskQueueSize.get(); i > 0; i--) {
			taskQueue.add(new DecodeTask());
		}
	}

	public void stopService() {
		if (taskQueue == null) {
			return;
		}
		
		taskQueue.clear();
		taskQueue = null;
		taskQueueSize.set(0);
	}
	
	public DecodeTask pollDecodeTask() {
		if (taskQueue.isEmpty()) {
			return null;
		}
		taskQueueSize.decrementAndGet();
		return taskQueue.poll();
	}
	
	public void pushDecodeTask(DecodeTask task) {
		if (taskQueueSize.get() >= AttributeUtil.getInMsgCount()) {
			return;
		}
		
		taskQueueSize.incrementAndGet();
		taskQueue.add(task);
	}

	public void newMessage(IoSession session, Object message) {
		if (message == null) {
			return;
		}

		int count = objectCountInCache.incrementAndGet();
		RawInputMessage rawMsg = new RawInputMessage(session, message);
		
		DecodeTask task = pollDecodeTask();
		if (task == null) {
			if (count % 10 >= 9) {
				logger.warn("All decode tasks in InputMessageCenter are used up, count: {}", count);
			}
			task = new DecodeTask();
		}
		
		if (count > 10000) {
			if (System.currentTimeMillis() % 100 > 80) {
				logger.warn("Cache size in InputMessageCenter is {}, over 10000!", count);
			}
		}
		task.setRawInputMessage(rawMsg);
		InputExecutorPool.instance.execute(task);
	}

	private class DecodeTask implements Runnable {
		private RawInputMessage rawMsg;
		
		public DecodeTask() {
			
		}
		
		public void setRawInputMessage(RawInputMessage rawMsg) {
			this.rawMsg = rawMsg;
		}

		@Override
		public void run() {
			int cacheCount = objectCountInCache.get();
	
			try {
				byte[] _message = (byte[]) rawMsg.message;
				int msgLength = _message.length;

				if (msgLength < 5) {
					logger.error("Invalid message received:{}", Byte2Hex.Bytes2HexString(_message));
					return;
				}
				logger.debug("message received:{}", Byte2Hex.Bytes2HexString(_message));
				
				ByteArrayUtility byteArray = new ByteArrayUtility(_message);
				//消息头和消息尾校验（0x7e）
				byte header = (byte) byteArray.getByteAt(0);
				byte tail = (byte) byteArray.getByteAt(msgLength - 1);
				if(header != 0x7e || tail != 0x7e){
					logger.error("Invalid message hearder or Tail received:{}", Byte2Hex.Bytes2HexString(_message));
					return;
				}
				//校验位校验
				byte[] checkData = byteArray.subArray(1, msgLength - 3);
				byte check = (byte) byteArray.getByteAt(msgLength - 2);
				byte result = PackageUtil.getCheckSum(checkData);
				if(check != result) {
					logger.error("checkSum: <{}>,orgSum:<{}>",result,check);
					logger.error("Invalid message checkSum received:{}", Byte2Hex.Bytes2HexString(_message));
					return;
				}
				
				//--------------------------------------------
				//标识头
				byteArray.skip(1);
				//消息ID
				int msgId = byteArray.cutUnsignedInteger(2);
				byteArray.skip(2);
				InputMessageType type = InputMessageType.parseValue(msgId, null);
				InputMessage inputMessage = InputMessageFactory.instance.createInputMessage(type, _message);
				if(inputMessage == null) {
					logger.error("Invalid message msgId received:{}", Byte2Hex.Bytes2HexString(_message));
					return;
				}
				inputMessage.setCreateTime(rawMsg.createTime);
				//----------------------------------------------
				BaseDevice device = null;
				String psn = null;
				String phone = PackageUtil.bcdToString(byteArray.cutArray(6));
				//用于处理注册消息和鉴权消息 1103 不同机器注册和健全同时到达 返回device导致线程混乱
				if(inputMessage.getMessageType() == InputMessageType.Register || inputMessage.getMessageType() == InputMessageType.Authentication){
					//device = ComonDevice.getInstance();
					device = new ComonDevice(phone);
				} else {
					psn = ComonDevice.phone2psn.get(phone); //鉴权后会把类似000161050200存入map中
					//根据PSN加载设备
					if(psn != null) {
						device = DeviceManager.instance.getDevice(psn);
					}
				}
				
				if (device == null) {
					logger.error("Failed to retrieve device {} from DeviceManager.", psn);
					return;
				}
				if(inputMessage.getMessageType() != InputMessageType.Register &&
						inputMessage.getMessageType() != InputMessageType.Authentication) {
					//增加同一psn的到达时间判断
					/*boolean isPass = DeviceManager.instance.timeCalcute(psn);
					if(!isPass){
						return;
					}*/
					logger.debug("Received message from device <{}>,deviceId <{}>", new Object[]{psn,device.getId()});
				}
				device.setSession(rawMsg.session); //***单例的device 在注册鉴权并发的时候，导致属性对象session被替换
				inputMessage.setSender(device);
				device.messgeReceived(inputMessage);
			} catch (Exception e) {
				FileLogger.printStackTrace(e);
			} finally {
				cacheCount = objectCountInCache.decrementAndGet();
				pushDecodeTask(this);
				logger.debug("DecodeTask ended, inputMessages cache: {}", cacheCount);
			}
		}
	}

	private class RawInputMessage {
		private IoSession session;
		private Object message;
		private long createTime;

		public RawInputMessage(IoSession session, Object message) {
			this.session = session;
			this.message = message;
			createTime = System.currentTimeMillis();
		}
	}

}
