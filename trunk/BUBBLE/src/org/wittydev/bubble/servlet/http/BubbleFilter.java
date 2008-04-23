package org.wittydev.bubble.servlet.http;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wittydev.bubble.bubble.bubbleURLContextFactory;
import org.wittydev.bubble.servlet.http.BubbleHttpServletRequest;
import org.wittydev.bubble.servlet.http.BubbleHttpServletResponse;
import org.wittydev.bubble.servlet.http.WebArchitect;
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

public class BubbleFilter implements Filter{
    WebArchitect webArchitect;
    public void init(FilterConfig conf) throws ServletException{
        LoggingService.getDefaultLogger().logInfo(this, "BubbleFilter INIT!");
        //System.out.println("A============>Calling init on testFilter");
        /*for ( java.util.Enumeration e=conf.getInitParameterNames(); e.hasMoreElements();){
            String key=(String)e.nextElement();
            if ( key.trim().equalsIgnoreCase(INSURE_REQUEST_INPUTSTREAM_AVAILABLE_METHOD )){
                String val=conf.getInitParameter(key);
                if ( val!=null && val.trim().equalsIgnoreCase("true") )
                    insureRequestInputStreamAvailableMethod=true;
                break;
            }
        }*/
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        //System.out.println("============>doFilter");
        //System.out.println("PrimaAAAAAAAAAAAAAAAAAAAAAAresponse.getBufferSize()============>"+response.getBufferSize());
        //System.out.println("DopAAAAAAAAAAAAAAAAAAAAAAresponse.getBufferSize()============>"+response.getBufferSize());
        WebArchitect webArchitect = getWebArchitect();
        if (  webArchitect!=null &&  request instanceof HttpServletRequest  ){
            request=wrapRequest((HttpServletRequest)request, webArchitect);
            response=wrapResponse((HttpServletResponse)response, webArchitect);
            //response=new BubbleHttpServletResponse((HttpServletResponse)response, webArchitect);
        }

        try{
            chain.doFilter(request, response);
        }finally{
            try{
                RequestBubbleContext rbc=(RequestBubbleContext)request.getAttribute( RequestBubbleContext.BUBBLES_REQUEST_CONTEXT_KEY);
                if (rbc!=null)rbc.unbindIt();
            }catch(Throwable e){
                LoggingService.getDefaultLogger().logWarning(this, e);
            }
        }

        //System.out.println("============>After chain.doFilter");

    }
    public void destroy(){
        //System.out.println("============>Destroying filter");
        LoggingService.getDefaultLogger().logInfo(this, "BubbleFilter DESTROY!");
    }
    public WebArchitect getWebArchitect(){
        if ( this.webArchitect==null ){
            try{
                this.webArchitect =(WebArchitect)bubbleURLContextFactory.getBubbleContext();
            }catch(WDException e){
                LoggingService.getDefaultLogger().logWarning(this, "Bubble WebArchitect not found....");
            }
        }
        return this.webArchitect;
    }

    protected BubbleHttpServletRequest wrapRequest( HttpServletRequest request, WebArchitect webArchitect  ){
        return new BubbleHttpServletRequest((HttpServletRequest)request, webArchitect);
    }
    protected BubbleHttpServletResponse wrapResponse( HttpServletResponse response, WebArchitect webArchitect  ){
        return new BubbleHttpServletResponse((HttpServletResponse)response, webArchitect);
    }

}