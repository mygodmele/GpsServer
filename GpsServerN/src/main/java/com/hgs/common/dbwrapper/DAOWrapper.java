package com.hgs.common.dbwrapper;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.hgs.common.utility.FileLogger;

public class DAOWrapper {

	private static SqlSessionFactory factory = null;
	
	public static SqlSession getSession() {
		if (factory == null) {
			synchronized (SqlSession.class) {
				if (factory == null) {
					try {
						Reader reader = Resources.getResourceAsReader("mybatis.xml");
						SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
						factory = builder.build(reader);
					} catch (Exception e) {
						FileLogger.printStackTrace(e);
						return null;
					}
				}
			}
		}
		
		if (factory == null) {
			return null;
		}
		return factory.openSession();
	}
}
