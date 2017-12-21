package com.hgs.common.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static String dateFormat = "yyyy/MM/dd HH:mm:ss";
	
	private static SimpleDateFormat format = new SimpleDateFormat(dateFormat);

	public static String formatData(String orgPat, String disPat, String data) {
		orgPat = StringUtil.isBlank(orgPat) ? "yyMMddHHmmss" : orgPat;
		disPat = StringUtil.isBlank(disPat) ?  dateFormat : disPat;
		SimpleDateFormat orgFor = new SimpleDateFormat(orgPat);
		SimpleDateFormat disFor = new SimpleDateFormat(disPat);
		try {
			return disFor.format(orgFor.parse(data));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getServerDate(){
		SimpleDateFormat orgFor = new SimpleDateFormat(dateFormat);
		String date = orgFor.format(new Date());
		return date;
	}
	
	public static String getCurDate() {
		return format.format(new Date());
	}
	
	public static String convertToDateStr(long time) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date(time));
	}
	
	public static void main(String[] args) {
		System.out.println(convertToDateStr(System.currentTimeMillis()));
	}
	
}
