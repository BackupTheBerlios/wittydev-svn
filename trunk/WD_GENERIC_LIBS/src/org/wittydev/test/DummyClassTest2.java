/**
 * 
 */
package org.wittydev.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author khayat
 *
 */
public class DummyClassTest2 {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("BeforeClass annotation (setUpBeforeClass)");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("AfterClass annotation (tearDownAfterClass)");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("Before annotation (setUp)");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("After annotation (tearDown)");
	}

	/**
	 * Test method for {@link org.wittydev.test.DummyClass#multiply(int, int)}.
	 */
	@Test
	//@Ignore
	public void testMultiply() {
		try {
			System.out.println("prima");
			fail("Not yet implemented");
			System.out.println("dopooooo");
		} catch (Error e) {
			//e.printStackTrace();
			System.out.println("==>"+e);
			
			throw e;
		}
	}

	
	@Test
	//@Ignore
	public void testMultiply2() {
		throw new RuntimeException ("dummy");
	}
	
	@Test(expected= RuntimeException.class)
	//@Ignore
	public void testMultiply2b() {
		throw new RuntimeException ("dummy");
	}
	
	@Test
	//@Ignore
	public void testMultiply3() {
		throw new AssertionError ("dummy assertion");
	}

	public void testMultiply4() {
		System.out.println("test4");
	}
	
	
}
