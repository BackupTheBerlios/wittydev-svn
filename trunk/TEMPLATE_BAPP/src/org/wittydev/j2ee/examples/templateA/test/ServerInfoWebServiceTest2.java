package org.wittydev.j2ee.examples.templateA.test;

 

import java.net.InetAddress;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.wittydev.j2ee.examples.templateA.bapp.ServerInfoWebService;



public class ServerInfoWebServiceTest2 {
	public static void main(String[] args) throws Exception{
		String wsdlLocation="http://127.0.0.1:8080/ServerInfoSessionBeanService/ServerInfoSessionBean?wsdl";
		
		URL url = new URL(wsdlLocation);
		
		QName qname = new QName(
						"http://bapp.templateA.examples.j2ee.wittydev.org/", 
						"ServerInfoSessionBeanService");

		Service service=Service.create(url, qname);
		ServerInfoWebService siw=(ServerInfoWebService)service.getPort(ServerInfoWebService.class);
		
		System.out.println(siw.getServerTime());
		System.out.println(siw.getServerInetAddresses());
		System.out.println(siw.getDummyObj("vediamo un po'").getASting());
		
		InetAddress[] ias=siw.getServerInetAddresses();
		for (int i=0; i<ias.length; i++)
				System.out.println("==>"+ias[i]);
	}
}
