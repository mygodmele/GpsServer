package com.hgs.common.db;


public interface TaxiInfoMapper{
	
	int updateSelective(TaxiInfo info);
	
	TaxiInfo loadByPsn(String psn);
}
