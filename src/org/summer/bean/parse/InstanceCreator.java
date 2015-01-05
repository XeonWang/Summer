package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public abstract class InstanceCreator {
	abstract Object getInstance(Object[] args);
	abstract Type[] getParameterTypes();
	
	public static InstanceCreator newInstance(Constructor<?> c) {
		return new ConstructorCreator(c);
	}
	
	public static InstanceCreator newInstance(Method factoryMethod, Object obj) {
		return new MethodCreator(factoryMethod, obj);
	}
}
