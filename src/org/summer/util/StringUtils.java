package org.summer.util;

public class StringUtils {
	public static String capitalize(String src) {
		return src.toUpperCase().charAt(0) + src.substring(1);
	}
}
