package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Mtd02GpsInfo;
import com.hgs.common.db.Mtd02GpsInfoMapper;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.device.DeviceManager;

public class Mtd02GpsInfoWrapper extends Mtd02GpsInfo implements IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			Mtd02GpsInfoMapper mapper = session.getMapper(Mtd02GpsInfoMapper.class);
			int row = mapper.updateSelective(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}
}
