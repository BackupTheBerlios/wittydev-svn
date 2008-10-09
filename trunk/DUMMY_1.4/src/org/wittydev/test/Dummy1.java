package org.wittydev.test;
import java.net.URL;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
/*
import org.wittydev.j2ee.examples.templateA.wapp.ServerInfoRPCWebService;
import org.wittydev.j2ee.examples.templateA.wapp.ServerInfoRPCWebService_Stub;
import org.wittydev.j2ee.examples.templateA.wapp.rpcclient.ServerInfoRPCService;
import org.wittydev.j2ee.examples.templateA.wapp.rpcclient.ServerInfoRPCService_Impl;
*/

import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
//import org.wittydev.j2ee.examples.templateA.wapp.ServerInfoRPCWebService;
import org.wittydev.j2ee.examples.templateA.wapp.ServerInfoRPCWebService;




public class Dummy1 {

	public static void main(String[] args) throws Exception{
		QName qname=new QName(
				"http://wapp.templateA.examples.j2ee.wittydev.org/",
				"ServerInfoRPCService"
					);
		URL url=new URL("http://127.0.0.1:8080/TEMPLATE_WAPP/ServerInfoRPC?wsdl");
		ServiceFactory factory= ServiceFactory.newInstance();
		Service service=factory.createService(url, qname);
		
		QName xmlType = new QName("http://wapp.templateA.examples.j2ee.wittydev.org/types",
										"String.Array");
		/*System.out.println(
					service.getTypeMappingRegistry().getDefaultTypeMapping().getDeserializer(String[].class, xmlType)
				);*/
		
		service.getTypeMappingRegistry().getDefaultTypeMapping()
			.register(
						String[].class, 
						xmlType,
						new ArraySerializerFactory(String[].class, xmlType), 
		                new ArrayDeserializerFactory()
						);
		ServerInfoRPCWebService siws=(ServerInfoRPCWebService)service.getPort(ServerInfoRPCWebService.class);
		//System.out.println(siws.getServerTime());
		String[] rslt=siws.getServerHostNames();
		for (int i=0; i<rslt.length; i++ )
			System.out.println(rslt[i]);
		
		//String[] rslt=siws.getServerHostNames();
		
	}	
	

	
	public static void main2(String[] args) throws Exception{
		Hashtable environment = new Hashtable();
		// The environment configuration depends on the application server the EJB is deployed on
		// The following setting is related to an EJB 3.0 deployed on JBoss 4.2.1 Application server 
		environment.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		environment.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		environment.put(Context.PROVIDER_URL,"localhost:1210");

		// Check the application server documentation for the EJB' JNDI name.  
		String jndiName = "ServerInfoSessionBean/remote"; //ejb-name
        
		InitialContext context = new InitialContext(environment);
		Service service = (Service)context.lookup("java:comp/env/TEMPLATE_WAPP/ServerInfoEndPoint");
		//ServerInfoService service = (ServerInfoService)context.lookup(jndiName); 
		
		//System.out.println("-->> "+service.getServerDate() );
		System.out.println("-->> lookup object successfully");
		
	}
	public static void main1(String[] args) throws Exception{
			//URL wsdlLocation= new URL("http://127.0.0.1:8080/ServerInfoSessionBeanService/ServerInfoSessionBean?wsdl");
			//service=new ServerInfoSessionBeanService(wsdlLocation, new ServerInfoSessionBeanService().getServiceName());
			//service=new ServerInfoSessionBeanService(wsdlLocation, 
			//			new QName("http://bapp.templateA.examples.j2ee.wittydev.org/", "ServerInfoSessionBeanService"));
			/*System.out.println(service.getServiceName().getLocalPart());
			System.out.println(service.getServiceName().getNamespaceURI());
			System.out.println(service.getServiceName().getPrefix());*/
			/*System.out.println(service.getWSDLDocumentLocation());
			ServerInfoWebService hmm= service.getServerInfoSessionBeanPort();
			System.out.println(hmm.getServerTime());
			System.out.println(hmm.getServerTime());*/
			
			QName qname=new QName(
					"http://wapp.templateA.examples.j2ee.wittydev.org/",
					"ServerInfoRPCService"
						);
			URL url=new URL("http://127.0.0.1:8080/TEMPLATE_WAPP/ServerInfoRPC");
			//ServiceFactory factory= ServiceFactory.newInstance();
			//Service service=factory.createService(url, qname);
			//ServerInfoWebService siws=(ServerInfoWebService)service.getPort(ServerInfoWebService.class);
 
			/*ServerInfoRPCService s=new ServerInfoRPCService_Impl();
			
			System.out.println(s.getServerInfoRPCWebServicePort());
			System.out.println(s.getServerInfoRPCWebServicePort().getServerTime());
			*/
			
			//System.out.println(siws);			
		}
		/**/
	}


