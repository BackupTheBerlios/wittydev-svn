package org.wittydev.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class DummyClassTest {

	@Test
	public void testMultiply() {
		DummyClass tester= new DummyClass();
		assertEquals("Result", 50, tester.multiply(10, 5));
		//fail("Not yet implemented");
	}

}
