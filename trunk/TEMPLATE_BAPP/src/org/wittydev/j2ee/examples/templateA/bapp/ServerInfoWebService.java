package org.wittydev.j2ee.examples.templateA.bapp;

import java.net.InetAddress;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServerInfoWebService {
	@WebMethod
	public abstract InetAddress[] getServerInetAddresses() throws Exception;
	@WebMethod
	public abstract String[] getServerIps() throws Exception;
	@WebMethod
	public abstract String[] getServerHostNames() throws Exception;
	@WebMethod
	public abstract long getServerTime() throws Exception;
	@WebMethod
	public abstract Date getServerDate() throws Exception;
	@WebMethod
	public abstract String getServerSystemProperty(String propertyName) throws Exception;
	
	@WebMethod
	public abstract DummyObj getDummyObj(String myMsg) throws Exception;

}
