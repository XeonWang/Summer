package org.summer.bean.convert;

public class DoubleConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Double.valueOf(src);
	}

}
