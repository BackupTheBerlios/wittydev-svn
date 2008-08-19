package org.wittydev.bubble.test;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wittydev.bubble.Architect;
import org.wittydev.core.WDException;

public class BubbleUnitTest {
	static String configPath;
	static Architect archy;
	
	@BeforeClass
	public static void  setUp() throws WDException {
		configPath="P:\\wd2\\trunk\\BUBBLE\\resources\\config";
		archy=new Architect();
		archy.setLoggingDebug(false);
		archy.setConfigPath(configPath);
		archy.startService();
		
	}
	
	@AfterClass
	public static void unSetUp() throws WDException{
		archy.stopService();
	}
	
	
	@Test
	public void testReolveName() throws Exception{
		String dummyBeanA="/org/wittydev/test/TestA/DummyBean";
		Assert.assertNotNull(archy.resolveName(dummyBeanA, true));
	}
	
}
