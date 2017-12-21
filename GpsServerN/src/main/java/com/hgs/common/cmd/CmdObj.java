package com.hgs.common.cmd;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.hgs.common.utility.Byte2Hex;

public class CmdObj {

	private String devPsn;     //设备psn
	private Short serialNum;  //消息序列号
	private Byte flag = 1;      //消息标识位(文本下发时对应标志位，订单下发时对应播报标识)
	private String content = "";    //消息内容
	
	private Integer cmdType = 0;   //命令类型（0：浏览器下发的指令<普通文本下发>，1：电招订单下发）
	private int textType = 3; // 文本下发类型
	
	public int getTextType() {
		return textType;
	}
	public void setTextType(int textType) {
		this.textType = textType;
	}
	public String getDevPsn() {
		return devPsn;
	}
	public void setDevPsn(String devPsn) {
		this.devPsn = devPsn;
	}
	public Short getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(Short serialNum) {
		this.serialNum = serialNum;
	}
	public Byte getFlag() {
		return flag;
	}
	public void setFlag(Byte flag) {
		this.flag = flag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getCmdType() {
		return cmdType;
	}
	public void setCmdType(Integer cmdType) {
		this.cmdType = cmdType;
	}
	public String encode(){
		String st = null;
		try{
			st = JSON.toJSONString(this);
		} catch(Exception e){
			return null;
		}
		return st;
	}
	public static CmdObj decode(String json){
		CmdObj obj = null;
		try{
			obj = JSON.parseObject(json, CmdObj.class);
			if(obj.getTextType() != 3){ // 仅文本显示
				obj.setContent(utf8ToUnicode(obj.getContent()));
			}
		} catch (Exception e) {
			return null;
		}
		return obj;
	}
	
	/**
	  * utf-8 转换成 unicode
	  * 2007-3-15
	  * @param inStr
	  * @return
	  */
	public static String utf8ToUnicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				// 英文及数字等
				sb.append(Byte2Hex.Bytes2HexString(getBytes(myBuffer[i])));
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				// 全角半角字符
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				// 汉字
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				if(hexS.startsWith("ffff")){
					hexS = hexS.substring(4);
				}
				//String unicode = "\\u" + hexS;
				sb.append(hexS.toLowerCase());
			}
		}
		return sb.toString();
	}
	
	public static byte[] getBytes(char chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(2);
		cb.put((char) 0x00);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}
	
	public static void main(String[] args) {
		CmdObj obj = new CmdObj();
		obj.setCmdType(0);
		obj.setContent(utf8ToUnicode("速读说"));
		obj.setDevPsn("5230038");
		obj.setFlag((byte) 1);
		obj.setSerialNum((short) 2);
		System.out.println(JSON.toJSONString(obj));
	}
}
