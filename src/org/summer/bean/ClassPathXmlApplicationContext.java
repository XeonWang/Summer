package org.summer.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.summer.bean.parse.Bean;
import org.summer.bean.parse.BeanConfigItem;
import org.summer.bean.parse.BeanConllection;
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
			Node beansNode = extractBeansNode(fileInput);
			parseConfiguration(beansNode);
			
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
			Object obj = configBeans.get(beanId).createBean();
			beans.put(beanId, obj);
		}
	}

	private void parseConfiguration(Node beansNode) {
		NodeList beanNodes = beansNode.getChildNodes();
		BeanConfigItem parent = new BeanConllection(beansNode, configBeans);
		
		for (int i = 0; i < beanNodes.getLength(); i++) {
			Node node = beanNodes.item(i);
			if("bean".equalsIgnoreCase(node.getNodeName())) {
				configBeans.put(XmlUtils.getNamedAttribute(node, "id"), new Bean(beanNodes.item(i), parent));
			}
		}
	}


	private Node extractBeansNode(InputStream fileInput)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(fileInput);
		Node beansNode = document.getChildNodes().item(0);
		return beansNode;
	}

	@Override
	public Object getBean(String beanName) {
		return beans.get(beanName);
	}

}
