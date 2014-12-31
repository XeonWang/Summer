package org.summer.bean;

import java.lang.reflect.Type;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;

public class StringPropertyValue implements PropertyValue {
	private String value;
	private Converter converter;

	@Override
	public boolean isSuitableForType(Type type) {
		Converter converter = ConvertFactory.getConverter(type);
		if (converter != null) {
			this.converter = converter;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object rightType(Type type) {
		if(isSuitableForType(type)) {
			return converter.getValue(value);
		}
		return null;
	}

	public StringPropertyValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
