package org.summer.bean.lifecycle;

public interface StateChangeListener {
	void process(LifeCycleStateChange event);
}
