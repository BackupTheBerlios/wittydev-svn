package org.wittydev.bubble.servlet.http;
import javax.servlet.http.HttpSession;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.wittydev.bubble.Architect;
import org.wittydev.bubble.BubbleContext;
import org.wittydev.config.ConfigLoader;
import org.wittydev.config.servlet.http.WebConfigLoader;
import org.wittydev.logging.LoggingService;
import org.wittydev.util.OrderedMap;


import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
//import javax.servlet.http.HttpSessionBindingListener;
import java.util.Iterator;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class WebArchitect extends Architect{
    protected  OrderedMap servletsMap;
    //public static final String DEFAULT_SCOPE_ID="general";
    String sessionsManagerComponentPath;
    SessionsManager sessionsManager;
    ServletContext servletContext;
    
    /*public WebArchitect() {
    	internalLogWarning("New instance without ServletContext... Testing?");
    }*/
    public WebArchitect(ServletContext servletContext) {
        super();
        this.servletContext=servletContext;
    }

    protected ConfigLoader getNewConfigLoaderInstance(){
    	return new WebConfigLoader(servletContext);
    }
    
    /*public SessionsManager getSessionsManager(){
        if (sessionsManager==null){
            if ( sessionsManagerComponentPath ==null ){
                sessionsManagerComponentPath="/SessionsManager";
                sessionsManager=new SessionsManager( this);
                try{
                    bind(sessionsManagerComponentPath,sessionsManager);
                }catch( javax.naming.NamingException ne ){
                    internalLogError(ne);
                }
            }else
                sessionsManager=(SessionsManager)resolveName( sessionsManagerComponentPath, true);
        }
        return sessionsManager;
    }*/
    /*public BubbleContext getSessionContext( HttpSession session, boolean create){
        //session.
        return null;
    }*/

    public SessionsManager getSessionsManager(){
        return this.sessionsManager;
    }
    public  void setSessionsManager(SessionsManager sessionsManager){
        this.sessionsManager=sessionsManager;
        if ( sessionsManager!=null ){
            if ( sessionsManager.getParentScope() !=null &&
                    sessionsManager.getParentScope()!=this )
                    internalLogWarning("Resetting SessionsManager Parent Scope!");
            sessionsManager.setParentScope(this);
        }
    }


    java.util.Hashtable servletsCache;
    public javax.servlet.Servlet getServlet(String servletName){
        if ( servletsCache== null )
            return null;
        else
            return ( javax.servlet.Servlet )servletsCache.get(servletName);

    }

    public OrderedMap getServletsMap(){
        return this.servletsMap;
    }
    public void setServletsMap(OrderedMap servletsMap){
        this.servletsMap=servletsMap;
        if ( servletsCache==null )
            servletsCache=new java.util.Hashtable();
        else{
            for (Enumeration e=servletsCache.elements(); e.hasMoreElements(); ){
                Servlet oldServ=(Servlet)e.nextElement();
                try {
                    oldServ.destroy();
                }catch(Exception exc){
                    internalLogError(exc);
                }
            }
            servletsCache.clear();
        }

        if ( servletsMap!=null ){
            for ( Iterator it=servletsMap.keySet().iterator(); it.hasNext(); ){
                String key=(String)it.next();
                String val=(String)servletsMap.get(key);

                Object obj=resolveName( val, true );
                if ( obj== null )
                    internalLogError("Component ["+val+"] not found, servlet ["+key+"] discarded!");
                else if ( !(obj instanceof Servlet))
                    internalLogError("Component ["+val+"] is not a Servlet, servlet ["+key+"] discarded!");
                else
                    servletsCache.put(key, obj);
            }
        }

    }

    /*public String getSessionsManagerComponentPath(){
        return this.sessionsManagerComponentPath;
    }
    public void setSessionsManagerComponentPath(String sessionsManagerComponentPath){
        this.sessionsManagerComponentPath=sessionsManagerComponentPath;
    }*/

    public static void main ( String[] args ) throws Exception{
        //String confP="D:\\web\\jakarta-tomcat-5.0.27\\webapps\\bubble\\WEB-INF\\config";
    	String confP="D:\\temp\\bubble\\config";
    	LoggingService.getDefaultLogger().setLoggingTrace(true);
        WebArchitect wa=new WebArchitect(null);
        wa.setConfigPath( confP );
        wa.startService(null);
        
        System.out.println(wa.getConfigLoader().getPathSeparator());
        //wa.resolveName("/com/nfj/servlets/ProvaServlet");
        Object obj = wa.resolveName("/Architect");
        System.out.println(obj);
        
        

    }
}















