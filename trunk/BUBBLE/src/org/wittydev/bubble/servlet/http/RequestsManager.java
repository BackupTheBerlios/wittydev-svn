package org.wittydev.bubble.servlet.http;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;


import javax.servlet.ServletRequest;

import org.wittydev.bubble.BubbleScopeManager;
import org.wittydev.logging.LoggingService;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class RequestsManager extends BubbleScopeManager {


    public Context createSubcontext(Name name) throws NamingException{
       NamingException ne= new NamingException ( "Invalid subcontext creation in RequestsManager." );
       LoggingService.getDefaultLogger().logError( this, ne);
       throw ne;
    }

    public Context createSubcontext(ServletRequest request) throws NamingException{
        return createNewRequestBubbleContext( request );
    }
    public RequestBubbleContext createNewRequestBubbleContext(  ServletRequest request)throws NamingException{
        String reqId=(String)request.getAttribute(RequestBubbleContext.BUBBLES_REQUEST_CONTEXT_ID_KEY );
        if ( reqId!=null ) throw new NamingException ("Request "+reqId+" already bound!");
        reqId="BubbleRequest-"+getNewId();
        return new RequestBubbleContext(  this, reqId, request);
    }
    public WebArchitect getWebArchitect(){
        return (WebArchitect)getRootContext();
    }

    public SessionBubbleContext getSessionContext(){
        return (SessionBubbleContext)getParent();
    }

    public RequestBubbleContext getRequestContext(ServletRequest request, boolean create){
        RequestBubbleContext rCtx=null;
        try {
            String str=(String)request.getAttribute(RequestBubbleContext.BUBBLES_REQUEST_CONTEXT_ID_KEY);
            if ( str==null ) throw new NamingException ("Request not bound yet");
            rCtx=(RequestBubbleContext)lookup( str );
        }catch(NamingException ne){}
        if (rCtx==null && create ){
            try {
                rCtx=(RequestBubbleContext )createSubcontext(request);
            }catch(NamingException ne){
                internalLogWarning(ne);
            }

        }
        return rCtx;
        //lookup_0(session.getId());
        //}catch(Exce)
    }
    Object dummy=new Object();
    long currentId;
    private long getNewId(){
        synchronized(dummy){
            return currentId++;
        }
    }





}