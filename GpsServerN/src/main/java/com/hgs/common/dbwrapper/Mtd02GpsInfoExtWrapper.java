package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Mtd02GpsInfoExt;
import com.hgs.common.db.Mtd02GpsInfoExtMapper;
import com.hgs.common.utility.FileLogger;

public class Mtd02GpsInfoExtWrapper extends Mtd02GpsInfoExt implements
		IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			Mtd02GpsInfoExtMapper mapper = session.getMapper(Mtd02GpsInfoExtMapper.class);
			mapper.updateSelective(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
