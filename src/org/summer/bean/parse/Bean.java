package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Bean extends BeanConfigItem {
	
	private String beanId, beanClass;
	private Object bean = null;
	
	public Bean(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		beanId = XmlUtils.getNamedAttribute(beanNode, "id");
		beanClass = XmlUtils.getNamedAttribute(beanNode, "class");
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
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof ConstructorArgs) {
				List<Injectable> args = ((ConstructorArgs)child).getArgs();
				try {
					List<Class<?>> argTypes = getArgTypes(args, configBeans);
					List<Object> argValues = getArgValues(args, configBeans);
					
					Constructor<?> c = findConstructorByArgTypes(argTypes);
					return createObjectByConstructorAndArgs(c, argValues);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private Object createObjectByConstructorAndArgs(Constructor<?> c,
			List<Object> argValues) throws Exception {
		List<Object> desiredValues = buildDesiredValues(c, argValues);
		return c.newInstance(desiredValues.toArray());
	}

	private List<Object> buildDesiredValues(Constructor<?> c,
			List<Object> argValues) {
		List<Object> desiredValues = new ArrayList<Object>();
		Type[] desiredTypes = c.getParameterTypes();
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
