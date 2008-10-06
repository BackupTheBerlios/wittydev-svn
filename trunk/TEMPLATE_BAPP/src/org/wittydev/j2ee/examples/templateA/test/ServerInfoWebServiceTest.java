package org.wittydev.j2ee.examples.templateA.test;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceRef;

import org.wittydev.j2ee.examples.templateA.bapp.jaxws.client.ServerInfoSessionBeanService;
import org.wittydev.j2ee.examples.templateA.bapp.jaxws.client.ServerInfoWebService;
/**/
/**/
public class ServerInfoWebServiceTest {
	/**/
	@WebServiceRef(wsdlLocation="http://127.0.0.1:8080/ServerInfoSessionBeanService/ServerInfoSessionBean?wsdl")
	static ServerInfoSessionBeanService service; 
	public static void main(String[] args) throws Exception{
		URL wsdlLocation= new URL("http://127.0.0.1:8080/ServerInfoSessionBeanService/ServerInfoSessionBean?wsdl");
		//service=new ServerInfoSessionBeanService(wsdlLocation, new ServerInfoSessionBeanService().getServiceName());
		service=new ServerInfoSessionBeanService(wsdlLocation, 
					new QName("http://bapp.templateA.examples.j2ee.wittydev.org/", "ServerInfoSessionBeanService"));
		System.out.println(service.getServiceName().getLocalPart());
		System.out.println(service.getServiceName().getNamespaceURI());
		System.out.println(service.getServiceName().getPrefix());
		System.out.println(service.getWSDLDocumentLocation());
		ServerInfoWebService hmm= service.getServerInfoSessionBeanPort();
		System.out.println(hmm.getServerTime());
		System.out.println(hmm.getServerTime());
	}
	/**/
}
