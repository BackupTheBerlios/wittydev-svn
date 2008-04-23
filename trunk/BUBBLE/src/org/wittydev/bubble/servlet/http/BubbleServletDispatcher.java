package org.wittydev.bubble.servlet.http;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletConfig;

import org.wittydev.logging.LoggingService;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BubbleServletDispatcher extends GenericServlet{
    public static final String BUFFER_SIZE_PARAM="ResponseBufferSize";
    int bufferSize=-2;
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        for ( Enumeration e= conf.getInitParameterNames(); e.hasMoreElements(); ){
            String key=((String)e.nextElement());
            String value=conf.getInitParameter(key);
            if ( key.trim().equalsIgnoreCase(BUFFER_SIZE_PARAM) )
                bufferSize=Integer.parseInt(value );
            else
                LoggingService.getDefaultLogger().logWarning(this, "In valid init parameter: ["+key+":"+value+"]");

        }

    }
    public void service(ServletRequest request, ServletResponse response)
                                                    throws ServletException, IOException{

         //System.out.println("Dispatcher called===>"+request);
         if ( request instanceof  HttpServletRequest ){
            WebArchitect wa=(WebArchitect)getServletContext().getAttribute(BubbleWebContainerListener.WEB_ARCHITECT);
            if ( wa ==null ){
                ServletException e=new ServletException("Internal error, WebArchitect not found.");
                LoggingService.getDefaultLogger().logError( this, e);
                throw e;
            }
            HttpServletRequest httpReq=(HttpServletRequest)request;
            String servletPath=httpReq.getServletPath();
            if ( servletPath==null){
                ServletException e=new ServletException("Invalid servlet path: Null");
                LoggingService.getDefaultLogger().logError( this, e);
                throw e;
            }else{
                Servlet servlet=wa.getServlet(servletPath);
                if (servlet==null){
                    ServletException e=new ServletException("Invalid servlet path: "+servletPath);
                    LoggingService.getDefaultLogger().logError( this, e);
                    throw e;
                }
                if ( bufferSize>-2) {
                    //System.out.println("setting buffersize =========================>"+bufferSize);
                    response.setBufferSize(bufferSize );
                    //System.out.println("getting buffersize =========================>"+response.getBufferSize());
                }
                servlet.service(request, response);
                //System.out.println("readLine =========================>?");
            }

         }else{
            ServletException e=new ServletException("Invalid request type!");
            LoggingService.getDefaultLogger().logError( this, e);
            throw e;
         }

    }

}