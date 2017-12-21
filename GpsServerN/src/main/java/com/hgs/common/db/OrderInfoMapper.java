package com.hgs.common.db;


public interface OrderInfoMapper{
	
	int updateSelective(OrderInfo info);

	OrderInfo loadByOrderNum(String orderNum);
}
