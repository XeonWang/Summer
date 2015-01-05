package org.summer.bean.parse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MethodCreator extends InstanceCreator {
	
	private Method factoryMethod;
	private Object obj;
	
	public MethodCreator(Method factoryMethod, Object obj) {
		super();
		this.factoryMethod = factoryMethod;
		this.obj = obj;
	}

	@Override
	public Object getInstance(Object[] args) {
		try {
			return factoryMethod.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Type[] getParameterTypes() {
		return factoryMethod.getParameterTypes();
	}

}
