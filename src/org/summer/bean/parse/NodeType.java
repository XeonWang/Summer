package org.summer.bean.parse;

public enum NodeType {
	
	BEAN_PROPERTY("property", Property.class), 
	BEAN_CONSTRUCTOR("constructor-arg", ConstructorArg.class), 
	REF("ref", Reference.class), 
	CONSTRUCTOR_ARGS_VALUE("value", ConstructorArgsValue.class), 
	BEAN("bean", Bean.class);
	
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
