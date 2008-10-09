package org.wittydev.j2ee.examples.templateA.wapp;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;



public interface ServerInfoRPCWebService extends Remote{
	//@WebMethod
	//public abstract InetAddress[] getServerInetAddresses() throws RemoteException;
	public abstract String[] getServerIps() throws RemoteException;
	public abstract String[] getServerHostNames() throws RemoteException;
	public abstract long getServerTime() throws RemoteException;
	public abstract Date getServerDate() throws RemoteException;
	public abstract String getServerSystemProperty(String propertyName) throws RemoteException;
}

