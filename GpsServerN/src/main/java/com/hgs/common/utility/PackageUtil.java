package com.hgs.common.utility;

import java.text.DecimalFormat;

public class PackageUtil {
	
	public static DecimalFormat format = new DecimalFormat("0.00000");

	public static byte getCheckSum(byte[] bts) {
		byte result = bts[0];
		for(int i=0; i<bts.length - 1; i++){
			result = (byte) (result ^ bts[i + 1]);
		}
		return result;
	}
	
	// BCD码转字符串
	public static String bcdToString(byte[] bt) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bt) {
			sb.append((b >> 4) & 0x0f);
			sb.append(b & 0x0f);
		}
		return sb.toString();
	}

	// TODO需判断正负
	public static String ConvertToLocation(byte[] str) {
		int flag = 1;
		try {
			// BCD转码后的字串
			String strbcd = bcdToString(str);
			if(strbcd.charAt(0) > '7') {
				strbcd = strbcd.charAt(0) - '8' + strbcd.substring(1);
				flag = -1;
			}
			// 度
			String strdegree = strbcd.substring(0, 3);
			// 分
			String strminute = strbcd.substring(3, 5) + "."
					+ strbcd.substring(5);
			// 坐标
			Double fcoordinate = (Double.parseDouble(strdegree)
					+ Double.parseDouble(strminute) / 60) * flag;
			return format.format(fcoordinate);
		} catch (Exception ex) {
			return "";
		}
	}

	public static int GetBit(byte b, int index) {
		return ((b & (1 << index)) > 0) ? 1 : 0;
	}
	
}
