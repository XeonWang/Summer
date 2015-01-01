package org.summer.bean.parse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Property extends BeanConfigItem {
	
	private static NodeType[] legalChildTypes = {NodeType.PROPERTY_REF};
	
	private Injectable value;
	private String name;
	
	public Property(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		this.name = XmlUtils.getNamedAttribute(beanNode, "name");
	}
	
	@Override
	protected NodeType[] getLegalChildTypes() {
		return legalChildTypes;
	}

	public void setValue(Injectable value) {
		this.value = value;
	}

	public Injectable getValue() {
		return value;
	}

	public void injectTo(Object obj, Map<String, Object> beans) {
		Method setter = null;
		try {
			setter = obj.getClass().getMethod(getSetterName(), value.getRealType(beans));
			setter.invoke(obj, value.getRealValue(beans));
		} catch (NoSuchMethodException e) {
			Method[] allMethod = obj.getClass().getMethods();
			for (Method method : allMethod) {
				if (!method.getName().equals(getSetterName())) continue;
				
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length == 1) {
					Converter converter = ConvertFactory.getConverter(parameterTypes[0]);
					if (converter != null) {
						try {
							method.invoke(obj, converter.getValue((String)value.getRealValue(beans)));
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getSetterName() {
		return "set" + StringUtils.capitalize(name);
	}

	@Override
	protected void parse() {
		super.parse();
		if (value == null) {
			String contentValue = XmlUtils.getContent(getBeanNode());
			if (!StringUtils.isEmpty(contentValue)) {
				value = new StringItem(contentValue);
			}
		}
	}
}
