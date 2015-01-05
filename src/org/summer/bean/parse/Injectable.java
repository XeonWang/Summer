package org.summer.bean.parse;

import java.util.Map;


public interface Injectable {

	Class<?> getRealType(Map<String, Bean> configBeans);

	Object getRealValue(Map<String, Bean> configBeans);

	Class<?> getType(Map<String, Bean> configBeans) throws ClassNotFoundException;

	Object getValue(Map<String, Bean> configBeans);

}
