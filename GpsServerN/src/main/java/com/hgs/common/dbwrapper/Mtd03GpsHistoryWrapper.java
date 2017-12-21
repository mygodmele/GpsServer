package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Mtd03GpsHistory;
import com.hgs.common.db.Mtd03GpsHistoryMapper;
import com.hgs.common.utility.FileLogger;

public class Mtd03GpsHistoryWrapper extends Mtd03GpsHistory implements
		IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			Mtd03GpsHistoryMapper mapper = session.getMapper(Mtd03GpsHistoryMapper.class);
			mapper.insert(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
