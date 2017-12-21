package com.hgs.gpsserver.device;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;

import com.hgs.common.db.DeviceMapper;
import com.hgs.common.dbwrapper.DAOWrapper;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.common.GlobalSetting;
import com.hgs.gpsserver.module.DeviceModule;

public class DbLoader {
	public static enum DbLoadType {
		Device(1);

		private int value;

		private DbLoadType(int value) {
			this.value = value;
		}
	}
	
	public final static DbLoader instance = new DbLoader();
	private ConcurrentHashMap<String, Future<Object>> futureMap = new ConcurrentHashMap<String, Future<Object>>();
	private ExecutorService executor = Executors.newFixedThreadPool(GlobalSetting.ThreadCount.LoadDeviceFromDb);
	private Logger logger = DeviceModule.instance.getLogger();
	
	public DbLoader() {
	}

	public Object loadObjFromDb(String id, DbLoadType loadType) {
		Future<Object> future = null;
		String key = id + loadType.name();
		synchronized (futureMap) {
			if (futureMap.containsKey(key)) {
				future = futureMap.get(key);
			}
			else {
				LoadTask task = new LoadTask(id, loadType);
				future = executor.submit(task);
				futureMap.put(key, future);
			}
		}
		
		try {
			Object obj = future.get();
			synchronized (futureMap) {
				futureMap.remove(key);
			}
			return obj;
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}
		return null;
	}
	
	private class LoadTask implements Callable<Object> {
		private Object id;
		private DbLoadType loadType;
		
		public LoadTask(Object id, DbLoadType loadType) {
			this.id = id;
			this.loadType = loadType;
		}
		
		public Object call() throws Exception {
			SqlSession session = null;
			Object obj = null;
			try {
				session = DAOWrapper.getSession();
				
				if (loadType == DbLoadType.Device) {
					DeviceMapper mapper = session.getMapper(DeviceMapper.class);
					//obj = mapper.selectByGpsImei(id.toString());
					obj = mapper.selectByPsn(id.toString());
				}
				
				if (obj == null) {
					return null;
				}
			} catch (Exception e) {
				FileLogger.printStackTrace(e);
				return null;
			} finally {
				if (session != null) {
					session.close();
				}
			}
	        return obj;
        }
		
	}
}
