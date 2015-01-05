package org.summer.test;

public class NonStaticFactory {
	
	public ConstructorTest getInstance(String testStr, Address address) {
		return new ConstructorTest(testStr, address);
	}
	
}
