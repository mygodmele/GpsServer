package com.hgs.gpsserver.common;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.module.InputMessageModule;


/**
 * Message handler, designed to request thread pool to execute input messages of
 * device in sequence
 **/
public class MessageHandler implements Runnable {
	private String msgOwnerInfo;
	private Logger logger = InputMessageModule.instance.getLogger();
	private ExecutorService executor;

	private boolean executeFlag = false;
	private Lock executeLock = new ReentrantLock();

	private ConcurrentLinkedQueue<IRunnableMessage> messageQueue = new ConcurrentLinkedQueue<IRunnableMessage>();
	private Lock queueLock = new ReentrantLock();

	private boolean syncPauseFlag = false;

	public MessageHandler(String msgOwnerInfo, ExecutorService executorPool) {
		this.msgOwnerInfo = msgOwnerInfo;
		this.executor = executorPool;
	}

	public void setMsgOwnerInfo(String msgOwnerInfo) {
		this.msgOwnerInfo = msgOwnerInfo;
	}

	public void clearMessage() {
		if (messageQueue.isEmpty()) {
			return;
		}

		queueLock.lock();
		messageQueue.clear();
		queueLock.unlock();
	}

	public void syncResponseReceived(String deviceSn) {
		syncPauseFlag = false;
		queueLock.lock();
		if (!messageQueue.isEmpty()) {
			executeLock.lock();
			if (!executeFlag) {
				executeFlag = true;
				executor.execute(this);
			}
			executeLock.unlock();
		}
		queueLock.unlock();
	}

	public void addMessage(IRunnableMessage message) {
		if (message == null) {
			return;
		}

		/*
		 * message.setQueueTime(System.currentTimeMillis()); String messageName = message.getMessageName();
		 * 
		 * if (logger.isDebugEnabled()) { logger.debug("Queue message " +
		 * messageName + " with MessageSN: " + message.getMessageSn()); }
		 */

		queueLock.lock();
		messageQueue.add(message);

		if (!syncPauseFlag) {
			executeLock.lock();
			if (!executeFlag) {
				executeFlag = true;
				executor.execute(this);
			}
			executeLock.unlock();
		}
		queueLock.unlock();
	}

	public void run() {
		try {
			logger.debug("Message handler of device <{}> started", msgOwnerInfo);
			queueLock.lock();

			IRunnableMessage message;
			while (!messageQueue.isEmpty()) {
				message = messageQueue.remove();
				queueLock.unlock();
				try {
					executeMessage(message);
				} catch (Exception e) {
					FileLogger.printStackTrace(e);
				}
				queueLock.lock();
			}

			executeLock.lock();
			executeFlag = false;
			executeLock.unlock();
			queueLock.unlock();
		} catch (Exception e) {
			FileLogger.printStackTrace(e);
		}

	}

	private void executeMessage(IRunnableMessage message) {
		message.run();
	}

	public boolean isEmpty() {
		return messageQueue.isEmpty();
	}
}
