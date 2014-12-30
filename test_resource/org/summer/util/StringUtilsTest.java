package org.summer.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {
	
	@Test
	public void testCapitalize() {
		String src = "abc";
		assertEquals("Abc", StringUtils.capitalize(src));
	}
}
