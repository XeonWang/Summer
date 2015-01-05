package org.summer.bean.parse;

import java.lang.reflect.Method;
import java.util.Map;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Property extends BeanConfigItem implements InjectableContainer {
	
	private Injectable value;
	private String name;
	
	public Property(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		this.name = XmlUtils.getNamedAttribute(beanNode, "name");
	}
	
	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.REF};
	}

	@Override
	public void setValue(Injectable value) {
		this.value = value;
	}

	public Injectable getValue() {
		return value;
	}

	public void injectTo(Object obj, Map<String, Bean> configBeans) {
		if (!setWithTypeMatch(obj, configBeans)) 
			setWithConverter(obj, configBeans);
	}

	private void setWithConverter(Object obj, Map<String, Bean> configBeans) {
		Method[] allMethod = obj.getClass().getMethods();
		for (Method method : allMethod) {
			if (!method.getName().equals(getSetterName())) continue;
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				Converter converter = ConvertFactory.getConverter(parameterTypes[0]);
				if (converter != null) {
					try {
						method.invoke(obj, converter.getValue((String)value.getRealValue(configBeans)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private boolean setWithTypeMatch(Object obj, Map<String, Bean> configBeans) {
		try {
			Method setter = obj.getClass().getMethod(getSetterName(), value.getRealType(configBeans));
			setter.invoke(obj, value.getRealValue(configBeans));
		} catch(Exception e) {
			return false;
		}
		return true;
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
