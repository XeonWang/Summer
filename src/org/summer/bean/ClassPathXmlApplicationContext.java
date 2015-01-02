package org.summer.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.summer.bean.parse.Bean;
import org.summer.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassPathXmlApplicationContext extends ApplicationContext {

	private Map<String, Bean> configBeans = new HashMap<String, Bean>();
	
	private Map<String, Object> beans = new HashMap<String, Object>();
	
	public ClassPathXmlApplicationContext(String configurationFileName) {
		InputStream fileInput = this.getClass().getClassLoader().getResourceAsStream(configurationFileName);
		try {
			NodeList beanNodes = extractBeanNodes(fileInput);
			parseConfiguration(beanNodes);
			
			createBeans();
			
			initBeans();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private void initBeans() {
		for(String beanId : configBeans.keySet()) {
			Object obj = beans.get(beanId);
			configBeans.get(beanId).initBean(obj, beans);
		}
		
	}

	private void createBeans() {
		for(String beanId : configBeans.keySet()) {
			Object obj = configBeans.get(beanId).createBean(configBeans, beans);
			beans.put(beanId, obj);
		}
	}

	private void parseConfiguration(NodeList beanNodes) {
		for (int i = 0; i < beanNodes.getLength(); i++) {
			Node node = beanNodes.item(i);
			if("bean".equalsIgnoreCase(node.getNodeName())) {
				configBeans.put(XmlUtils.getNamedAttribute(node, "id"), new Bean(beanNodes.item(i), null));
			}
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
