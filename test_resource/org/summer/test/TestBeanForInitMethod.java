package org.summer.test;

public class TestBeanForInitMethod {
	
	private String testStr;
	
	public void initMethod() {
		testStr = "Success";
	}
	
	public String getTestStr() {
		return testStr;
	}
}
