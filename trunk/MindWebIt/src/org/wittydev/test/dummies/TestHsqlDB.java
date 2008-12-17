package org.wittydev.test.dummies;

import org.hsqldb.Server;

public class TestHsqlDB {
	public static void main(String[] args) {
		//org.hsqldb.Server.main(arg0) -database.0 file:mydb -dbname.0 xdb
		Server server= new Server();
		server.setDatabaseName(0, "dummydb");
		server.setDatabasePath(0, "C:\\tmp\\hsqldb\\dummydb");
		System.out.println(server.getAddress());
		System.out.println(server.getPort());
		System.out.println(server.getDatabaseType(0));
		
		server.start();
		
		
		
	}
}
