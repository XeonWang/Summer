package org.summer.bean.lifecycle;

import org.summer.bean.parse.Bean;

public class InjectedState implements LifeCycleStateChange {
	
	private Bean configBean;
	
	public InjectedState(Bean configBean) {
		super();
		this.configBean = configBean;
	}

	@Override
	public Bean getConfigBean() {
		return configBean;
	}

}
