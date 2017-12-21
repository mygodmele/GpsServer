package com.hgs.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hgs.gpsserver.common.Constants;


public class AttributeUtil {

	private static Logger log = LoggerFactory.getLogger(AttributeUtil.class);
	private static Map<String,String> attMap = new HashMap<String, String>();
	
	static {
		InputStream in = AttributeUtil.class.getClassLoader().getResourceAsStream("attribute.properties");
		Properties pt = new Properties();
		try {
			pt.load(in);
			loadAttr(pt);
		} catch (IOException e) {
			log.error("load attribute config file fail....");
		}
	}
	
	public static void loadAttr(Properties pt){
		attMap.clear();
		attMap.put(Constants.SysAttr.GPS_PORT, pt.getProperty(Constants.SysAttr.GPS_PORT));
		attMap.put(Constants.SysAttr.CMD_PORT, pt.getProperty(Constants.SysAttr.CMD_PORT));
		attMap.put(Constants.SysAttr.WEB_PORT, pt.getProperty(Constants.SysAttr.WEB_PORT));
		attMap.put(Constants.SysAttr.WEB_PORT_SERVER, pt.getProperty(Constants.SysAttr.WEB_PORT_SERVER));
		attMap.put(Constants.SysAttr.IN_MSG_COUNT, pt.getProperty(Constants.SysAttr.IN_MSG_COUNT));
		attMap.put(Constants.SysAttr.OUT_MSG_COUNT, pt.getProperty(Constants.SysAttr.OUT_MSG_COUNT));
		attMap.put(Constants.SysAttr.SAVE_DB_COUNT, pt.getProperty(Constants.SysAttr.SAVE_DB_COUNT));
		attMap.put(Constants.SysAttr.MAX_DEV_COUNT, pt.getProperty(Constants.SysAttr.MAX_DEV_COUNT));
		attMap.put(Constants.SysAttr.MIN_MSG_COUNT, pt.getProperty(Constants.SysAttr.MIN_MSG_COUNT));
		attMap.put(Constants.SysAttr.IS_TCP, pt.getProperty(Constants.SysAttr.IS_TCP));
		attMap.put(Constants.SysAttr.MONGODB_ACC, pt.getProperty(Constants.SysAttr.MONGODB_ACC));
		attMap.put(Constants.SysAttr.MONGODB_PWD, pt.getProperty(Constants.SysAttr.MONGODB_PWD));
	}
	
	public static boolean isTcp() {
		return Boolean.valueOf(attMap.get(Constants.SysAttr.IS_TCP));
	}
	
	public static int getGpsPort(){
		return Integer.parseInt(attMap.get(Constants.SysAttr.GPS_PORT));
	}
	
	public static int getCmdPort(){
		return Integer.parseInt(attMap.get(Constants.SysAttr.CMD_PORT));
	}
	
	public static int getWebPort(){
		return Integer.parseInt(attMap.get(Constants.SysAttr.WEB_PORT));
	}
	
	public static int getWebPortServer(){
		return Integer.parseInt(attMap.get(Constants.SysAttr.WEB_PORT_SERVER));
	}
	
	public static int getInMsgCount() {
		return Integer.parseInt(attMap.get(Constants.SysAttr.IN_MSG_COUNT));
	}
	
	public static int getOutMsgCount() {
		return Integer.parseInt(attMap.get(Constants.SysAttr.OUT_MSG_COUNT));
	}
	
	public static int getSaveToDbCount() {
		return Integer.parseInt(attMap.get(Constants.SysAttr.SAVE_DB_COUNT));
	}
	
	public static int getMaxDevCount() {
		return Integer.parseInt(attMap.get(Constants.SysAttr.MAX_DEV_COUNT));
	}
	
	public static int getMinDevCount() {
		return Integer.parseInt(attMap.get(Constants.SysAttr.MIN_MSG_COUNT));
	}
	
	public static String getMongodbAccount() {
		return attMap.get(Constants.SysAttr.MONGODB_ACC);
	}
	
	public static String getMongodbPwd() {
		return attMap.get(Constants.SysAttr.MONGODB_PWD);
	}
	
	public static void main(String[] args) {
		System.out.println(getInMsgCount());
		System.out.println(getOutMsgCount());
		System.out.println(getSaveToDbCount());
		System.out.println(getMaxDevCount());
		System.out.println(getMinDevCount());
	}
}
