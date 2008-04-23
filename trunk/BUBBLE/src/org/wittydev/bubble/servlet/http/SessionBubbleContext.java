package org.wittydev.bubble.servlet.http;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.naming.NamingException;

import org.wittydev.bubble.Architect;
import org.wittydev.bubble.BubbleContext;
import org.wittydev.bubble.BubbleServiceEvent;
import org.wittydev.bubble.ScopeBubbleContext;
import org.wittydev.config.ConfigEntry;
import org.wittydev.core.WDException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class SessionBubbleContext extends ScopeBubbleContext
                                    implements HttpSessionBindingListener, HttpSession{
    //public static final String SESSION_SCOPE_ID="session";
    RequestsManager requestsManager;
    HttpSession session;
    public static final String BUBBLES_SESSION_CONTEXT_KEY="_bubble_session_context_key_";

    /*public SessionBubbleContext(SessionsManager  parent, String httpSessionId) {
        super(httpSessionId, parent);
        this.session=session;
        session.setAttribute( BUBBLES_SESSION_CONTEXT_KEY, this );

    }*/

    public SessionBubbleContext(SessionsManager  parent, HttpSession session) {
        super(session.getId(), parent);
        this.session=session;
        session.setAttribute( BUBBLES_SESSION_CONTEXT_KEY, this );

    }

    public void valueBound(HttpSessionBindingEvent p0){
        //getParent().unbind(p0.getSession().getId());
        try {
            getParent().bind(p0.getSession().getId(), this);
        }catch(NamingException ne){
            internalLogWarning( ne );
        }

        try{
            BubbleServiceEvent event=new BubbleServiceEvent( this, getParent(),
                                                        p0.getSession().getId());
            this.startService(event);
        }catch(WDException iwe){
            internalLogWarning( iwe );
        }
    }

    public void valueUnbound(HttpSessionBindingEvent p0){
        try {
            getParent().unbind(p0.getSession().getId());
        }catch(NamingException ne){
            internalLogWarning( ne );
        }
        this.stopService();
    }

    public SessionsManager getSessionsManager(){
        return (SessionsManager)getParent();
    }

    public boolean isValid(){
        return isRunning() ;
    }
    /*public Object resolveName( String componentPath ){
        return resolveName(componentPath, true );
    }
    public Object resolveName( String componentPath, boolean create ){
        ConfigEntry cr=null;
        try{
            cr=getSessionsManager().getWebArchitect().getConfigEntry(componentPath);
        }catch(WDException iwe){
            internalLogError( iwe  );
            return null;
        }
        String scope=cr.getScope();
        Object result;
        if ( scope!=null && scope.equalsIgnoreCase("session") )
            result=getSessionsManager().getWebArchitect().resolveName(componentPath, this, create);
        else if ( scope!=null && scope.equalsIgnoreCase("request") ){
            internalLogError("Request components not supported: "+componentPath);
            result=null;
        }else
            result=getSessionsManager().getWebArchitect().resolveName(componentPath, null, create);

        return result;
    }*/

    public RequestsManager getRequestsManager(){
        if (this.requestsManager==null){
            //internalLogInfo("RequestsManager component not configured, using default!");
            this.requestsManager= new RequestsManager();
            requestsManager.setParentScope(this);
            requestsManager.setScopeId("request");
            try{
                bind("RequestsManager", requestsManager);
            }catch(NamingException  ne){
                internalLogError( ne);
            }
            try{
                this.requestsManager.startService(new BubbleServiceEvent(this, this, "RequestsManager") );
            }catch(WDException wde){
                internalLogError( wde );
            }
    }
        return this.requestsManager;
    }
    /*protected void setRequestsManager( RequestsManager requestsManager ){
        this.requestsManager=requestsManager;
        if ( requestsManager!=null ){
             if (  requestsManager.getParentScope() !=null &&
                    requestsManager.getParentScope()!=this )
                    internalLogWarning("Resetting RequestsManager Parent Scope!");

            requestsManager.setParentScope(this);
        }

    }*/

    public long getCreationTime(){
        return session.getCreationTime();
    }
    public String getId(){
        return session.getId();
    }
    public long getLastAccessedTime(){
        return session.getLastAccessedTime();
    }
    public ServletContext getServletContext(){
        return session.getServletContext();
    }
    public void setMaxInactiveInterval(int p0){
        session.setMaxInactiveInterval( p0 );
    }
    public int getMaxInactiveInterval(){
        return session.getMaxInactiveInterval();
    }
    public HttpSessionContext getSessionContext(){
        return session.getSessionContext();
    }
    public Object getAttribute(String p0){
        return session.getAttribute(p0);
    }
    public Object getValue(String p0){
        return session.getValue(p0);
    }
    public Enumeration getAttributeNames(){
        return session.getAttributeNames();
    }
    public String[] getValueNames(){
        return session.getValueNames();
    }
    public void setAttribute(String p0, Object p1){
        session.setAttribute(p0, p1);
    }
    public void putValue(String p0, Object p1){
        session.putValue( p0, p1);
    }
    public void removeAttribute(String p0){
        session.removeAttribute(p0);
    }
    public void removeValue(String p0){
        session.removeValue(p0);
    }
    public void invalidate(){
        session.invalidate();
    }
    public boolean isNew(){
        return session.isNew();
    }

}