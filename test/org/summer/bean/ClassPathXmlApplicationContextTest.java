package org.summer.bean;

import static org.junit.Assert.*;

import org.junit.Test;
import org.summer.test.TestBean;

public class ClassPathXmlApplicationContextTest {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		TestBean testBean = (TestBean)context.getBean("testBean");
		assertEquals("John", testBean.getName());
		assertEquals(20, testBean.getAge());
		assertEquals(173.3, testBean.getHeigh(), 0.001);
	}

}
