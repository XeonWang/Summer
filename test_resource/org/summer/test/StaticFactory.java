package org.summer.test;

public class StaticFactory {
	
	public static ConstructorTest getInstance(String testStr, Address address) {
		return new ConstructorTest(testStr, address);
	}
	
}
