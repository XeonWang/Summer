package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class ConstructorCreator extends InstanceCreator {

	private Constructor<?> creator;
	
	public ConstructorCreator(Constructor<?> creator) {
		super();
		this.creator = creator;
	}

	@Override
	public Object getInstance(Object[] args) {
		try {
			return creator.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	public Type[] getParameterTypes() {
		return creator.getParameterTypes();
	}

}
