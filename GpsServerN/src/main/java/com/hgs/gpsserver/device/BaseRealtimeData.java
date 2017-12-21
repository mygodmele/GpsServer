package com.hgs.gpsserver.device;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.MsgCommonResult;


public abstract class BaseRealtimeData {
	private long lastUpdateTime;

	private String msgId; //消息ID
	private int msgBodyLength;  //消息体长度
	private String dataEncType; //数据加密方式
	private int subPackage;  //分包
	
	private  String phone; //终端手机号
	private String msgNum; //消息流水号
	private String msgPackat;//消息封装项
	
	private MsgCommonResult handresult = MsgCommonResult.Success;  //消息处理结果(用于生成平台通用应答消息)
	
	
	public abstract void update(InputMessage inputMessage);


	public MsgCommonResult getHandresult() {
		return handresult;
	}


	public void setHandresult(MsgCommonResult handresult) {
		this.handresult = handresult;
	}


	public long getLastUpdateTime() {
	    return lastUpdateTime;
    }


	public void setLastUpdateTime(long lastUpdateTime) {
	    this.lastUpdateTime = lastUpdateTime;
    }


	public String getMsgId() {
		return msgId;
	}


	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}


	public int getMsgBodyLength() {
		return msgBodyLength;
	}


	public void setMsgBodyLength(int msgBodyLength) {
		this.msgBodyLength = msgBodyLength;
	}


	public String getDataEncType() {
		return dataEncType;
	}


	public void setDataEncType(String dataEncType) {
		this.dataEncType = dataEncType;
	}


	public int getSubPackage() {
		return subPackage;
	}


	public void setSubPackage(int subPackage) {
		this.subPackage = subPackage;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getMsgNum() {
		return msgNum;
	}


	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}


	public String getMsgPackat() {
		return msgPackat;
	}


	public void setMsgPackat(String msgPackat) {
		this.msgPackat = msgPackat;
	}
	
	
	
	
}
