package org.summer.bean.convert;

public class IntegerConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Integer.valueOf(src);
	}

}
