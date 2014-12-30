package org.summer.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlUtils {
	public static String getValue(Node node) {
		NamedNodeMap attrs = node.getAttributes();
		Node valueAttr = attrs.getNamedItem("value");
		return valueAttr != null ? valueAttr.getNodeValue() : node.getTextContent();
	}
	
	public static String getName(Node node) {
		return node.getNodeName();
	}
	
	public static String getNamedAttribute(Node node, String attrName) {
		NamedNodeMap attrs = node.getAttributes();
		return attrs.getNamedItem(attrName).getNodeValue();
	}
}
