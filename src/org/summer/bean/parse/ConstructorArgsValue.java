package org.summer.bean.parse;

import org.summer.util.XmlUtils;
import org.w3c.dom.Node;

public class ConstructorArgsValue extends BeanConfigItem {

	public ConstructorArgsValue(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{};
	}

	@Override
	protected void parse() {
		super.parse();
		StringItem value = new StringItem(XmlUtils.getContent(getBeanNode()));
		((ConstructorArg)getParent()).setValue(value);
	}
}
