package org.wittydev.bubble.servlet.http;
import java.io.IOException;
import java.io.BufferedInputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletInputStream;

import org.wittydev.bubble.servlet.EnsureAvailableMethodInServletInputStream;
import org.wittydev.logging.LoggingService;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class EnsureAvailableMethodInRequestInputStreamFilter implements Filter{
    public void init(FilterConfig conf) throws ServletException{
       // System.out.println("A============>Calling init on AvailInReq Filter");
       LoggingService.getDefaultLogger().logInfo(this, "EnsureAvailableMethodInRequestInputStreamFilter INIT!");
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        //System.out.println("AvailInReq============>doFilter");
        //request = new Buffer
        if ( request instanceof HttpServletRequest )
            request = new AvailableIsHttpRequest ((HttpServletRequest)request);
        else
            request = new AvailableIsRequest (request);


        chain.doFilter(request, response);

        //System.out.println("============>After chain.doFilter [AvailInReq]");

    }
    public void destroy(){
        LoggingService.getDefaultLogger().logInfo(this, "EnsureAvailableMethodInRequestInputStreamFilter DESTROY!");
    }

    public class AvailableIsHttpRequest extends HttpServletRequestWrapper {
        EnsureAvailableMethodInServletInputStream bufIs;
        public AvailableIsHttpRequest(HttpServletRequest request){
            super(request);
        }
        public ServletInputStream getInputStream() throws IOException {
            if ( bufIs==null) bufIs=new EnsureAvailableMethodInServletInputStream(super.getInputStream());
            return bufIs;
        }
    }
    public class AvailableIsRequest extends ServletRequestWrapper {
        EnsureAvailableMethodInServletInputStream bufIs;
        public AvailableIsRequest(ServletRequest request){
            super(request);
        }

        public ServletInputStream getInputStream() throws IOException {
            if ( bufIs==null) bufIs=new EnsureAvailableMethodInServletInputStream(super.getInputStream());
            return bufIs;
        }
    }

}

