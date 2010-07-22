package org.wittydev.bubble.servlet.http;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletContextEvent;

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

public class BubbleWebContainerListener implements ServletContextListener {
    public static final String WEB_ARCHITECT="_WEB_ARCHITECT_";
    
    {
    	LoggingService.getDefaultLogger().setLoggingDebug(true);
    	LoggingService.getDefaultLogger().setLoggingWarning(true);
    	LoggingService.getDefaultLogger().setLoggingError(true);
    	LoggingService.getDefaultLogger().setLoggingInfo(true);
    }


    public void contextInitialized(ServletContextEvent event){
        WebArchitect wa=(WebArchitect)event.getServletContext().getAttribute(WEB_ARCHITECT);
        if ( wa!=null ){
            LoggingService.getDefaultLogger().logWarning( this, "Bubble already set....");
       }else{
            LoggingService.getDefaultLogger().logInfo(this, "Starting bubble...");
            try{
                wa = (WebArchitect)org.wittydev.bubble.bubble.bubbleURLContextFactory.getBubbleContext();
            }catch(WDException iwe){
                LoggingService.getDefaultLogger().logError(this, iwe);
                LoggingService.getDefaultLogger().logError(this, "Bubble not started!!");
                return;
            }
            if (wa!=null){
                LoggingService.getDefaultLogger().logError(this, "Setting Bubble Initial Context found in factory!!");
            }else{
                wa=createWebArchitect(event.getServletContext());
                
                String confPath=event.getServletContext().getInitParameter(WebArchitect.CONFIG_PATH_KEY);
                LoggingService.getDefaultLogger().logInfo(this, "CONFIG_PATH:["+confPath+"]");
                if ( confPath!=null ) wa.setConfigPath(confPath);
                try{
                    wa.startService();
                    org.wittydev.bubble.bubble.bubbleURLContextFactory.setBubbleContext( wa );
                }catch(WDException iwe){
                    LoggingService.getDefaultLogger().logError(this, iwe);
                    LoggingService.getDefaultLogger().logError(this, "Bubble not started!!");
                    return;
                }
            }

            event.getServletContext().setAttribute(WEB_ARCHITECT, wa);
            LoggingService.getDefaultLogger().logInfo(this, "Bubble started!");
        }
    }
    public void contextDestroyed(ServletContextEvent event){
        LoggingService.getDefaultLogger().logInfo(this, "Stopping bubble...");
        LoggingService.getDefaultLogger().logInfo(this, "Bubble stopped!");
        WebArchitect wa=(WebArchitect)event.getServletContext().getAttribute(WEB_ARCHITECT);
        if ( wa!=null)wa.stopService();

    }

    public WebArchitect createWebArchitect(ServletContext servletContext){
        return new WebArchitect(servletContext);
    }
}