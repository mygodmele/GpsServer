package com.hgs.common.executorpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hgs.common.utility.AttributeUtil;



public class InputExecutorPool {
	public final static ExecutorService instance = Executors.newFixedThreadPool(AttributeUtil.getInMsgCount());
	
	public final static ExecutorService cmdPool = Executors.newCachedThreadPool();
	
	public InputExecutorPool() {
	}

	public void execute(Runnable message) {
		instance.execute(message);
	}
}
