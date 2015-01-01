package org.summer.bean.parse;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class BeanConfigItem {
	
	private Node beanNode;
	private List<BeanConfigItem> children = new ArrayList<BeanConfigItem>();
	private BeanConfigItem parent;
	
	public BeanConfigItem(Node beanNode, BeanConfigItem parent) {
		this.beanNode = beanNode;
		this.parent = parent;
		
		NodeList childNodes = beanNode.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			BeanConfigItem child = createChild(childNode);
			if (child != null ) {
				getChildren().add(child);
			}
		}
	}
	
	private BeanConfigItem createChild(Node childNode) {
		for(NodeType type : getLegalChildTypes()) {
			if(type.getNodeName().equalsIgnoreCase(childNode.getNodeName())) {
				try {
					Constructor<? extends BeanConfigItem> constructor = type.getType().getConstructor(new Class[]{Node.class, BeanConfigItem.class});
					return constructor.newInstance(new Object[]{childNode, this});
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return null;
	}
	
	protected void parse() {
		for (BeanConfigItem child : getChildren())
			child.parse();
	}
	
	protected Node getBeanNode() {
		return beanNode;
	}
	
	public List<BeanConfigItem> getChildren() {
		return children;
	}
	
	public BeanConfigItem getParent() {
		return parent;
	}

	protected abstract NodeType[] getLegalChildTypes();
}
