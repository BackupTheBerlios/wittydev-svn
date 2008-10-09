package org.wittydev.j2ee.examples.templateA.test;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;

import org.wittydev.j2ee.examples.templateA.wapp.ServerInfoRPCWebService;


///import org.wittydev.j2ee.examples.templateA.wapp.rpcclient.ServerInfoRPCService;
///import org.wittydev.j2ee.examples.templateA.wapp.rpcclient.ServerInfoRPCService_Impl;

public class ServerInfoRPCServiceTest {
	public static void main(String[] args) throws Exception {
		QName qname=new QName(
				"http://bapp.templateA.examples.j2ee.wittydev.org/",
				"ServerInfoRPCService"
					);
		URL url=new URL("http://127.0.0.1:8080/TEMPLATE_WAPP/ServerInfoRPC?wsdl");
		ServiceFactory factory= ServiceFactory.newInstance();
		Service service=factory.createService(url, qname);
		ServerInfoRPCWebService siws=(ServerInfoRPCWebService)service.getPort(ServerInfoRPCWebService.class);
		
		/***
		ServerInfoRPCService s=new ServerInfoRPCService_Impl();
		System.out.println(s.getServerInfoWebServicePort());
		System.out.println(s.getServerInfoWebServicePort().getServerTime());
		***/
		System.out.println(siws);
	}
}
