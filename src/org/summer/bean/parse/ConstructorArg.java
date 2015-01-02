package org.summer.bean.parse;

import org.w3c.dom.Node;

public class ConstructorArg extends BeanConfigItem implements InjectableContainer {
	
	private Injectable value;
	
	public ConstructorArg(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.CONSTRUCTOR_ARGS_VALUE, NodeType.REF};
	}

	public Injectable getValue() {
		return value;
	}

	@Override
	public void setValue(Injectable value) {
		this.value = value;
	}

}
