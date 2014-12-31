package org.summer.test;

public class TestBean {
	private String name = "Test Bean";
	private int age;
	private double heigh;
	
	private Address address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeigh() {
		return heigh;
	}

	public void setHeigh(double heigh) {
		this.heigh = heigh;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
