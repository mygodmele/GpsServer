package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class OrderReportRealtimeData extends BaseRealtimeData {

	private String devPsn;
	private String orderNum; //订单编号
	private String curDateTime;//终端上报抢单时间
	
	
	
	public String getDevPsn() {
		return devPsn;
	}



	public void setDevPsn(String devPsn) {
		this.devPsn = devPsn;
	}



	public String getCurDateTime() {
		return curDateTime;
	}



	public void setCurDateTime(String curDateTime) {
		this.curDateTime = curDateTime;
	}



	public String getOrderNum() {
		return orderNum;
	}



	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}



	@Override
	public void update(InputMessage inputMessage) {
		// TODO Auto-generated method stub

	}

}
