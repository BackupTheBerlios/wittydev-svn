package org.wittydev.logging;

import java.util.HashMap;
import java.util.Map;

import org.wittydev.logging.impl.SimpleLogger;


public class LoggingService {
    private static Logger logger;
    private static Map dict=new HashMap();
    public LoggingService() {}

    public static Logger getDefaultLogger(){
        if (logger==null)logger=new SimpleLogger ();
        return LoggingService.logger;
    }

    public static void setDefaultLogger(Logger logger){
        LoggingService.logger=logger;
    }

    /**
     * metodi statici da implementare in fururo
     */
    public static void setLogger(String serviceName, Logger logger){
        if ( serviceName ==null || logger==null ) {
            throw new NullPointerException();
            //return;
        }

        dict.put( serviceName, logger );
    }
    public static void removeLogger(String serviceName){
        if ( serviceName ==null  )
            return;
        else
            dict.remove( serviceName );
    }
    public static Logger getLogger(String serviceName){
        if ( serviceName ==null ) return getDefaultLogger();
        Logger logger=(Logger)dict.get(serviceName);
        if (logger==null)
            return getDefaultLogger();
        else
            return logger;
    }
}
