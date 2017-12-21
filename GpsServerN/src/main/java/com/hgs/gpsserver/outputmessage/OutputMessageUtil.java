package com.hgs.gpsserver.outputmessage;

import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;

import com.hgs.common.utility.PackageUtil;

public class OutputMessageUtil {
	public static IoBuffer genOutPutMessage(byte[] protobuf,byte mainCmd,short packageSize) {
		IoBuffer buff = IoBuffer.allocate(5).setAutoExpand(true);
		//包头
        byte[] packageHeader = new byte[]{0x29,0x29};
		buff.put(packageHeader);
		buff.put(mainCmd);
		buff.putShort(packageSize);
		buff.put(protobuf);
		
		byte[] tmp = new byte[3 + packageSize];
		buff.flip();
		buff.get(tmp);
		
		buff.put(PackageUtil.getCheckSum(tmp));
		buff.put((byte) 0x0D);
		return buff;
	}
	
	public static ChangeGpsTimeIntevalOutputMessage genChangeTimOutputMessage(String devImei) {
		return new ChangeGpsTimeIntevalOutputMessage(devImei);
	}
	public static void main(String[] args) {
		byte[] pro = new byte[]{112, -128, 4};
		byte maincmd = 0x21;
		short packs = 5;
		IoBuffer bf = genOutPutMessage(pro, maincmd, packs);
		byte[] tmp = new byte[bf.limit()];
		bf.flip();
		bf.get(tmp);
	}
}
