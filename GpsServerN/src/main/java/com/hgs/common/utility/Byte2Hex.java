package com.hgs.common.utility;

import java.util.Arrays;

public class Byte2Hex {
	
	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	public static byte[] int2bytes(int data) {
		int length = 4;
		byte[] result = new byte[length];
		for(int i = 0; i < length; i++) {
			result[i] = (byte)((data >> 8*(length - 1 - i)) & 0xff);
		}
		return result;
	}
	 
	public static byte[] short2bytes(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) ((n & 0xFF00) >> 8);
		b[1] = (byte) (n & 0xFF);
		return b;
	}
	 
	public static short bytes2short(byte[] b) {
		short n = (short) (((b[0] < 0 ? b[0] + 256 : b[0]) << 8) + (b[1] < 0 ? b[1] + 256
				: b[1]));
		return n;
	}

	public static int bytes2int(byte b[]) {
		int s = 0;
		s = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8
				| (b[3] & 0xff);
		return s;
	}

	public static char byte2char(byte b) {
		return (char) b;
	}

	public static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	public static String Bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	public static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}
	
	public static void main(String[] args) {
		byte[] bts = HexString2Bytes("cddad8c144e521");
		System.out.println(Bytes2HexString(bts));
		System.out.println(PackageUtil.bcdToString(bts));
	}
}
