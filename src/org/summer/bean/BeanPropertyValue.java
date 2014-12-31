package org.summer.bean;

import java.lang.reflect.Type;

public class BeanPropertyValue implements PropertyValue {
	private String refId;
	
	@Override
	public boolean isSuitableForType(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object rightType(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	public BeanPropertyValue(String refId) {
		this.refId = refId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
	
}
