package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Bean extends BeanConfigItem {
	private static NodeType[] legalChildTypes = {NodeType.BEAN_PROPERTY};
	
	private String beanId;
	private String beanClass;
	
	public Bean(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		beanId = XmlUtils.getNamedAttribute(beanNode, "id");
		beanClass = XmlUtils.getNamedAttribute(beanNode, "class");
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return legalChildTypes;
	}

	public String getBeanId() {
		return beanId;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public Object createBean() {
		Object obj = null;
		Constructor<?> c;
		try {
			c = Class.forName(beanClass).getConstructor(new Class[]{});
			obj = c.newInstance(new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return obj;
	}

	public void initBean(Object obj, Map<String, Object> beans) {
		parse();
		
		for(BeanConfigItem child : getChildren()) {
			if(child instanceof Property) {
				Property property = (Property)child;
				property.injectTo(obj, beans);
			}
		}		
	}
}
