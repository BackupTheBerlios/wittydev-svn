package org.wittydev.bubble.servlet.http;

import javax.servlet.ServletRequest;
import javax.naming.NamingException;

import java.io.BufferedReader;
import java.util.Map;
import java.io.IOException;
import java.util.Enumeration;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;

import org.wittydev.bubble.BubbleServiceEvent;
import org.wittydev.bubble.ScopeBubbleContext;
import org.wittydev.core.WDException;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class RequestBubbleContext extends ScopeBubbleContext implements ServletRequest {
    ServletRequest request;
    String requestId;
    public static final String BUBBLES_REQUEST_CONTEXT_KEY="_bubble_request_context_key_";
    public static final String BUBBLES_REQUEST_CONTEXT_ID_KEY="_bubble_request_context_id_key_";

    public RequestBubbleContext(RequestsManager parent, String reqId, ServletRequest request) {
        super(reqId, parent);
        this.request=request;
        this.requestId=reqId;
        request.setAttribute( BUBBLES_REQUEST_CONTEXT_ID_KEY, reqId );
        request.setAttribute( BUBBLES_REQUEST_CONTEXT_KEY, this );
        bindIt();
    }

    public void bindIt(){
        try {
            getParent().bind(requestId, this);
        }catch(NamingException ne){
            internalLogWarning( ne );
        }

        try{
            BubbleServiceEvent event=new BubbleServiceEvent(
                                                        this, getParent(),
                                                        requestId);
            this.startService(event);
        }catch(WDException iwe){
            internalLogWarning( iwe );
        }
    }
    public void unbindIt(){
        try {
            getParent().unbind(requestId);
        }catch(NamingException ne){
            internalLogWarning( ne );
        }
        this.stopService();
    }

    public RequestsManager getRequestsManager(){
        return (RequestsManager)getParent();
    }

    public ServletRequest getOriginaloRequest(){
        return this.request;
    }

    /////////////////////////////////////////////
    public Object getAttribute(String p0){ return request.getAttribute(p0) ;}
    public Enumeration getAttributeNames(){ return request.getAttributeNames() ;}
    public String getCharacterEncoding(){ return request.getCharacterEncoding() ;}

    public int getContentLength(){ return request.getContentLength() ;}
    public String getContentType(){ return request.getContentType() ;}
    public javax.servlet.ServletInputStream getInputStream() throws IOException{ return request.getInputStream()  ;}
    public String getParameter(String p0){ return request.getParameter(p0) ;}
    public Enumeration getParameterNames(){ return request.getParameterNames() ;}
    public String[] getParameterValues(String p0){ return request.getParameterValues(p0) ;}

    public String getProtocol(){ return request.getProtocol();}
    public String getScheme(){ return request.getScheme() ;}
    public String getServerName(){ return request.getServerName() ;}
    public int getServerPort(){ return request.getServerPort() ;}
    public BufferedReader getReader() throws IOException{ return request.getReader() ;}
    public String getRemoteAddr(){ return request.getRemoteAddr() ;}
    public String getRemoteHost(){ return request.getRemoteHost()  ;}
    public void setAttribute(String p0, Object p1){ request.setAttribute( p0, p1);  }
    public void removeAttribute(String p0){ request.removeAttribute( p0) ;}
    public Locale getLocale(){ return request.getLocale() ;}
    public Enumeration getLocales(){ return request.getLocales() ;}
    public boolean isSecure(){ return request.isSecure() ;}
    public RequestDispatcher getRequestDispatcher(String p0){ return request.getRequestDispatcher(p0) ;}
    public String getRealPath(String p0){ return request.getRealPath(p0) ;}

    public int getRemotePort(){ return request.getRemotePort()  ;}
    public String getLocalName(){ return request.getLocalName() ;}
    public String getLocalAddr(){ return request.getLocalAddr() ;}
    public int getLocalPort(){ return request.getLocalPort() ;}
    public Map getParameterMap(){ return request.getParameterMap() ;}
    public void setCharacterEncoding(String p0) throws UnsupportedEncodingException{
        request.setCharacterEncoding(p0);
    }
}