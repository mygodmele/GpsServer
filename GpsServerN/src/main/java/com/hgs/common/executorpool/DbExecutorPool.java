package com.hgs.common.executorpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hgs.common.utility.AttributeUtil;



public class DbExecutorPool {
	public final static ExecutorService instance = Executors.newFixedThreadPool(AttributeUtil.getSaveToDbCount());

	public DbExecutorPool() {
	}

	public void execute(Runnable message) {
		instance.execute(message);
	}
}
