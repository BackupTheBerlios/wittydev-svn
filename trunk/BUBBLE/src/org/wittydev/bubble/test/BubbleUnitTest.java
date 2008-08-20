package org.wittydev.bubble.test;

import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wittydev.bubble.Architect;
import org.wittydev.bubble.test.pkg1.BeanA;
import org.wittydev.bubble.test.pkg2.BeanB;
import org.wittydev.core.WDException;
import org.wittydev.util.PathsUtil;

public class BubbleUnitTest {
	static String configPath;
	static Architect archy;
	
	@BeforeClass
	public static void  setUp() throws WDException {
		configPath="P:\\wd2\\trunk\\BUBBLE\\resources\\config";
		archy=new Architect();
		archy.setLoggingDebug(true);
		archy.setLoggingWarning(true);
		archy.setConfigPath(configPath);
		archy.setJndiNamespaceType(Architect.REGISTER_JNDI_NAMESPACE);
		archy.startService();
		
	}
	
	@AfterClass
	public static void unSetUp() throws WDException{
		archy.stopService();
	}
		
	@Test
	public void testReolveName() throws Exception{
		String dummyBeanB1_Path="/org/wittydev/test/TestA/DummyBeanB1";
		String dummyBeanB2_Path="/org/wittydev/test/TestA/DummyBeanB2";
		String dummyBeanA1_Path="/org/wittydev/test/TestA/DummyBeanA1";
		BeanA dummyBeanA1=(BeanA)archy.resolveName(dummyBeanA1_Path, true);
		BeanB dummyBeanB1=(BeanB)(archy.resolveName(dummyBeanB1_Path, true));
		BeanB dummyBeanB2=(BeanB)(archy.resolveName(dummyBeanB2_Path, true));
		
		Assert.assertNotNull(dummyBeanB1);
		Assert.assertNotNull(dummyBeanB2);
		
		Assert.assertNotNull(dummyBeanB1.getBeanBIntArr());
		Assert.assertSame(dummyBeanB1.getBeanBIntArr(), dummyBeanB2.getBeanBIntArr());
		
		Assert.assertNotNull(dummyBeanB1.getBeanBIntObjectArr());
		Assert.assertSame(dummyBeanB1.getBeanBIntObjectArr(), dummyBeanB2.getBeanBIntObjectArr());
		
		Assert.assertNotNull(dummyBeanB1.getBeanBStringArr());
		Assert.assertSame(dummyBeanB1.getBeanBStringArr(), dummyBeanB2.getBeanBStringArr());
		
		Assert.assertSame(dummyBeanA1.getBeanB(), dummyBeanB1);
		
		Assert.assertSame(dummyBeanA1.getBeanB(), dummyBeanB1);
	}

	@Test
	public void testPathNormalizer() throws Exception{
		Assert.assertEquals(
					PathsUtil.normalizePath("/aa/bb/cc/", "/ee/ff/gg.hhh"), "/ee/ff/gg.hhh");	
	
		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "ee/ff/gg.hhh"), "/aa/bb/cc/ee/ff/gg.hhh");	

		
		
		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "./ee/ff/gg.hhh"), "/aa/bb/cc/ee/ff/gg.hhh");	

		
		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "../ee/ff/gg.hhh"), "/aa/bb/ee/ff/gg.hhh");	

		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "../../ee/ff/gg.hhh"), "/aa/ee/ff/gg.hhh");	

		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "ee/../../ff/gg.hhh"), "/aa/bb/ff/gg.hhh");	

		Assert.assertEquals(
				PathsUtil.normalizePath("/aa/bb/cc/", "ee/../../../../../../ff/gg.hhh"), "/ff/gg.hhh");	
		
		
	}
	
	
}
