package org.summer.bean.parse;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

public class ConstructorArgs extends BeanConfigItem{
	
	private List<Injectable> args = new ArrayList<Injectable>();
	
	public ConstructorArgs(Node beanNode, BeanConfigItem parent) {
		super(beanNode, parent);
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.CONSTRUCTOR_ARGS_VALUE};
	}

	public List<Injectable> getArgs() {
		return args;
	}

}
