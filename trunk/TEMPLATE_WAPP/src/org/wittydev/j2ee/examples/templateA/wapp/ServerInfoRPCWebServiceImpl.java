package org.wittydev.j2ee.examples.templateA.wapp;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Date;

public class ServerInfoRPCWebServiceImpl implements ServerInfoRPCWebService{

		/**/
		public Date getServerDate() {
			
			return new Date();
		}

		public String[] getServerHostNames() throws RemoteException {
			return new String[]{"ciccione"};
		}
		/**/

		public InetAddress[] getServerInetAddresses() throws RemoteException {
			return null;
		}

		/**/
		public String[] getServerIps() throws RemoteException {
			return null;
		}

		public String getServerSystemProperty(String arg0) {
			return null;
		}

		public long getServerTime() {
			return System.currentTimeMillis();
		}
		/**/
		

}
