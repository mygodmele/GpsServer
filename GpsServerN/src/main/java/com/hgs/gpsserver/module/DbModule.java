package com.hgs.gpsserver.module;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;

import com.hgs.common.db.Device;
import com.hgs.common.db.DeviceMapper;
import com.hgs.common.db.Mtd02GpsInfo;
import com.hgs.common.db.Mtd02GpsInfoMapper;
import com.hgs.common.db.Mtd03GpsHistory;
import com.hgs.common.db.Mtd03GpsHistoryMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.dbwrapper.DbSaveManager;
import com.hgs.common.dbwrapper.Mtd02GpsInfoWrapper;
import com.hgs.common.dbwrapper.Mtd03GpsHistoryWrapper;
import com.hgs.common.utility.MongoDBUtil;

public class DbModule extends BaseModule {

	public static final DbModule instance = new DbModule();
	
	public DbModule() {
		logger = LoggerFactory.getLogger(DbModule.class);
	}
	
	@Override
	public boolean startService() {
		if (isStarted) {
			logger.warn("DB Module is being started duplicately");
			return true;
		}
		SqlSession session = DAOWrapper.getSession();
		if(session == null) {
			return false;
		}
		session.close();
		DbSaveManager.instance.startService();
		MongoDBUtil.init();
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopService() {
		return true;
	}

	public static void main(String[] args) {
		/*SqlSession session = DAOWrapper.getSession();
		System.out.println(session);*/
		/*Mtd02GpsInfoMapper mapper = session.getMapper(Mtd02GpsInfoMapper.class);
		
		Mtd02GpsInfo info = new Mtd02GpsInfo();
		info.setLatitude(80.01f);
		info.setLongitude(12f);
		info.setSpeed("30");
		info.setDirection("40");
		info.setGpsArriveTime("2015/04/08 15:48:02");
		info.setK01UserId("55");
		mapper.updateSelective(info);*/
		/*DeviceMapper mapper  = session.getMapper(DeviceMapper.class);
		Device dev = mapper.selectByGpsImei("test0001");
		System.out.println(dev.getUserId());*/
		//session.close();
	}
}
