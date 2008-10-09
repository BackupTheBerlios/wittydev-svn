package org.wittydev.j2ee.examples.templateA.bapp;

import java.rmi.RemoteException;
import java.util.List;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

@SuppressWarnings({"serial","unchecked"})
@javax.ejb.Stateless //(name="ServerInfoService", mappedName="ServerInfoServiceJNDI")

// Define the EJB as being a remote service component  
@javax.ejb.Remote ({ServerInfoService.class})

// Define the EJB as being a local service component
//@javax.ejb.Local ({ServerInfoService.class})

// 
@WebService (endpointInterface="org.wittydev.j2ee.examples.templateA.bapp.ServerInfoWebService")
public class ServerInfoSessionBean implements ServerInfoService {
	InetAddress[] inetAddresses;
	String[] ips, hostNames;
	 
	public InetAddress[] getServerInetAddresses() throws RemoteException {
		if (inetAddresses==null){
			java.util.List ias = new ArrayList();
			try {
				for (Enumeration e = NetworkInterface.getNetworkInterfaces(); e!=null && e.hasMoreElements();) {
					NetworkInterface nic=(NetworkInterface)e.nextElement();
					for (Enumeration i=nic.getInetAddresses(); i!=null && i.hasMoreElements();){
						ias.add(i.nextElement());
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
				throw new RemoteException(e.getMessage(), e);
			}
			inetAddresses = (InetAddress[])ias.toArray(
													new InetAddress[ias.size()]);
		}
		
		for (int i=0; i<inetAddresses.length; i++)
			System.out.println("inetAddresses==>"+inetAddresses[i]);
		return inetAddresses;
	}

	public String[] getServerIps() throws RemoteException{
		if (this.ips==null) {
			InetAddress[] ias=getServerInetAddresses();
			if (ias==null) return null;
			String[] ips=new String[ias.length]; 
			for (int i=0; i<ias.length; i++){
				if (ias[i] != null)
				ips[i] = ias[i].getHostAddress(); //Returns the IP address string in textual presentation.
			}
			this.ips=ips;
		}
		
		return ips;
	}

	public String[] getServerHostNames() throws RemoteException{
		if (this.hostNames==null) {
			InetAddress[] ias=getServerInetAddresses();
			if (ias==null) return null;
			String[] hostNames=new String[ias.length]; 
			for (int i=0; i<ias.length; i++){
				if (ias[i] != null)
					hostNames[i] = ias[i].getHostName(); //Returns the IP address string in textual presentation.
			}
			this.hostNames=hostNames;
		}
		
		return hostNames;
	}	
	
	public long getServerTime() {
		return System.currentTimeMillis();
	}

	public Date getServerDate() {
		return new java.util.Date();
	}
	public Properties getServerSystemProperties() {
		return System.getProperties();
	}
	public String getServerSystemProperty(String propertyName) {
		return System.getProperty(propertyName);
	}
	
	public DummyObj getDummyObj( String myMsg ) throws Exception{
		DummyObj d= new DummyObj(myMsg+"- OKEY??");
		System.out.println("==>"+d.getASting());
		return d;
	}

	
}
