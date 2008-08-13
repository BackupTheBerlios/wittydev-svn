package org.wittydev.test;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnitTest1 {
	@Test
	public void simpleAdd(){
		int result=1;
		int expected=1;
		assertEquals(result, expected);
	}

	@Test
	public void simpleBadAdd(){
		int result=1;
		int expected=2;
		assertEquals(result, expected);
	}

	@Test
	public void simpleVeryBadAdd(){
		throw new RuntimeException("ciccione");
	}	
	
}
