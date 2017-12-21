package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.TaxiHistoryInfo;
import com.hgs.common.db.TaxiHistoryInfoMapper;
import com.hgs.common.utility.FileLogger;

public class TaxiHistoryInfoWrapper extends TaxiHistoryInfo implements
		IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			TaxiHistoryInfoMapper mapper = session.getMapper(TaxiHistoryInfoMapper.class);
			mapper.insert(this);
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
