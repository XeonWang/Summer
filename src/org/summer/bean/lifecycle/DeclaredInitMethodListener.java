package org.summer.bean.lifecycle;

import java.lang.reflect.Method;

import org.summer.util.StringUtils;

public class DeclaredInitMethodListener implements StateChangeListener {

	@Override
	public void process(LifeCycleStateChange event) {
		if(event instanceof InjectedState) {
			Object bean = event.getConfigBean().getBean();
			String initMethodName = event.getConfigBean().getInitMethodName();
			if(StringUtils.isEmpty(initMethodName)) return;
			try {
				Method initMethod = bean.getClass().getMethod(initMethodName, new Class[]{});
				initMethod.invoke(bean, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
