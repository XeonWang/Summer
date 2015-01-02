package org.summer.test;

public class ConstructorTest {
	private String testStr;
	private Address address;

	public ConstructorTest(String testStr, Address address) {
		super();
		this.testStr = testStr;
		this.address = address;
	}

	public String getTestStr() {
		return testStr;
	}

	public Address getAddress() {
		return address;
	}
	
}
