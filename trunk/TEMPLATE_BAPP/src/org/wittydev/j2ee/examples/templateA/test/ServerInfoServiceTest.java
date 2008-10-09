package org.wittydev.j2ee.examples.templateA.test;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;

import org.wittydev.j2ee.examples.templateA.bapp.ServerInfoService;

public class ServerInfoServiceTest {

	public static void main(String[] args) throws Exception{
        
		Hashtable environment = new Hashtable();
		// The environment configuration depends on the application server the EJB is deployed on
		// The following setting is related to an EJB 3.0 deployed on JBoss 4.2.1 Application server 
		environment.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		environment.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		environment.put(Context.PROVIDER_URL,"localhost:1210");

		// Check the application server documentation for the EJB' JNDI name.  
		String jndiName = "ServerInfoSessionBean/remote"; //ejb-name
        
		InitialContext context = new InitialContext(environment);
		ServerInfoService service = (ServerInfoService)context.lookup(jndiName); 
		
		System.out.println("-->> "+service.getServerDate() );
		System.out.println("-->> lookup object successfully");
	}
}
