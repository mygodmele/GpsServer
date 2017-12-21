package com.hgs.gpsserver.common;

import com.hgs.common.utility.Byte2Hex;


public class ByteArrayUtility {
	private byte[] buffer;
	private int index;
	
	public ByteArrayUtility(byte[] buffer)
	{
		this.buffer = new byte[buffer.length];
		System.arraycopy(buffer, 0, this.buffer, 0, buffer.length);
		index = 0;
	}
	
	public int length()
	{
		if (!hasData())
		{
			return 0;
		}
		
		return buffer.length - index;
	}
	
	public boolean hasData()
	{
		if (buffer == null)
		{
			return false;
		}
		
		if (index >= buffer.length)
		{
			return false;
		}
		return true;
	}
	
	public boolean hasData(int expectedLen)
	{
		if (!hasData())
		{
			return false;
		}
		
		if (index + expectedLen > buffer.length)
		{
			return false;
		}
		return true;
	}
	
	public int cutUnsignedInteger(int length)
	{
		if (!hasData(length)) {
			return -1;
		}
		
		if (length > 4 || length <=0) {
			return -1;
		}
		
		int result;
		if (length == 1) {
			result = buffer[index] & 0xff;
		}
		else if (length == 2) {
			result = (buffer[index + 1] & 0xff) | ((buffer[index] << 8) & 0xff00);
		}
		else if (length == 3) {
			result = (buffer[index + 2] & 0xff) | ((buffer[index + 1] << 8) & 0xff00)
					| ((buffer[index] << 24) >>> 8);
		}
		else {
			result = (buffer[index + 3] & 0xff) | ((buffer[index + 2] << 8) & 0xff00)   
					| ((buffer[index + 1] << 24) >>> 8) | (buffer[index] << 24);
		}
		index += length;
		return result;
	}
	
	public int cutUnsignedIntegerTurnDiff(int length){
		if (!hasData(length)) {
			return -1;
		}
		
		if (length > 4 || length <=0) {
			return -1;
		}
		
		int result;
		if (length == 1) {
			result = buffer[index] & 0xff;
		}
		else if (length == 2) {
			result = (buffer[index] & 0xff) | ((buffer[index + 1] << 8) & 0xff00);
		}
		else if (length == 3) {
			result = (buffer[index] & 0xff) | ((buffer[index + 1] << 8) & 0xff00)
					| ((buffer[index + 2] << 24) >>> 8);
		}
		else {
			result = (buffer[index] & 0xff) | ((buffer[index + 1] << 8) & 0xff00)   
					| ((buffer[index + 2] << 24) >>> 8) | (buffer[index + 3] << 24);
		}
		index += length;
		return result;
	}
	
	public byte getByte() {
		if (!hasData())
		{
			return -1;
		}
		byte result = buffer[index];
		return result;
	}
	
	public byte cutByte()
	{
		if (!hasData())
		{
			return -1;
		}
		byte result = buffer[index];
		index += 1;
		return result;
	}
	
	public void skip(int length)
	{
		index += length;
	}
	
	public byte[] getArray(int length)
	{
		if (!hasData(length))
		{
			return null;
		}
		byte[] array = new byte[length];
		System.arraycopy(buffer, index, array, 0, length);
		return array;
	}
	
	public byte[] cutArray(int length)
	{
		byte[] array = getArray(length);
		if (array != null)
		{
			index += length;
		}
		return array;
	}
	
	public byte[] subArray(int startIndex, int length)
	{
		if (startIndex + length > buffer.length)
		{
			return null;
		}
		byte[] array = new byte[length];
		System.arraycopy(buffer, startIndex, array, 0, length);
		return array;
	}
	
	public int getByteAt(int index)
	{
		if (index < 0 || index >= buffer.length)
		{
			return -1;
		}
		return buffer[index] & 0xff;
	}
	
	public String cutHexString(int length)
	{
		if (!hasData(length))
		{
			return null;
		}
		
		byte[] array = cutArray(length);
		return Byte2Hex.Bytes2HexString(array);
	}
	
	public byte[] cutRemainData()
	{
		if (!hasData())
		{
			return null;
		}
		
		return cutArray(buffer.length - index);
	}
	
	public String toHexString()
	{
		if (!hasData())
		{
			return null;
		}
		byte[] array = cutRemainData();
		return Byte2Hex.Bytes2HexString(array);
	}
	
	public String cutBinString(int dataLength, int strLength)
	{
		if (!hasData())
		{
			return null;
		}
		if (dataLength > 4 || dataLength < 0)
		{
			return null;
		}
		
		int tmpValue = cutUnsignedInteger(dataLength);
		
		String result = Integer.toBinaryString(tmpValue);
		int pads = strLength - result.length();
		if (pads > 0)
		{
			char[] buf = new char[pads];
			for (int i = 0; i < buf.length; i++)
	        {
	            buf[i] = '0';
	        }
			
			result = new String(buf) + result;
		}
		return result;
	}
}
