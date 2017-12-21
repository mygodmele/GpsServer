package com.hgs.common.utility;

public class StringUtil {

	public static boolean isBlank(String st) {
		if (st == null) {
			return true;
		}
		if (st.trim().length() == 0) {
			return true;
		}
		return false;
	}

}
