/**
 * @Description: Management class for sending output messages
 * 
 * History:
 *    2014年6月24日: Tomas, initial version
 * 
 * Copyright 2013-2014, Hangzhou Tusung Technology Co.,Ltd. All rights reserved.
 *
 */

package com.hgs.gpsserver.outputmessage;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.hgs.common.executorpool.OutputExecutorPool;
import com.hgs.gpsserver.common.GlobalSetting;
import com.hgs.gpsserver.module.InputMessageModule;
import com.hgs.gpsserver.module.OutputMessageModule;

/**
 * Center for management of all output messages, it's a global unique instance.
 **/
public class OutputMessageCenter {
	private ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(GlobalSetting.ThreadCount.OutputSchedule);
	private Logger logger = InputMessageModule.instance.getLogger();
	private OutputExecutorPool executorPool = new OutputExecutorPool();
	public final static OutputMessageCenter instance = new OutputMessageCenter();

	public void newOutputMessage(OutputMessage message) {
		if (message.getDelay() > 0){
			OutputMessageDelayTask task = new OutputMessageDelayTask(message);
			scheduledExecutor.schedule(task, message.getDelay(), TimeUnit.MILLISECONDS);
			return;
		}
		executorPool.execute(message);
	}

	private class OutputMessageDelayTask implements Runnable{
		private OutputMessage outputMessage;

		public OutputMessageDelayTask(OutputMessage message) {
			outputMessage = message;
		}
		
        public void run() {
        	if (outputMessage == null){
        		logger.error("Fatal error: outputMessage is null");
        		return;
        	}
        	outputMessage.run();
        }
	}
}
