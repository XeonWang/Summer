package org.summer.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlUtils {
	
	public static String getName(Node node) {
		return node.getNodeName();
	}
	
	public static String getNamedAttribute(Node node, String attrName) {
		NamedNodeMap attrs = node.getAttributes();
		Node attr = attrs.getNamedItem(attrName);
		return attr == null ? null : attr.getNodeValue();
	}
}
