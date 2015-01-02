package org.summer.bean.convert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConvertFactory {
	
	private static Map<String, Converter> primitiveTypeMap = new HashMap<String, Converter>();
	
	static {
		primitiveTypeMap.put("int", new IntegerConverter());
		primitiveTypeMap.put("double", new DoubleConverter());
		primitiveTypeMap.put("float", new FloatConverter());
		primitiveTypeMap.put("long", new LongConverter());
		primitiveTypeMap.put("boolean", new BooleanConverter());
		primitiveTypeMap.put("char", new CharConverter());
		primitiveTypeMap.put("short", new ShortConverter());
		primitiveTypeMap.put("byte", new ByteConverter());
	}
	
	public static Converter getConverter(Type type) {
		Class<?> clazz = (Class<?>)type;
		if (clazz.isPrimitive()) {
			return primitiveTypeMap.get(clazz.getName());
		}
		
		String defaultConverterName = "org.summer.bean.convert." + ((Class<?>)type).getSimpleName() + "Converter";
		try {
			Class<?> converterClass = Class.forName(defaultConverterName);
			if (Converter.class.isAssignableFrom(converterClass)) {
				return (Converter)converterClass.newInstance();
			}
		} catch (Exception e) {
			//Do nothing, will return null at the end of method.
		} 
		return null;
	}
}
