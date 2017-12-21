package com.hgs.gpsserver.message.pojo;

import com.hgs.gpsserver.common.InputMessage;
import com.hgs.gpsserver.device.BaseRealtimeData;


public class GPSRealtimeData extends BaseRealtimeData{
	
	//位置数据
	 public String dataTimer;        //年月日时分秒
     public String longitude;          //经度
     public String latitude;           //纬度
     public int speed;              //速度
     public int direction;          //方向
     public int mileage;               //里程数
     public String deviceSN;            //终端序列号
     
     public byte mainCmd;            //主信令
     public byte childCmd;           //子信令
     public byte crc;                 //校验码
     
     //定位标志
     public int postionlogo;        //1: GPS已定位；0:未定位
     public String error;            //d6、d5组合 11：GPS正常；10：GPS天线短路；01：GPS天线开路；00：GPS模块故障
     public String powerstate;       //d3、d4组合电源状态 11：正常；10：主电源掉电；01:主电源过高或过低
     public String protocollogo;     //d0、d1、d2组合 协议区别标志 000：001：111    
     
     //车辆状态 st1
     public int acc;                //汽车钥匙开关
     public int st1d6;              // true：自定义 1 路高传感器状态为低；false:自定义1 路高传感器状态为高
     public int st1d5;              // true：自定义 2 路高传感器状态为低；false:自定义2 路高传感器状态为高
     public int st1d4;              // true：自定义 1 路低传感器状态为高；false:自定义1 路低传感器状态为低
     public int st1d3;              // true：自定义 2 路低传感器状态为高；false:自定义2 路低传感器状态为低
     public int st1d2;              // true：油路正常；false:油路断开
     public int st1d1;              // true：没有登签；false:已登签
     public int st1d0;              // true：未设防；false:已设防
     
     //报警状态st2
     public int st2d7;              // true: 正常；false:劫警报警
     public int st2d6;              // true：正常；false:超速报警
     public int st2d5;              // true：正常；false:停车超长报警
     public int st2d4;              // true：正常；false:驶出区域报警
     public int st2d3;              // true：正常；false:驶入区域报警
     public int st2d2;              // true：正常；false:看车密码错误报警
     public int st2d1;              // true：GPRS 没有上线；false:GPRS 已上线
     public int st2d0;              // true：终端拨号未成功；false:终端拨号成功
     
     //GPRS st3
     public int st3d7;              // true: GPRS 未注册；false: GPRS 注册
     public int st3d6;              // true：中心应下发 21 指令；false:中心不需下发 21 指令
     public int st3d5;              // true：TCP 通讯方式；false:UDP 通讯方式
     public int csq;                 // 信号强度
     
     //外设状态st4
     public int st4d7;              // true: 手柄接入；false:手柄没接入
     public int st4d6;              // true： LCD 显示屏接入；false: LCD 显示屏没接入
     public int st4d5;              // true：图像采集器接入；false:图像采集器没接入
     public int st4d4;              // true：计价器接入；false:计价器没接入
     public int st4d3;              // true：语音波号器接入；false:语音波号器没接入
     public int st4d2;              // true：禁止打出；false:允许打出
     public int st4d1;              // true：禁止打入；false:允许打入
     public int st4d0;              // true：禁止通话；false:允许通话
    
     //终端设备状态        
     public int sendtime;         //V1V2 定时发送时间
     public int stoptime;         //V3：停车设置时间
     public int overspeedtime;    //V4：超速设置时间
     public int fence;               //V5；电子围栏设置个数
     public String v6;               //V6：登签
     public String v7;               //V7: 定时发送图片的时间
     public String v8;               //V8: 中心下发的主命令

     @Override
	public void update(InputMessage inputMessage) {
		
	}

	public String getDataTimer() {
		return dataTimer;
	}

	public void setDataTimer(String dataTimer) {
		this.dataTimer = dataTimer;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
	
}
