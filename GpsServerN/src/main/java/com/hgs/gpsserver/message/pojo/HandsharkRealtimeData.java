package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;

public class HandsharkRealtimeData extends BaseRealtimeData {

	public byte mainCmd;            //主信令
    public byte childCmd;           //子信令
    public byte crc;                 //校验码
    public String deviceSN;            //终端序列号
    
	@Override
	public void update(InputMessage inputMessage) {

	}

}
