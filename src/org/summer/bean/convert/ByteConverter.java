package org.summer.bean.convert;

public class ByteConverter implements Converter {

	@Override
	public Object getValue(String src) {
		return Integer.valueOf(src).byteValue();
	}

}
