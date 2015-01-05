package org.summer.bean.parse;

import java.util.Map;

import org.w3c.dom.Node;

public class BeanConllection extends BeanConfigItem {
	private Map<String, Bean> configBeans;
	
	public BeanConllection(Node beanNode, Map<String, Bean> configBeans) {
		super(beanNode, null);
		this.configBeans = configBeans;
	}

	@Override
	protected NodeType[] getLegalChildTypes() {
		return new NodeType[]{NodeType.BEAN};
	}

	public Map<String, Bean> getConfigBeans() {
		return configBeans;
	}
	
}
