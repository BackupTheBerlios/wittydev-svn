package org.wittydev.bubble.servlet.http;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletInputStream;

import org.wittydev.bubble.BubbleContext;
import org.wittydev.config.ConfigEntry;
import org.wittydev.core.WDException;
import org.wittydev.logging.LoggingService;
import org.wittydev.util.BeansUtil;


import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BubbleHttpServletRequest extends HttpServletRequestWrapper {
    WebArchitect webArchitect;
    //Dictionary dict;
    Map paramsMap;

    protected BubbleHttpServletRequest(){
        //this(new org.nfj.test.servlet.DummyHttpServletRequest(), null);
        this(null, null);

    }
    public BubbleHttpServletRequest(
                                    HttpServletRequest originalRequest,
                                    WebArchitect webArchitect ) {
        super( originalRequest );
        this.webArchitect=webArchitect;

    }

    public WebArchitect getWebArchitect(){
        return this.webArchitect;
    }

    public Object resolveName( String componentPath ){
        return resolveName(componentPath, true );
    }
    public Object resolveName( String componentPath, boolean create ){
        //System.out.println(getWebArchitect());
        //System.out.println(getWebArchitect().getSessionsManager());
        if (componentPath==null)return null;
        //ConfigEntry conf=getWebArchitect().getConfigEntry(componentPath);
        //if (conf==null || conf.getScope()==null)
        //    return getWebArchitect().resolveName( componentPath, request)

        //System.out.println("=>"+getWebArchitect().getSessionsManager());
        //System.out.println("=>"+getWebArchitect().getSessionsManager().getSessionContext(getSession(), true));
        RequestBubbleContext ctx =getWebArchitect().getSessionsManager().
                        getSessionContext ( getSession(), true ).
                        getRequestsManager().getRequestContext(this, true);

        return ctx.resolveName( componentPath, create  );
        /*if ( ctx==null )
            return getWebArchitect().resolveName ( componentPath, create  );
        else
            return ctx.resolveName ( componentPath, create  );*/
    }

    public void setParameter(String name, Object value){
        if (paramsMap==null)paramsMap=new HashMap();
        paramsMap.put(  name, value );
    }

    public Object getObjectParameter(String name){
        String objectPath=name, propertyName=null;
        int pos=objectPath.indexOf(".");
        if ( pos>0){
            propertyName=objectPath.substring(pos+1);
            objectPath=objectPath.substring(0, pos);
        }
        Object val;
        if ( paramsMap!=null){
            val=paramsMap.get(objectPath);
            if ( val!=null ){
                if ( propertyName!=null){
                    try{
                        val=BeansUtil.resolveValueIW( val, propertyName );
                    }catch(WDException e){
                        LoggingService.getDefaultLogger().logWarning(this, e);
                        val=null;
                    }
                }
                return val;
            }else
                return getRequest().getParameter( name );

        }else
            return getRequest().getParameter( name );

    }
    public String getParameter(String name){
        Object val=getObjectParameter(name);
        if ( val!=null)
            return val.toString();
        else
            return null;
            //return "";
    }

    /*public Object getObjectParameter(String name){
        return (paramsMap==null)?null:paramsMap.get(name);
    }
    public String getParameter(String name){
        Object val=(paramsMap==null)?null:paramsMap.get(name);
        if (val==null )val=getRequest().getParameter( name );
        if ( val!=null)
            return val.toString();
        else
            return null;
    }*/


    /*public javax.servlet.ServletRequest getRequest() {
        return this;
    }*/

}