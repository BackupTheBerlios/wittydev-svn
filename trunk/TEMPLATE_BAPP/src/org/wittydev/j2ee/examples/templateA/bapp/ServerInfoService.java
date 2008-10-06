package org.wittydev.j2ee.examples.templateA.bapp;

import java.net.InetAddress;
import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

public interface ServerInfoService extends ServerInfoWebService{
	public static final String JNDI_NAME="ServerInfoService";
}
