package org.summer.bean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.summer.bean.convert.ConvertFactory;
import org.summer.bean.convert.Converter;
import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassPathXmlApplicationContext extends ApplicationContext {

	private Map<String, Object> beans = new HashMap<String, Object>();
	
	public ClassPathXmlApplicationContext(String configurationFileName) {
		InputStream fileInput = this.getClass().getClassLoader().getResourceAsStream(configurationFileName);
		try {
			NodeList beanNodes = extractBeanNodes(fileInput);
			for (int i = 0; i < beanNodes.getLength(); i++) {
				Node bean = beanNodes.item(i);
				createBean(bean);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private void createBean(Node bean) {
		if(bean.getNodeName().equals("bean")) {
			try {
				String beanName = XmlUtils.getNamedAttribute(bean, "id");
				String beanClazz = XmlUtils.getNamedAttribute(bean, "class");
				
				Map<String, PropertyValue> properties = extractProperties(bean);
				
				Class<?> desiredClass = Class.forName(beanClazz);
				Constructor<?> constructor = desiredClass.getConstructor(new Class[0]);
				Object obj = constructor.newInstance(new Object[0]);
				
				invokeSetters(properties, desiredClass, obj);
				
				beans.put(beanName, obj);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void invokeSetters(Map<String, PropertyValue> properties,
			Class<?> desiredClass, Object obj) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		
		Method[] methods = desiredClass.getMethods();
		
		for (String key : properties.keySet()) {
			PropertyValue value = properties.get(key);
			String setterName = getSetterName(key);
			
			for (Method method : methods) {
				if (method.getName().equals(setterName)) {
					Type[] types = method.getGenericParameterTypes();
					if (value.isSuitableForType(types[0])) {
						method.invoke(obj, new Object[]{value.rightType(types[0])});
					}
				
//					Object typedValue = covertToRightType(value, types[0]);
//					
//					if(typedValue != null) {
//						method.invoke(obj, new Object[]{typedValue});
//						break;
//					}
				}
			}
			
		}
	}

//	private Object covertToRightType(PropertyValue value, Type type) {
////		if(((Class<?>)type).isInstance(value)) return value;
//		Converter converter = ConvertFactory.getConverter(type);
//		if (converter != null) return converter.getValue((String)value.);
//		return null;
//	}

	private Map<String, PropertyValue> extractProperties(Node bean) {
		Map<String, PropertyValue> properties = new HashMap<String, PropertyValue>();
		NodeList subNodes = bean.getChildNodes();
		for (int i = 0; i < subNodes.getLength(); i++) {
			Node sub = subNodes.item(i);
			if(sub.getNodeName().equals("property")) {
				properties.put(XmlUtils.getNamedAttribute(sub, "name"), getPropertyValue(sub));
			}
		}
		return properties;
	}
	
	private PropertyValue getPropertyValue(Node node) {
		String valueAttr = XmlUtils.getNamedAttribute(node, "value");
		if (valueAttr != null) return new StringPropertyValue(valueAttr);

		NodeList contentNodes = node.getChildNodes();
		for (int i = 0; i < contentNodes.getLength(); i++) {
			Node subOfProperty = contentNodes.item(i);
			if("ref".equalsIgnoreCase(subOfProperty.getNodeName())) {
				return new BeanPropertyValue(XmlUtils.getNamedAttribute(subOfProperty, "bean"));
			}
		}
		
		return new StringPropertyValue(node.getTextContent());
	}

	private String getSetterName(String key) {
		String capitalizedKey = StringUtils.capitalize(key);
		return "set" + capitalizedKey;
	}

	private NodeList extractBeanNodes(InputStream fileInput)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(fileInput);
		NodeList beansNode = document.getChildNodes();
		NodeList beanNodes = beansNode.item(0).getChildNodes();
		return beanNodes;
	}

	@Override
	public Object getBean(String beanName) {
		return beans.get(beanName);
	}

}
