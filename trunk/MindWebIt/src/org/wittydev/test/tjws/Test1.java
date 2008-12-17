package org.wittydev.test.tjws;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Acme.Serve.Serve;

public class Test1 {
	
		public static void main(String... args) {
			final Acme.Serve.Serve.PathTreeDictionary aliases = new Acme.Serve.Serve.PathTreeDictionary();
			final Acme.Serve.Serve srv = new Acme.Serve.Serve() {
							{
								setMappingTable(aliases);
								System.setProperty("tjws.webappdir", "p:\\wd2\\trunk\\MindWebIt\\WebAppDir"); 
								addWarDeployer(null, null);
							}
				// Overriding method for public access
	                        public void setMappingTable(PathTreeDictionary mappingtable) { 
	                              super.setMappingTable(mappingtable);
	                        }
	                        // add the method below when .war deployment is needed
	                        public void addWarDeployer(String deployerFactory, String throttles) {
	                              super.addWarDeployer(deployerFactory, throttles);
	                        }
	                };
			// setting aliases, for an optional file servlet
	                
	                aliases.put("/", new java.io.File("C:\\temp"));
	                
			//  note cast name will depend on the class name, since it is anonymous class
	                //((Test1$1)srv).setMappingTable(aliases);
	                
			// setting properties for the server, and exchangable Acceptors
			java.util.Properties properties = new java.util.Properties();
			properties.put("port", 80);
			//System.setProperty(); 
			properties.setProperty(Acme.Serve.Serve.ARG_NOHUP, "nohup");
			srv.arguments = properties;
			srv.addDefaultServlets(null); // optional file servlet
			//srv.addServlet("/myservlet", new MyServlet()); // optional
			srv.addServlet("/myservlet", new HttpServlet(){
												public void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
													//super.doGet(arg0, arg1);
													System.out.println("aaaaaaaaaaaaa");
													super.doGet(request, response);
												}
											});
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					try {
						srv.notifyStop();
					}catch(java.io.IOException ioe) {
						
					}
					srv.destroyAllServlets();
				}
			}));
			srv.serve();
		}
	
}
