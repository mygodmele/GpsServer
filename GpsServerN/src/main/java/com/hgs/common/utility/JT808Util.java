package com.hgs.common.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.hgs.common.cmd.CmdObj;
import com.hgs.gpsserver.common.ByteArrayUtility;
import com.hgs.gpsserver.common.MsgCommonResult;
import com.hgs.gpsserver.common.OutputMessageType;
import com.hgs.gpsserver.device.BaseDevice;
import com.hgs.gpsserver.device.BaseRealtimeData;
import com.hgs.gpsserver.message.pojo.JT808GpsRealtimeData;
import com.hgs.gpsserver.message.pojo.OrderReportRealtimeData;
import com.hgs.gpsserver.message.pojo.RegisterRealtimeData;

public class JT808Util {
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	
	//平台回复消息流水号
	public static short msgNum;
	
	//文本下发时的编码类型
	private static String encodeType = "gb2312";
	
	//获取消息流水号
	public static short getMsgNum() {
		synchronized (JT808Util.class) {
			if(++msgNum > Short.MAX_VALUE) {
				msgNum = 0;
			}
			return msgNum;
		}
	}
	
	//根据当前时间生成鉴权码 20170111135416
	public static String genAuthId() {
		
		return format.format(new Date());
	}
	
	public static String genAuthId(String psn) {
		String target = psn + format.format(new Date());
		String authCode = MD5Util.MD5(target).substring(0, 14).toLowerCase();
		String authrepl = "";
		if(authCode.contains("7d") || authCode.contains("7e")){
			authrepl = authCode.replace("7d", "6d").replace("7e", "6e");
		}else{
			authrepl = authCode;
		}
		return authrepl;
	}
	
	//生成订单下发消息
	public static IoBuffer genPublishOrderOutputMessage(BaseDevice device,CmdObj obj,String content) {
		//该IoBuffer包含消息头，消息体，校验码
		IoBuffer myBuffer = IoBuffer.allocate(10).setAutoExpand(true);
		//标识头
		myBuffer.put((byte) 0x7e);
		
		//记录消息体长度
		int msgBodyLen = 0;
		byte[] tempContentBt = null;
		try {
			tempContentBt = content.getBytes("gbk");
		} catch (Exception e1) {
		}
		if(StringUtil.isBlank(content)) {
			msgBodyLen = 1;
		} else {
			msgBodyLen = 1 + tempContentBt.length + 5;
		}
		//生成消息头
		genMsgHeaderForTextMsg(myBuffer, device, msgBodyLen,OutputMessageType.PublishOrder,obj.getSerialNum());
		//订单编号
		myBuffer.put(Byte2Hex.HexString2Bytes(obj.getContent().trim()));
		//播报标识
		myBuffer.put((byte) obj.getFlag().intValue());
		myBuffer.put(tempContentBt);
		genCheckSum(myBuffer);
		//标识尾
		myBuffer.put((byte) 0x7e);
		return myBuffer;
	}
	
	//生成文本下发消息(发给设备)
	public static IoBuffer genCommonTextOutputMessage(CmdObj obj,BaseDevice device) {
		//该IoBuffer包含消息头，消息体，校验码
		IoBuffer myBuffer = IoBuffer.allocate(10).setAutoExpand(true);
		//标识头
		myBuffer.put((byte) 0x7e);
		
		//记录消息体长度
		int msgBodyLen = 0;
		byte[] tempContentBt = null;
		try {
			tempContentBt = obj.getContent().getBytes(encodeType);
		} catch (Exception e1) {
		}
		if(StringUtil.isBlank(obj.getContent())) {
			if(obj.getTextType() == 1 || obj.getTextType() == 2 || obj.getTextType() == 3){
				msgBodyLen = 5;
			}else{
				msgBodyLen = 1;
			}
		} else {
			if(obj.getTextType() == 1 || obj.getTextType() == 2 || obj.getTextType() == 3){
				msgBodyLen = 5 + tempContentBt.length;
			}else{
				msgBodyLen = 1 + tempContentBt.length;
			}
		}
		//生成消息头
		genMsgHeaderForTextMsg(myBuffer, device, msgBodyLen,OutputMessageType.TextMessage,obj.getSerialNum());
		myBuffer.put((byte) obj.getFlag().intValue()); // 消息文本标识
		//消息体内容前增加两个字节 17-06-12
		if(obj.getTextType() == 1){ // 一键报警
			myBuffer.put((byte)0x30);
			myBuffer.put((byte)0x30);
			myBuffer.put((byte)0x32);
			myBuffer.put((byte)0x30);
		}else if(obj.getTextType() == 2){ // TTS播报
			myBuffer.put((byte)0x01);
			myBuffer.put((byte)0x02);
			myBuffer.put((byte)0x00);
			myBuffer.put((byte)0x00);
		}else if(obj.getTextType() == 3){ // 仅文本显示
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
		}else{
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
			myBuffer.put((byte)0x20);
		}
		myBuffer.put(tempContentBt);
		
		genCheckSum(myBuffer);
		//标识尾
		myBuffer.put((byte) 0x7e);
		return myBuffer;
	}
	//生成电招抢单数据应答
	public static IoBuffer genDZGrabOrderResp(OrderReportRealtimeData data) {
		//该IoBuffer包含消息头，消息体，校验码
		IoBuffer myBuffer = IoBuffer.allocate(10).setAutoExpand(true);
		//标识头
		myBuffer.put((byte) 0x7e);
		//生成消息头
		genMsgHeader(myBuffer, data, 6,OutputMessageType.DZGrabOrderResp);
		//应答流水号
		myBuffer.put(Byte2Hex.HexString2Bytes(data.getMsgNum()));
		//应答ID
		myBuffer.put(new byte[]{0x0F,0x02});
		//订单状态,结果
		if(data.getHandresult() == MsgCommonResult.Success) {
			myBuffer.put((byte) 1);
			myBuffer.put((byte) 0);
		} else {
			myBuffer.put((byte) 0);
			myBuffer.put((byte) 0);
		}
		/*//消息处理结果
		myBuffer.put((byte) data.getHandresult().getValue());*/
		
		genCheckSum(myBuffer);
		//标识尾
		myBuffer.put((byte) 0x7e);
		return myBuffer;
	}

	//生成平台通用应答消息
	public static IoBuffer genPlatformCommonOutputMessage(BaseRealtimeData data) {
		//该IoBuffer包含消息头，消息体，校验码
		IoBuffer myBuffer = IoBuffer.allocate(10).setAutoExpand(true);
		//标识头
		myBuffer.put((byte) 0x7e);
		//生成消息头
		genMsgHeader(myBuffer, data, 5,OutputMessageType.Platform);
		
		//消息体--
		//应答流水号
		myBuffer.put(Byte2Hex.HexString2Bytes(data.getMsgNum()));
		
		//应答ID
		myBuffer.put(Byte2Hex.HexString2Bytes(data.getMsgId()));
		
		//消息处理结果
		myBuffer.put((byte) data.getHandresult().getValue());
		
		genCheckSum(myBuffer);
		//标识尾
		myBuffer.put((byte) 0x7e);
		return myBuffer;
	}

	//生成终端注册消息回复消息
	public static IoBuffer genRegisterReplyOutputMessage(RegisterRealtimeData data) {
		//该IoBuffer包含消息头，消息体，校验码
		IoBuffer myBuffer = IoBuffer.allocate(10).setAutoExpand(true);
		//标识头
		myBuffer.put((byte) 0x7e);
		
		//记录消息体长度
		int msgBodyLen = 0;
		if(data.getRegistResult().getValue() != 0) {
			msgBodyLen = 3;
		} else {
			msgBodyLen = 3 + Byte2Hex.HexString2Bytes(data.getAuthId()).length;
		}
		
		//生成消息头
		genMsgHeader(myBuffer, data, msgBodyLen,OutputMessageType.Register);
		
		//消息体--
		
		//应答流水号
		myBuffer.put(Byte2Hex.HexString2Bytes(data.getMsgNum()));
		//结果
		myBuffer.put((byte) data.getRegistResult().getValue());
		if(data.getRegistResult().getValue() == 0) {
			//鉴权码(注册成功时)
			myBuffer.put(Byte2Hex.HexString2Bytes(data.getAuthId()));
		}
		
		genCheckSum(myBuffer);
		//标识尾
		myBuffer.put((byte) 0x7e);
		return myBuffer;
	}
	
	//生成校验码
	private static void genCheckSum(IoBuffer myBuffer) {
		myBuffer.flip();
		byte[] checkSumBt = new byte[myBuffer.limit() - 1];
		myBuffer.skip(1);
		myBuffer.get(checkSumBt);
		//校验码
		myBuffer.put(PackageUtil.getCheckSum(checkSumBt));
	}
	
	//文本下发、订单下发时，生成头消息
	private static void genMsgHeaderForTextMsg(IoBuffer myBuffer,BaseDevice device, int msgBodyLen,OutputMessageType type,short serialNum) {
		//消息ID
		myBuffer.putShort((short) type.getValue());
		
		//消息头属性
		StringBuilder sb = new StringBuilder();
		//保留
		sb.append("0000");
		//TODO 分包,需确认分包项内容   
		sb.append("00");
		//加密方式
		if(StringUtil.isBlank(device.getEncType())){
			sb.append("000000");
		}else{
			sb.append(device.getEncType());
		}
		String st = Integer.toBinaryString(msgBodyLen);
		int val = 10 - st.length();
		if(val > 0) {
			char[] buf = new char[val];
			for (int i = 0; i < buf.length; i++)
	        {
	            buf[i] = '0';
	        }
			st = new String(buf) + st;
		}
		sb.append(st);
		
		//消息体属性
		myBuffer.putShort((short) Integer.parseInt(sb.toString(), 2));
		
		//终端手机号
		myBuffer.put(Byte2Hex.HexString2Bytes(StringUtil.isBlank(device.getDevPhone())?"000000000000":device.getDevPhone()));
		
		//流水号
		myBuffer.putShort(serialNum);
		
		//TODO 消息包封装项?
	}
	
	//生成回复消息头
	private  static void genMsgHeader(IoBuffer myBuffer,BaseRealtimeData data,int msgBodyLen,OutputMessageType type) {
		
		//消息ID
		myBuffer.putShort((short) type.getValue());
		
		//消息头属性
		StringBuilder sb = new StringBuilder();
		//保留
		sb.append("0000");
		//TODO 分包,需确认分包项内容
		sb.append("00");
		//加密方式
		sb.append(data.getDataEncType());
		String st = Integer.toBinaryString(msgBodyLen);
		int val = 10 - st.length();
		if(val > 0) {
			char[] buf = new char[val];
			for (int i = 0; i < buf.length; i++)
	        {
	            buf[i] = '0';
	        }
			st = new String(buf) + st;
		}
		sb.append(st);
		
		//消息体属性
		myBuffer.putShort((short) Integer.parseInt(sb.toString(), 2));
		
		//终端手机号
		myBuffer.put(Byte2Hex.HexString2Bytes(data.getPhone()));
		
		//流水号
		myBuffer.putShort(JT808Util.getMsgNum());
		
		//TODO 消息包封装项?
	}
	
	//解析JT808协议消息头（用于gps消息解析）
	public static void handMsgHeader(ByteArrayUtility byteArray,BaseRealtimeData realtimeData) {
		//消息ID
		realtimeData.setMsgId(byteArray.cutHexString(2));
		
		//消息体属性
		String msgBodyAttr = byteArray.cutBinString(2, 16);
		//消息体长度
		realtimeData.setMsgBodyLength(Integer.valueOf(msgBodyAttr.substring(6),2));
		//数据加密方式
		realtimeData.setDataEncType(msgBodyAttr.substring(3, 6));
		//分包
		realtimeData.setSubPackage(Integer.parseInt(msgBodyAttr.substring(2, 3)));
		
		//手机号
		byte[] sj = byteArray.cutArray(6);
		realtimeData.setPhone(PackageUtil.bcdToString(sj));
		
		//消息流水号
		realtimeData.setMsgNum(byteArray.cutHexString(2));
		
	}
	
	//处理位置附加信息
	public static void handAddressExtraData(byte[] datas,JT808GpsRealtimeData realtimeData) {
		ByteArrayUtility byteArray = new ByteArrayUtility(datas);
		while(byteArray.hasData(2)) {
			if(!readAndParseAddrExtraMsg(byteArray, realtimeData)) {
				break;
			}
		}
	}
	
	//根据消息ID，读取相应的附加信息内容并解析
	private static boolean readAndParseAddrExtraMsg(ByteArrayUtility byteArray,JT808GpsRealtimeData realtimeData) {
		byte msgId = byteArray.cutByte();
		byte msgLength = byteArray.cutByte();
		if(!byteArray.hasData(msgLength)) {
			return false;
		}
		switch (msgId) {
		case 0x01:
			double mileage = byteArray.cutUnsignedInteger(msgLength);
			realtimeData.setMileage(mileage);
			break;

		default:
			break;
		}
		return true;
	}
	
	public static int GetBit(int b, int index) {
		return ((b & (1 << index)) > 0) ? 1 : 0;
	}
	
	
	public static void main(String[] args) {
		
		String hex = "0000097718a8td";
		String aa = hex.substring(0, 12);
		aa += "0d";
		System.out.println(aa);
	}
}
