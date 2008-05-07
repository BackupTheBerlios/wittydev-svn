package org.wittydev.bubble.bubble;
import java.util.Hashtable;
import javax.naming.Name;
import javax.naming.Context;

import org.wittydev.core.WDException;
import org.wittydev.logging.LoggingService;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class bubbleURLContextFactory implements javax.naming.spi.ObjectFactory{
    private static Context bubbleInitialContext;
    private String className;
    public bubbleURLContextFactory(){
        String className=getClass().getName();
        int pos=className.lastIndexOf(".");
        if (pos>=0) className=className.substring(pos+1);
        this.className=className;
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
				    Hashtable environment) throws Exception{
        //System.out.println("\n\n\n\n\n========>calling:"+this+" [obj:"+obj+"][name:"+name+"][nameCtx:"+nameCtx+"]\n\n\n\n");
        if (LoggingService.getDefaultLogger().isLoggingInfo() );
            LoggingService.getDefaultLogger().logInfo(this, "jndi domain lookup: [obj:"+obj+"][name:"+name+"][nameCtx:"+nameCtx+"]");
        return bubbleInitialContext;
        /*if ( name!=null && nameCtx!=null ){
            LoggingService.getDefaultLogger().logWarning( this,
                                "bubbleURLContextFactory should only be used for resolving 'bubble' scheme Context!" );
            return null;
        }*/
        //if ( obj == null || !(obj instanceof String) ){
        /*if ( name == null ){
            LoggingService.getDefaultLogger().logWarning( this,
                                "bubbleURLContextFactory should only be used for resolving 'bubble' scheme Context! [2]" );
            return null;
        } else {
            //String scheme=(String)obj;
            String scheme=name.toString() ;
            if ( (scheme + "URLContextFactory").equals(className))
                return bubbleInitialContext;
            else{
            LoggingService.getDefaultLogger().logWarning( this,
                                "bubbleURLContextFactory should only be used for resolving 'bubble' scheme Context! [3] ["+scheme+"]" );
                return null;
            }
        }*/

    }

    public static void setBubbleContext( Context bubbleInitialContext ) throws WDException {
        if ( bubbleURLContextFactory.bubbleInitialContext!=null)  {
            WDException iwe=new WDException(WDException.CODE_SYSTEM_EXCEPTION, "bubble scheme Already Bound" );
            throw iwe;
        }
        bubbleURLContextFactory.bubbleInitialContext=bubbleInitialContext;
    }

    public static Context getBubbleContext( ) throws WDException {
        return bubbleURLContextFactory.bubbleInitialContext;
    }
    public static Context unsetBubbleContext( ) throws WDException {
        Context ctx=bubbleURLContextFactory.bubbleInitialContext;
        bubbleURLContextFactory.bubbleInitialContext=null;
        return ctx;
    }



}