package org.wittydev.j2ee.examples.templateA.bapp;

import java.net.InetAddress;
import java.rmi.Remote;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServerInfoWebService extends Remote{
	@WebMethod
	public abstract InetAddress[] getServerInetAddresses() throws Exception;
	@WebMethod
	public abstract String[] getServerIps() throws Exception;
	@WebMethod
	public abstract String[] getServerHostNames() throws Exception;
	@WebMethod
	public abstract long getServerTime();
	@WebMethod
	public abstract Date getServerDate();
	@WebMethod
	public abstract String getServerSystemProperty(String propertyName);
}
