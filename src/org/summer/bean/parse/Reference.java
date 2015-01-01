package org.summer.bean.parse;

import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class Reference extends BeanConfigItem {
	
	private String beanId;

	public Reference(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
		beanId = XmlUtils.getNamedAttribute(beanNode, "bean");
	}
	
	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{};
	}
	
	@Override
	protected void parse() {
		super.parse();
		if (getParent() instanceof Property) {
			Property parent = (Property)getParent();
			parent.setValue(new ReferenceBean(beanId));
		}
	}
}
