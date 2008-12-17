package org.wittydev.test.dummies;

import org.hsqldb.util.DatabaseManagerSwing;

/*********
    --driver <classname>  jdbc driver class
    --url <name>          jdbc url
    --user <name>         username used for connection
    --password <password> password for this user
    --urlid <urlid>       use url/user/password/driver in rc file
    --rcfile <file>       (defaults to 'dbmanager.rc' in home dir)
    --dir <path>          default directory
    --script <file>       reads from script file
    --noexit              do not call system.exit()
	(Single-hypen switches like '-driver' are also supported)
 */

public class TestDatabaseManager {
	public static void main(String[] args) {
		 //java -cp /path/to/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing --urlid mem
		String[]arr={
						"--url", "jdbc:hsqldb:hsql://localhost/dummydb;shutdown=true",
						"--user", "sa",
						"--password", "",
						};
		//{};//"--urlid", "mem"};
		
		DatabaseManagerSwing d=new DatabaseManagerSwing();
		
		DatabaseManagerSwing.main(arr);
	}
}
