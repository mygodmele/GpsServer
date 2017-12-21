package com.hgs.common.dbwrapper;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hgs.common.executorpool.DbExecutorPool;
import com.hgs.common.utility.AttributeUtil;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.module.DbModule;


public class DbSaveManager {
	public final static DbSaveManager instance = new DbSaveManager();
	protected Logger logger = DbModule.instance.getLogger();
	protected AtomicInteger objectCountInCache = new AtomicInteger();
	
	protected ConcurrentLinkedQueue<SaveTask> taskQueue;
	protected AtomicInteger taskQueueSize = new AtomicInteger();
	public void startService() {
		if (taskQueue != null ) {
			return;
		}
		
		taskQueue = new ConcurrentLinkedQueue<SaveTask>();
		taskQueueSize.set(AttributeUtil.getSaveToDbCount());
		for (int i = taskQueueSize.get(); i > 0; i--) {
			taskQueue.add(new SaveTask());
		}
	}

	public void stopService() {
		if (taskQueue == null) {
			return;
		}
		
		taskQueue.clear();
		taskQueue = null;
		taskQueueSize.set(0);
	}

	public int getCountInCache() {
		return objectCountInCache.get();
	}
	
	public boolean hasCacheObject() {
		return objectCountInCache.get()>0;
	}

	/**
	 * Save object in cache
	 * 
	 * @param obj
	 *            : object to be saved
	 */
	public void cache(IDbWrapper obj) {
		if (obj == null) {
			logger.debug("obj == null, return directly");
			return;
		}

		int count = objectCountInCache.incrementAndGet();
		
		SaveTask task = pollSaveTask();
		if (task == null) {
			if (count % 10 >= 9) {
				logger.warn("All save tasks in DbSaveManager are used up, count: {}", count);
			}
			task = new SaveTask(obj);
		}
		task.setDbWrapper(obj);
		
		DbExecutorPool.instance.execute(task);
		
		if (count > 2000) {
			logger.warn("Cache size in DbSaveManager is {}, over 2000!", count);
		}
	}
	
	public SaveTask pollSaveTask() {
		if (taskQueue.isEmpty()) {
			return null;
		}
		taskQueueSize.decrementAndGet();
		return taskQueue.poll();
	}
	
	public void pushSaveTask(SaveTask task) {
		if (taskQueueSize.get() >= AttributeUtil.getSaveToDbCount()) {
			return;
		}
		
		taskQueueSize.incrementAndGet();
		taskQueue.add(task);
	}

	public synchronized void clear() {
	}

	private class SaveTask implements Runnable {
		private IDbWrapper obj;
		
		public SaveTask() {
			
		}
		
		public SaveTask(IDbWrapper obj) {
			setDbWrapper(obj);
		}
		
		public void setDbWrapper(IDbWrapper obj) {
			this.obj = obj;
		}
		
		@Override
		public void run() {
			logger.debug("SaveTask starts");
			SqlSession session = null;
			try {
				session = DAOWrapper.getSession();
				obj.save(session);
				session.commit();
			} catch(Exception e) {
				FileLogger.printStackTrace(e);
			} finally {
				try {
					if (session != null) {
						session.close();
					}
				} catch(Exception e) {
					FileLogger.printStackTrace(e);
				} finally {
					int cacheCount = objectCountInCache.decrementAndGet();
					logger.debug("SaveTask ended, objects in cache: {}", cacheCount);
					pushSaveTask(this);
				}
			}
        }
		
	}
}
