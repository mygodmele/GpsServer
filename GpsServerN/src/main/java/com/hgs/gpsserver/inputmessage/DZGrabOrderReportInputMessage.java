package com.hgs.gpsserver.inputmessage;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.OrderInfo;
import com.hgs.common.db.OrderInfoMapper;
import com.hgs.common.db.TaxiInfo;
import com.hgs.common.db.TaxiInfoMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.DateUtil;
import com.hgs.common.utility.JT808Util;
import com.hgs.common.utility.PackageUtil;
import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.common.InputMessageType;
import com.hgs.gpsserver.common.MsgCommonResult;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.OrderReportRealtimeData;
import com.hgs.gpsserver.outputmessage.OutputMessage;
import com.hgs.gpsserver.outputmessage.OutputMessageFactory;
/**
 * 
 * @description:电招抢单数据
 *
 * @author yinz
 */
public class DZGrabOrderReportInputMessage extends InputMessage {

	public DZGrabOrderReportInputMessage(byte[] buff) {
		this.setByteBuffer(buff);
		this.setMessageType(InputMessageType.DZGrabOrderReport);
	}
	
	@Override
	public String getMessageName() {
		return InputMessageType.DZGrabOrderReport.name();
	}

	@Override
	public BaseRealtimeData decode() throws Exception {
		OrderReportRealtimeData realtimeData = new OrderReportRealtimeData();
		
		realtimeData.setLastUpdateTime(this.createTime);
		
		//标识头
		byteArray.skip(1);
		//处理消息头
		JT808Util.handMsgHeader(byteArray, realtimeData);
		//psn
		//realtimeData.setDevPsn(new String(byteArray.cutArray(7)));
		realtimeData.setDevPsn(byteArray.cutUnsignedInteger(4) + "");
		byteArray.skip(3);
		//订单编号
		realtimeData.setOrderNum(byteArray.cutHexString(5));
		//抢单时间
		realtimeData.setCurDateTime(PackageUtil.bcdToString(byteArray.cutArray(6)));
		return realtimeData;
	}
	
	@Override
	public void postRun() {
		OrderReportRealtimeData realtimeData = (OrderReportRealtimeData) this.getRealtimeData();
		SqlSession session = null;
		try {
			session = DAOWrapper.getSession();
			TaxiInfoMapper txMapper = session.getMapper(TaxiInfoMapper.class);
			TaxiInfo taxi = txMapper.loadByPsn(realtimeData.getDevPsn());
			if(taxi == null) {
				realtimeData.setHandresult(MsgCommonResult.Failure);
				return;
			}
			
			OrderInfo info = new OrderInfo();
			info.setTaxiId(taxi.getId());
			info.setOrderNum(realtimeData.getOrderNum());
			info.setOrderState("5");
			info.setOrderEndTime(DateUtil.getCurDate());
			OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
			//仅记录第一条抢单数据
			int rel = mapper.updateSelective(info);
			session.commit();
			if(rel == 1) {
				realtimeData.setHandresult(MsgCommonResult.Success);
			} else {
				realtimeData.setHandresult(MsgCommonResult.Failure);
			}
		} catch(Exception e) {
			session.rollback();
			realtimeData.setHandresult(MsgCommonResult.Failure);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	@Override
	public OutputMessage getSendMessage() {
		return OutputMessageFactory.instance.createOutputMessage(OutputMessageType.DZGrabOrderResp, getRealtimeData());
	}

}
