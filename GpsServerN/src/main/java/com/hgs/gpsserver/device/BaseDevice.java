package com.hgs.gpsserver.device;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.hgs.common.executorpool.InputExecutorPool;
import com.hgs.common.executorpool.OutputExecutorPool;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.common.IDevice;
import com.hgs.gpsserver.common.IReceiver;
import com.hgs.gpsserver.common.ISender;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.MessageHandler;
import com.hgs.gpsserver.module.DeviceModule;
import com.hgs.gpsserver.outputmessage.NotifyResult;
import com.hgs.gpsserver.outputmessage.OutputMessage;

public abstract class BaseDevice implements IDevice, IReceiver, ISender, Comparable<BaseDevice> {
	protected Integer id;
	//2015/6/3新增企业编号
	protected Integer enterId;
	
	protected volatile String encType;
	protected volatile String devPhone;
	//JT808中该字段对应设备鉴权码
	protected String authId;
	protected String deviceImei = "110";
	protected Integer userId;
	protected BaseRealtimeData lastRealtimeData;
	protected IoSession session;
	protected MessageHandler inputMessageHandler;
	protected MessageHandler outputMessageHandler;
	protected long lastActivityTime;
	
	protected Logger logger = DeviceModule.instance.getLogger();


	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public abstract boolean loadFromDb();
	public abstract boolean saveToDb();

	public BaseDevice(String psn) {
		inputMessageHandler = new MessageHandler(psn, InputExecutorPool.instance);
		outputMessageHandler = new MessageHandler(psn, OutputExecutorPool.instance);
		this.deviceImei = psn;
		this.lastActivityTime = 0;
	}

	public BaseRealtimeData getRealtimeData() {
		return lastRealtimeData;
	}

	public void setLastRealtimeData(BaseRealtimeData realData){
		this.lastRealtimeData = realData;
	}
			
	

	public String getEncType() {
		return encType;
	}

	public void setEncType(String encType) {
		this.encType = encType;
	}

	public String getDevPhone() {
		return devPhone;
	}

	public void setDevPhone(String devPhone) {
		this.devPhone = devPhone;
	}

	public String getDeviceImei() {
		return deviceImei;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	public Integer getEnterId() {
		return enterId;
	}

	public void setEnterId(Integer enterId) {
		this.enterId = enterId;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void updateLastActivityTime() {
		this.lastActivityTime = System.currentTimeMillis();
	}

	public long getLastActivityTime() {
		return lastActivityTime;

	}

	public void messgeReceived(InputMessage inputMessage, boolean highPriority) {
		updateLastActivityTime();

		if (highPriority) {
			InputExecutorPool.instance.execute(inputMessage);
		} else {
			inputMessageHandler.addMessage(inputMessage);
		}
	}

	public void messgeReceived(InputMessage inputMessage) {
		InputMessageType type = inputMessage.getMessageType();
		if (type == InputMessageType.JT808GpsData || type == InputMessageType.GpsData) {
			messgeReceived(inputMessage, false);
		} else {
			messgeReceived(inputMessage, true);
		}
	}

	public void prepareOutputMessage(OutputMessage outputMessage) {
		if (outputMessage != null) {
			outputMessageHandler.addMessage(outputMessage);
		}
	}

	public void sendMessage(IoBuffer buffer) {
		if (session == null) {
			logger.warn("Mina session of Device {} is null, and sending message is given up", this.getImei());
			return;
		}

		if (!session.isConnected()) {
			logger.warn("Device {} is not online currently, and sending message is given up", this.getImei());
			return;
		}

		try {
			buffer.flip();
			session.write(buffer);
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

	public void loadAlarmConfig() {
		
	}

	/**
	 * 收到数据，更新设备内部状态，并遍历告警列表
	 * 
	 * @param realtimeData
	 */
	public abstract void updateRealtimeData(BaseRealtimeData realtimeData);

	public abstract void onCheckState();

	public void notifyResponse(OutputMessage outputMessage, NotifyResult result) {

	}

	public String getSenderId() {
		return deviceImei;
	}

	/**
	 * @return the deviceId
	 */
	public String getImei() {
		return deviceImei;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceImei = deviceId;
	}

	public void onTimeout() {
		//this.changeStatus(DeviceStatusType.Offline);
	}

	@Override
	public int compareTo(BaseDevice targetDevice) {
		return (int)(getLastActivityTime() - targetDevice.getLastActivityTime());
	}

	public Integer getId()
	{
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
}
