package org.wittydev.test.dummies;

import java.io.IOException;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestJetty {
	public static void main(String[] args) throws Exception{
		/*Handler handler=new AbstractHandler()
		{
		    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
		        throws IOException, ServletException
		    {
		        response.setContentType("text/html");
		        response.setStatus(HttpServletResponse.SC_OK);
		        response.getWriter().println("<h1>Hello</h1>");
		        ((Request)request).setHandled(true);
		    }
		};*/

		//Server server = new Server(8080);
		//server.setHandler(handler);
		
		int port=8080;
		String host="localhost";
		Connector connector = new SelectChannelConnector();
        connector.setPort(port);
        connector.setHost(host);
        
        Server server = new Server();
        server.addConnector(connector);
        //server.setHandler(handler);
        
        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        // this is path to .war OR TO expanded, existing webapp; 
        // WILL FIND web.xml and parse it
        wac.setWar("C:/carlos/dev/projects/eclipse_workspace/MindWebIt/WebAppDir");    
        server.setHandler(wac);
        server.setStopAtShutdown(true);
        
        server.start();		
		
	}
}
