package org.wittydev.bubble.servlet.http;
import javax.servlet.http.HttpSession;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

import org.wittydev.bubble.BubbleContext;
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

public class SessionsManager extends BubbleScopeManager {
    public Context createSubcontext(Name name) throws NamingException{
       NamingException ne= new NamingException ( "Invalid subcontext creation in SessionsManager." );
       LoggingService.getDefaultLogger().logError( this, ne);
       throw ne;
    }
    public Context createSubcontext(HttpSession session) throws NamingException{
        //Context ctx= new SessionBubbleContext(  this, session);

        /*try{
            ((SessionBubbleContext)ctx).startService();
        }catch(WDException nfje){
            throw new NamingException (nfje.getMessage());
        }*/
        //super.bind( session.getId(), ctx );
        //return ctx;

        return createNewSessionBubbleContext( session );
    }

    public WebArchitect getWebArchitect(){
        return (WebArchitect)getRootContext();
    }

    public SessionBubbleContext getSessionContext(HttpSession session, boolean create){
        SessionBubbleContext sCtx=null;
        try {
            sCtx=(SessionBubbleContext)lookup( session.getId() );
        }catch(NamingException ne){}
        if (sCtx==null && create ){
            try {
                sCtx=(SessionBubbleContext )createSubcontext(session);
            }catch(NamingException ne){
                internalLogWarning(ne);
            }

        }
        return sCtx;
        //lookup_0(session.getId());
        //}catch(Exce)
    }

    public SessionBubbleContext createNewSessionBubbleContext(  HttpSession session){
        return new SessionBubbleContext(  this, session);
    }
}



