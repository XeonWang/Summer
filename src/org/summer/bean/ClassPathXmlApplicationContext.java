package org.summer.bean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.summer.util.StringUtils;
import org.summer.util.XmlUtils;
import org.w3c.dom.Document;
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
				
				Map<String, String> properties = extractProperties(bean);
				
				Class<?> desiredClass = Class.forName(beanClazz);
				Constructor<?> constructor = desiredClass.getConstructor(new Class[0]);
				Object obj = constructor.newInstance(new Object[0]);
				
				invokeSetters(properties, desiredClass, obj);
				
				beans.put(beanName, obj);
			} catch(Exception e) {
				
			}
		}
	}

	private void invokeSetters(Map<String, String> properties,
			Class<?> desiredClass, Object obj) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		for (String key : properties.keySet()) {
			Method setter = desiredClass.getMethod(getSetterName(key), new Class[]{String.class});
			setter.invoke(obj, new Object[]{properties.get(key)});
		}
	}

	private Map<String, String> extractProperties(Node bean) {
		Map<String, String> properties = new HashMap<String, String>();
		NodeList subNodes = bean.getChildNodes();
		for (int i = 0; i < subNodes.getLength(); i++) {
			Node sub = subNodes.item(i);
			if(sub.getNodeName().equals("property")) {
				properties.put(XmlUtils.getNamedAttribute(sub, "name"), XmlUtils.getValue(sub));
			}
		}
		return properties;
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
