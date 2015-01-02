package org.summer.bean.convert;

public class FloatConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Float.valueOf(src);
	}

}
