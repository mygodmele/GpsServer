package com.hgs.common.db;

public interface DeviceMapper {

	Device selectByPsn(String psn);
	
	Device selectByPhone(String psn);//05-27 add
	
	Device selectByAuthId(String authId);
	
	void update(Device device);
	
	void clearAuthId(Integer id);
}
