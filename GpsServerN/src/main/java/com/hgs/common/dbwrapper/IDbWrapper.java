package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

public interface IDbWrapper{
	public void save(SqlSession session);
}
