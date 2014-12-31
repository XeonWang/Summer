package org.summer.bean.parse;

import org.w3c.dom.Node;

public class Bean implements Parseable {
	private Node beanNode;
	
	public Bean(Node beanNode) {
		this.beanNode = beanNode;
	}

	@Override
	public void parse() {
		// TODO Auto-generated method stub

	}

}
