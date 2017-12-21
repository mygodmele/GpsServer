package com.hgs.common.executorpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hgs.common.utility.AttributeUtil;


public class OutputExecutorPool {
	public final static ExecutorService instance = Executors.newFixedThreadPool(AttributeUtil.getOutMsgCount());

	public OutputExecutorPool() {
	}

	public void execute(Runnable message) {
		instance.execute(message);
	}

}
