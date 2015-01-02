package org.summer.bean.convert;

public class LongConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Long.valueOf(src);
	}

}
