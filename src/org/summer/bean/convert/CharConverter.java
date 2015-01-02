package org.summer.bean.convert;

public class CharConverter implements Converter {

	@Override
	public Object getValue(String src) {
		if(src.length() > 1) throw new IllegalArgumentException("Try to covert String \"" + src + "\" to char.");
		return src.charAt(0);
	}

}
