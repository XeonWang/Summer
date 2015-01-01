package org.summer.bean.parse;

import java.util.Map;


public interface Injectable {

	Class<?> getRealType(Map<String, Object> beans);

	Object getRealValue(Map<String, Object> beans);

}
