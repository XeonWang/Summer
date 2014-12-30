package org.summer.bean.convert;

public class StringConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return src;
	}

}
