package org.summer.bean.parse;

import java.util.Map;


public class ReferenceBean implements Injectable {
	private String beanId;

	public ReferenceBean(String beanId) {
		super();
		this.beanId = beanId;
	}

	public String getBeanId() {
		return beanId;
	}

	@Override
	public Class<?> getRealType(Map<String, Object> beans) {
		Object refBean = beans.get(beanId);
		return refBean.getClass();
	}

	@Override
	public Object getRealValue(Map<String, Object> beans) {
		return beans.get(beanId);
	}

	@Override
	public Class<?> getType(Map<String, Bean> configBeans) throws ClassNotFoundException {
		return Class.forName(configBeans.get(beanId).getBeanClass());
	}

	@Override
	public Object getValue(Map<String, Bean> configBeans) {
		return configBeans.get(beanId).createBean();
	}

}
