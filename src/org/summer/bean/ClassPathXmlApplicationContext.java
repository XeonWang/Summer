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

		} catch(Exception e) {
			e.printStackTrace();
		}
		
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
