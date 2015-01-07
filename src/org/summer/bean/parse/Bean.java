package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.bean.lifecycle.DeclaredInitMethodListener;
import org.summer.bean.lifecycle.InjectedState;
import org.summer.bean.lifecycle.LifeCycleStateChange;
import org.summer.bean.lifecycle.StateChangeListener;
import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Bean extends BeanConfigItem {
	
	private String beanId, beanClass, initMethodName;
	private String factoryMethod;
	private String factoryClass;
	private BeanScope scope = BeanScope.SINGLETON;
	
	private Object bean = null;
	
	private List<StateChangeListener> stateChangeListeners = new ArrayList<StateChangeListener>();
	
	public Bean(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		beanId = XmlUtils.getNamedAttribute(beanNode, "id");
		beanClass = XmlUtils.getNamedAttribute(beanNode, "class");
		initMethodName = XmlUtils.getNamedAttribute(beanNode, "init-method");
		factoryClass = XmlUtils.getNamedAttribute(beanNode, "factory-bean");
		factoryMethod = XmlUtils.getNamedAttribute(beanNode, "factory-method");
		String beanScope = XmlUtils.getNamedAttribute(beanNode, "scope");
		if (BeanScope.PROTOTYPE.name().equalsIgnoreCase(beanScope)) {
			scope = BeanScope.PROTOTYPE;
		}
		
		initStateChangeListeners();
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.BEAN_PROPERTY, NodeType.BEAN_CONSTRUCTOR};
	}

	public Object createBean() {
		Object tempBean;
		if(getBean() != null) return getBean();
		
		parse();
		tempBean = createObjectByConstructorConfig();
		setBean(tempBean);
		
		return tempBean;
	}

	private Object createObjectByConstructorConfig() {
		List<Injectable> args = new ArrayList<Injectable>();
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof ConstructorArg) {
				args.add(((ConstructorArg)child).getValue());
			}
		}
		try {
			List<Class<?>> argTypes = getArgTypes(args);
			List<Object> argValues = getArgValues(args);
			
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
			Object target = getTargetObject();
			return InstanceCreator.newInstance(method, target);
		}
	}

	private Object getTargetObject() {
		if (StringUtils.isEmpty(factoryClass)) return null;
		return ((BeanConllection)getParent()).getConfigBeans().get(factoryClass).createBean();
	}

	private Method findFactoryMethod(String factoryMethod, Class<?>[] parameterTypes) {
		try {
			return getFactoryClass().getMethod(factoryMethod, parameterTypes);
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Class<?> getFactoryClass() throws ClassNotFoundException {
		if (StringUtils.isEmpty(factoryClass)) {
			return Class.forName(beanClass);
		} else { 
			return Class.forName(((BeanConllection)getParent()).getConfigBeans().get(factoryClass).getBeanClass());
		}
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
			Constructor<?>[] cons = getFactoryClass().getConstructors();
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

	private List<Class<?>> getArgTypes(List<Injectable> args) throws ClassNotFoundException {
		List<Class<?>> argTypes = new ArrayList<Class<?>>();
		for(Injectable arg : args) {
			argTypes.add(arg.getType(((BeanConllection)getParent()).getConfigBeans()));
		}
		return argTypes;
	}
	
	private List<Object> getArgValues(List<Injectable> args) throws ClassNotFoundException {
		List<Object> argValues = new ArrayList<Object>();
		for(Injectable arg : args) {
			argValues.add(arg.getValue(((BeanConllection)getParent()).getConfigBeans()));
		}
		return argValues;
	}

	public void initBean(Map<String, Bean> configBeans) {
		
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof Property) {
				Property property = (Property)child;
				property.injectTo(getBean(), configBeans);
			}
		}
		
		publisStateChangeEvent(new InjectedState(this));
	}
	
	private void initStateChangeListeners() {
		stateChangeListeners.add(new DeclaredInitMethodListener());
	}
	
	private void publisStateChangeEvent(LifeCycleStateChange event) {
		for(StateChangeListener listener : stateChangeListeners) {
			listener.process(event);
		}
	}

	public String getBeanId() {
		return beanId;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		if(scope == BeanScope.SINGLETON)
			this.bean = bean;
	}

	public BeanScope getScope() {
		return scope;
	}

	public String getInitMethodName() {
		return initMethodName;
	}
	
}
