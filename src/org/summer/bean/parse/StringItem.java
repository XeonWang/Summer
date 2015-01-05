package org.summer.bean.parse;

import java.util.Map;

public class StringItem implements Injectable {
	
	private String value;
	
	public StringItem(String value) {
		super();
		this.value = value;
	}

	@Override
	public Class<?> getRealType(Map<String, Bean> configBeans) {
		return String.class;
	}

	@Override
	public Object getRealValue(Map<String, Bean> configBeans) {
		return value;
	}

	@Override
	public Class<?> getType(Map<String, Bean> configBeans)
			throws ClassNotFoundException {
		return String.class;
	}

	@Override
	public Object getValue(Map<String, Bean> configBeans) {
		return value;
	}

}
