package com.hgs.gpsserver.device;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import com.hgs.common.utility.AttributeUtil;
import com.hgs.common.utility.FileLogger;
import com.hgs.gpsserver.module.InputMessageModule;

/**
 * Global unique instance for management of all devices
 **/
public class DeviceManager {
	public final static DeviceManager instance = new DeviceManager();

	private final long limit = 14000;
	protected Map<String, BaseDevice> deviceMap = new ConcurrentHashMap<String, BaseDevice>();
	protected Map<String, Long> deviceTimeMap = new ConcurrentHashMap<String, Long>();

	protected AtomicInteger deviceCount = new AtomicInteger();

	protected Logger logger = InputMessageModule.instance.getLogger();
	protected ScheduledExecutorService timerService;

	public DeviceManager() {
	
	}
	
	public boolean timeCalcute(String psn){
		if(!deviceTimeMap.containsKey(psn)){
			deviceTimeMap.put(psn, System.currentTimeMillis());
			return true;
		}else{
			long time = System.currentTimeMillis();
			long msgTime = time - deviceTimeMap.get(psn);
			if(msgTime < limit){ //小于14sec 舍弃
				logger.debug("<{}> send message is litter than 15 sec , time is <{}>, timeDiff is <{}>",new Object[]{psn, time, msgTime});
				return false;
			}else{ 
				deviceTimeMap.put(psn, time);
				return true;
			}
		}
	}
	
	public boolean isOnline(String psn) {
		return deviceMap.containsKey(psn);
	}

	public BaseDevice getDevice(String psn) {
		if (deviceMap.containsKey(psn)) {
			return deviceMap.get(psn);
		}

		BaseDevice device = loadDeviceFromDB(psn);
		if (device != null) {
			addDeviceToMap(device);
		}
		return device;
	}
	
	public Iterator<Map.Entry<String, BaseDevice>> getDeviceIter() {
		return deviceMap.entrySet().iterator();
	}

	private void addDeviceToMap(BaseDevice device) {
		synchronized(deviceMap) {
			if (deviceMap.containsKey(device.getImei())) {
				return;
			}
			deviceMap.put(device.getImei(), device);
			logger.debug("Load device {}", device.getImei());
			deviceCount.incrementAndGet();
		}
		
		//TODO 长时间没消息的设备需释放掉
		int count = deviceCount.get();
		if (count >= AttributeUtil.getMaxDevCount()) {
			try {
				compressDeviceMap();
			} catch(Exception e) {
				FileLogger.printStackTrace(e);
			}
			
		}
	}
	
	public synchronized void compressDeviceMap() {
		logger.debug("Start to compress device map");
		int count = deviceCount.get();
		if (count < AttributeUtil.getMaxDevCount()) {
			return;
		}

		// Total device count to be removed
		int countToBeRemoved = count - AttributeUtil.getMinDevCount();
		BaseDevice[] deviceArray = deviceMap.values().toArray(new BaseDevice[deviceMap.size()]);
		mergeSort(deviceArray, 0, 1);

		BaseDevice device = null;
		for (int i = 0; i < deviceArray.length; i++) {
			device = deviceArray[i];
			this.releaseDevice(device.getImei());
			countToBeRemoved--;
			
			if (countToBeRemoved <= 0) {
				return;
			}
		}
	}

	public void releaseDevice(String deviceImei) {
		synchronized(deviceMap) {
			if (!deviceMap.containsKey(deviceImei)) {
				logger.error("The device <{}> to be removed is not in DeviceManager", deviceImei);
				return;
			}
			deviceMap.remove(deviceImei);
			deviceCount.decrementAndGet();
		}
	}
	
	public void mergeSort(BaseDevice[] orgArr, int s, int len){
		int size = orgArr.length;
		int mid = size / (2 * len);
		int c = size % (2 * len);
		// -------归并到只剩一个有序集合的时候结束算法-------//
		if(mid == 0){
			return;
		}
		// ------进行一趟归并排序-------//
		for(int i=0; i < mid; i++){
			s = i * 2 * len;
			merge(orgArr, s, s + len, 2 * len + s -1);
		}
		// -------将剩下的数和倒数一个有序集合归并-------//
		if( c != 0){
			merge(orgArr,size - c - 2 * len, size - c, size - 1);
		}
		// -------递归执行下一趟归并排序------//
		mergeSort(orgArr, 0, 2 * len);
	}
	
	private void merge(BaseDevice[] org, int s, int m, int t){
		BaseDevice[] tmp = new BaseDevice[t - s + 1];
		int i = s, j = m, k = 0;
		while(i < m && j <= t){
			if(org[i].getLastActivityTime() <= org[j].getLastActivityTime()){
				tmp[k] = org[i];
				k++;
				i++;
			} else {
				tmp[k] = org[j];
				j++;
				k++;
			}
		}
		while(i < m){
			tmp[k] = org[i];
			i++;
			k++;
		}
		while(j <= t){
			tmp[k] = org[j];
			j++;
			k++;
		}
		System.arraycopy(tmp, 0, org, s, tmp.length);
	}
	//加载设备
	private BaseDevice loadDeviceFromDB(String psn) {
		BaseDevice gpsDevice = new GpsDevice(psn);
		if (!gpsDevice.loadFromDb()) {
			return null;
		}
		return gpsDevice;
	}

}
