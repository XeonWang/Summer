package org.summer.bean.convert;

public class BooleanConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Boolean.valueOf(src);
	}

}
