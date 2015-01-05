package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Bean extends BeanConfigItem {
	
	private String beanId, beanClass;
	private Object bean = null;
	private String factoryMethod;
	
	public Bean(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		beanId = XmlUtils.getNamedAttribute(beanNode, "id");
		beanClass = XmlUtils.getNamedAttribute(beanNode, "class");
		factoryMethod = XmlUtils.getNamedAttribute(beanNode, "factory-method");
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.BEAN_PROPERTY, NodeType.BEAN_CONSTRUCTOR};
	}

	public Object createBean(Map<String, Bean> configBeans) {
		
		if(bean != null) return bean;
		
		parse();
		
		bean = createObjectByConstructorConfig(configBeans);
		if(bean == null) {
			try {
				Constructor<?> c = Class.forName(beanClass).getConstructor(new Class[]{});
				bean = c.newInstance(new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return bean;
	}

	private Object createObjectByConstructorConfig(Map<String, Bean> configBeans) {
		List<Injectable> args = new ArrayList<Injectable>();
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof ConstructorArg) {
				args.add(((ConstructorArg)child).getValue());
			}
		}
		try {
			List<Class<?>> argTypes = getArgTypes(args, configBeans);
			List<Object> argValues = getArgValues(args, configBeans);
			
			InstanceCreator creator = getInstanceCreator(argTypes);
			return createObjectByConstructorAndArgs(creator, argValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private InstanceCreator getInstanceCreator(List<Class<?>> argTypes) {
		if (StringUtils.isEmpty(factoryMethod)) {
			Constructor<?> c = findConstructorByArgTypes(argTypes);
			return InstanceCreator.newInstance(c);
		} else {
			Method method = findFactoryMethod(factoryMethod, argTypes.toArray(new Class<?>[0]));
			return InstanceCreator.newInstance(method, null);
		}
	}

	private Method findFactoryMethod(String factoryMethod, Class<?>[] parameterTypes) {
		try {
			return Class.forName(beanClass).getMethod(factoryMethod, parameterTypes);
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object createObjectByConstructorAndArgs(InstanceCreator creator,
			List<Object> argValues) throws Exception {
		List<Object> desiredValues = buildDesiredValues(creator, argValues);
		return creator.getInstance(desiredValues.toArray());
	}

	private List<Object> buildDesiredValues(InstanceCreator creator,
			List<Object> argValues) {
		List<Object> desiredValues = new ArrayList<Object>();
		Type[] desiredTypes = creator.getParameterTypes();
		for (int i = 0; i < desiredTypes.length; i++) {
			if(desiredTypes[i] == argValues.get(i).getClass()) {
				desiredValues.add(argValues.get(i));
			} else {
				Converter converter = ConvertFactory.getConverter(desiredTypes[i]);
				desiredValues.add(converter.getValue((String)argValues.get(i)));
			}
		}
		return desiredValues;
	}

	private Constructor<?> findConstructorByArgTypes(List<Class<?>> argTypes) {
		try {
			Constructor<?>[] cons = Class.forName(beanClass).getConstructors();
			for(Constructor<?> con : cons) {
				Type[] desiredTypes = con.getParameterTypes();
				
				if(desiredTypes.length != argTypes.size()) continue;
				
				boolean typeMatch = checkEveryArgType(argTypes, desiredTypes);
				
				if(typeMatch)
					return con;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	private boolean checkEveryArgType(List<Class<?>> argTypes,
			Type[] desiredTypes) {
		boolean typeMatch = true;
		for (int i = 0; typeMatch && i < desiredTypes.length; i++) {
			if(desiredTypes[i] == argTypes.get(i)) continue;
			
			boolean existConverter = argTypes.get(i) == String.class && ConvertFactory.getConverter(desiredTypes[i]) != null;
			if(existConverter) continue;
			
			typeMatch = false;
		}
		return typeMatch;
	}

	private List<Class<?>> getArgTypes(List<Injectable> args, Map<String, Bean> configBeans) throws ClassNotFoundException {
		List<Class<?>> argTypes = new ArrayList<Class<?>>();
		for(Injectable arg : args) {
			argTypes.add(arg.getType(configBeans));
		}
		return argTypes;
	}
	
	private List<Object> getArgValues(List<Injectable> args, Map<String, Bean> configBeans) throws ClassNotFoundException {
		List<Object> argValues = new ArrayList<Object>();
		for(Injectable arg : args) {
			argValues.add(arg.getValue(configBeans));
		}
		return argValues;
	}

	public void initBean(Object obj, Map<String, Object> beans) {
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof Property) {
				Property property = (Property)child;
				property.injectTo(obj, beans);
			}
		}		
	}

	public String getBeanId() {
		return beanId;
	}

	public String getBeanClass() {
		return beanClass;
	}
}
