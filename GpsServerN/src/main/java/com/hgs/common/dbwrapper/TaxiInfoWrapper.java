package com.hgs.common.dbwrapper;

import org.apache.ibatis.session.SqlSession;

import com.hgs.common.db.Mtd02GpsInfoMapper;
import com.hgs.common.db.TaxiInfo;
import com.hgs.common.db.TaxiInfoMapper;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.device.DeviceManager;

public class TaxiInfoWrapper extends TaxiInfo implements IDbWrapper {

	@Override
	public void save(SqlSession session) {
		try {
			TaxiInfoMapper mapper = session.getMapper(TaxiInfoMapper.class);
			int row = mapper.updateSelective(this);
			
			/*if(row <= 0) {
				//释放缓存device信息
				DeviceManager.instance.releaseDevice(this.getInterphonePsn());
			}*/
		} catch(Exception e) {
			FileLogger.printStackTrace(e);
		}
	}

}
