package org.summer.util;

public class StringUtils {
	public static String capitalize(String src) {
		return src.toUpperCase().charAt(0) + src.substring(1);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}
}
