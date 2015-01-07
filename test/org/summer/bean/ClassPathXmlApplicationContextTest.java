package org.summer.bean;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.summer.test.ConstructorTest;
import org.summer.test.TestBean;
import org.summer.test.TestBeanForInitMethod;

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
	
	@Test
	public void testSingletonBean() {
		TestBean singletonBean1 = (TestBean)context.getBean("singletonBean");
		TestBean singletonBean2 = (TestBean)context.getBean("singletonBean");
		assertTrue(singletonBean1 == singletonBean2);
	}
	
	@Test
	public void testPrototypeBean() {
		TestBean prototypeBean1 = (TestBean)context.getBean("prototypeBean");
		TestBean prototypeBean2 = (TestBean)context.getBean("prototypeBean");
		assertEquals("China", prototypeBean1.getAddress().getCountry());
		assertTrue(prototypeBean1 != prototypeBean2);
	}
	
	@Test
	public void testInitMethod() {
		TestBeanForInitMethod bean = (TestBeanForInitMethod)context.getBean("testBeanForInitMethod");
		assertEquals("Success", bean.getTestStr());
	}

}
