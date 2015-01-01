package org.summer.bean.parse;

public enum NodeType {
	
	BEAN_PROPERTY("property", Property.class), PROPERTY_REF("ref", Reference.class);
	
	private String nodeName;
	private Class<? extends BeanConfigItem> type; 
	
	private NodeType(String nodeName, Class<? extends BeanConfigItem> type) {
		this.nodeName = nodeName;
		this.type = type;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public Class<? extends BeanConfigItem> getType() {
		return type;
	}
}
