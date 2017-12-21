package com.hgs.gpsserver.outputmessage;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.cmd.CmdObj;
import com.hgs.common.db.OrderInfo;
import com.hgs.common.db.OrderInfoMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.DateUtil;
import com.hgs.common.utility.JT808Util;
import com.hgs.common.utility.StringUtil;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;

/**
 * 
 * @description:用于发布订单
 *
 * @author yinz
 */
public class PublishOrderOutputMessage extends OutputMessage {

	public CmdObj data;
	
	public PublishOrderOutputMessage(Object obj) {
		if(obj instanceof CmdObj) {
			this.data = (CmdObj) obj;
			messageType = OutputMessageType.PublishOrder;
		}
	}
	
	@Override
	public String getMessageName() {
		return this.messageType.name();
	}

	@Override
	public void encode() {
		if(data != null) {
			String orderNum = data.getContent();
			if(StringUtil.isBlank(orderNum)) {
				return;
			}
			SqlSession session  = null;
			
			try {
				session = DAOWrapper.getSession();
				OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
				OrderInfo order = mapper.loadByOrderNum(orderNum);
				if(order == null) {
					logger.warn("load order faild,orderNum<{}>",orderNum);
					return;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("时间");
				sb.append(DateUtil.getCurDate());
				sb.append("\r\n");
				sb.append("起始地址");
				sb.append(order.getStartPosition());
				sb.append("\r\n");
				sb.append("目的地");
				sb.append(order.getDestination());
				buffer = JT808Util.genPublishOrderOutputMessage((BaseDevice) this.receiverList.get(0), data, sb.toString());
			} catch(Exception e ) {
				
			}finally {
				if(session != null) {
					session.close();
				}
			}
			
		}
	}

}
