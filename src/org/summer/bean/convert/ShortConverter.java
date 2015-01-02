package org.summer.bean.convert;

public class ShortConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Short.valueOf(src);
	}

}
