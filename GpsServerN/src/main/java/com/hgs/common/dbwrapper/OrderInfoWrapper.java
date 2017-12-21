package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.OrderInfo;
import com.hgs.common.db.OrderInfoMapper;
import com.hgs.common.db.TaxiInfoMapper;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.device.DeviceManager;

public class OrderInfoWrapper extends OrderInfo implements IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			OrderInfoMapper mapper = session.getMapper(OrderInfoMapper.class);
			mapper.updateSelective(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}

	}

}
