package org.wittydev.j2ee.examples.templateA.bapp;

import java.net.InetAddress;
import java.util.Date;

public interface ServerInfoService {
	public static final String JNDI_NAME="ServerInfoService";
	
	public abstract InetAddress[] getServerInetAddresses() throws Exception;
	public abstract String[] getServerIps() throws Exception;
	public abstract String[] getServerHostNames() throws Exception;
	public abstract long getServerTime();
	public abstract Date getServerDate();
	public abstract String getServerSystemProperty(String propertyName);
}
