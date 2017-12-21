package com.hgs.common.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** */
public class FileLogger {
	private static final Logger logger = LoggerFactory.getLogger("CommonLogger");

	public static String LogLevelFine = "Fine";
	public static String LogLevelInfo = "Info";
	public static String LogLevelWarning = "Warning";
	public static String LogLevelSevere = "Severe";

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	public static void info(String logInfo) {
		logger.info(logInfo);
	}

	public static void severe(String logInfo) {
		logger.error(logInfo);
	}

	public static void warning(String logInfo) {
		logger.warn(logInfo);
	}

	public static void fine(String logInfo) {
		logger.debug(logInfo);
	}

	public static void printStackTrace(Exception e) {
		if (e == null) {
			e = new Exception("Fatal error: Exception printed by FileLogger is null");
		}
		String errMsg = e.getMessage();
		if (errMsg != null) {
			errMsg = errMsg.replace("\r\n", " ");
			logger.error(errMsg);
		}

		StackTraceElement[] messages = e.getStackTrace();
		int length = messages.length;
		for (int i = 0; i < length; i++) {
			logger.error(messages[i].toString());
		}
	}
}
