package org.summer.bean;

import java.lang.reflect.Type;

public interface PropertyValue {
	boolean isSuitableForType(Type type);
	
	Object rightType(Type type);
}
