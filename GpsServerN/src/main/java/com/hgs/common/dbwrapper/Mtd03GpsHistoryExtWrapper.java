package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Mtd03GpsHistoryExt;
import com.hgs.common.db.Mtd03GpsHistoryExtMapper;
import com.hgs.common.utility.FileLogger;

public class Mtd03GpsHistoryExtWrapper extends Mtd03GpsHistoryExt implements
		IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			Mtd03GpsHistoryExtMapper mapper = session.getMapper(Mtd03GpsHistoryExtMapper.class);
			mapper.insert(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
