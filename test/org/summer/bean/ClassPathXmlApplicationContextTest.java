package org.summer.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.summer.test.ConstructorTest;
import org.summer.test.TestBean;

public class ClassPathXmlApplicationContextTest {
	
	private ApplicationContext context;
	
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("beans.xml");
	}

	@Test
	public void testSetterInject() {
		
		TestBean testBean = (TestBean)context.getBean("testBean");
		assertEquals("John", testBean.getName());
		assertEquals(20, testBean.getAge());
		assertEquals(173.3, testBean.getHeigh(), 0.001);
		
		assertEquals("China", testBean.getAddress().getCountry());
	}
	
	@Test
	public void testConstructorInject() {
		ConstructorTest ct = (ConstructorTest)context.getBean("constructorTest");
		assertEquals("TestString", ct.getTestStr());
		assertEquals("China", ct.getAddress().getCountry());
	}
	
	@Test
	public void testStaticFactoryMethod() {
		ConstructorTest ct = (ConstructorTest)context.getBean("staticFactoryMethodTest");
		assertEquals("TestString", ct.getTestStr());
		assertEquals("China", ct.getAddress().getCountry());
	}
	
	@Test
	public void testNonStaticFactoryMethod() {
		ConstructorTest ct = (ConstructorTest)context.getBean("beanFromNonStaticFactory");
		assertEquals("TestString", ct.getTestStr());
		assertEquals("China", ct.getAddress().getCountry());
	}

}
