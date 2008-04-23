package org.wittydev.logging.impl;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.wittydev.logging.Logger;


public class SimpleLogger implements Logger{

    private static final String EMPTY_STRING="";

    SimpleDateFormat sDFormat= new SimpleDateFormat( "[dd/MM kk:mm:ss]");
    String separator= " ";

    /*boolean loggingTrace=false;
    boolean loggingDebug=false;
    boolean loggingInfo=true;
    boolean loggingWarning=true;
    boolean loggingError=true;*/
    
    private PrintStream tracePS;
    private PrintStream debugPS;
    private PrintStream infoPS;
    private PrintStream warningPS;
    private PrintStream errorPS;
    
    boolean loggingLevels[]= new boolean[128]; 
    {
    	setLoggingLevel(LEV_INFO);
    }
    public void setDateFormat(SimpleDateFormat sDFormat) {
            this.sDFormat=sDFormat;
    }
    public SimpleDateFormat getDateFormat() {
            return sDFormat;
    }

    public void logTrace(Object logApp, String message){
        if (!isLoggingTrace())return;
        log( LEV_TRACE,logApp, message );
    }
    public void logTrace(Object logApp, Throwable pThrowable){
        if (!isLoggingDebug())return;
        log( LEV_DEBUG,logApp, pThrowable );
    }
    
    public void logDebug(Object logApp, String message){
        if (!isLoggingDebug())return;
        log( LEV_DEBUG,logApp, message );
    }
    public void logDebug(Object logApp, Throwable pThrowable){
        if (!isLoggingDebug())return;
        log( LEV_DEBUG,logApp, pThrowable );
    }

    public void logInfo(Object logApp, String message){
        if (!isLoggingInfo()) return;
        log( LEV_INFO,logApp, message );
    }
	public void logInfo(Object logApp, Throwable pThrowable){
        if (!isLoggingInfo()) return;
        log( LEV_INFO, logApp, pThrowable );
    }
	public void logWarning(Object logApp, String message){
        if (!isLoggingWarning())return;
        log( LEV_WARNING, logApp, message );
    }
	public void logWarning(Object logApp, Throwable pThrowable){
        if (!isLoggingWarning())return;
        log( LEV_WARNING, logApp, pThrowable );
    }
	public void logError(Object logApp, String message){
        if (!isLoggingError())return;
        log( LEV_ERROR, logApp, message );
    }
	public void logError(Object logApp, Throwable pThrowable){
        if (!isLoggingError())return;
        log( LEV_ERROR, logApp, pThrowable );
    }


    public boolean isLogging(byte level){
        return this.loggingLevels[level];
    }
    public boolean isLoggingTrace(){
        return this.loggingLevels[LEV_TRACE];
    }
    public boolean isLoggingDebug(){
    	return this.loggingLevels[LEV_DEBUG];
    }
	public boolean isLoggingInfo(){
		return this.loggingLevels[LEV_INFO];
    }
	public boolean isLoggingWarning(){
		return this.loggingLevels[LEV_WARNING];
    }
	public boolean isLoggingError(){
		return this.loggingLevels[LEV_ERROR];
    }


	public void setLogging(byte level, boolean enableLogging){
		this.loggingLevels[level]= enableLogging;
	}
	public void setLoggingLevel(byte level){
		for (int i=0; i<loggingLevels.length;i++){
			if (i<=level)
				loggingLevels[i]=true;
			else
				loggingLevels[i]=false;
		}
	}
	public void setLoggingTrace( boolean logging ){
    	this.loggingLevels[LEV_TRACE]=logging ;
    }
    public void setLoggingDebug( boolean logging ){
    	this.loggingLevels[LEV_DEBUG]=logging ;
    }
	public void setLoggingInfo( boolean logging ){
		this.loggingLevels[LEV_INFO]=logging ;
    }
	public void setLoggingWarning( boolean logging ){
		this.loggingLevels[LEV_WARNING]=logging ;
    }
	public void setLoggingError( boolean logging ){
		this.loggingLevels[LEV_ERROR]=logging ;
    }
    /////////////////////////////////
    public void setTraceOutputStream(OutputStream os){
        if (os ==null) new NullPointerException ();
        if (os instanceof PrintStream)
            tracePS=(PrintStream)os;
        else
        	tracePS=new PrintStream(os);
    }
    public void setDebugOutputStream(OutputStream os){
        if (os ==null) new NullPointerException ();
        if (os instanceof PrintStream)
            debugPS=(PrintStream)os;
        else
            debugPS=new PrintStream(os);
    }
    public void setInfoOutputStream(OutputStream os){
        if (os ==null) new NullPointerException ();
        if (os instanceof PrintStream)
            infoPS=(PrintStream)os;
        else
            infoPS=new PrintStream(os);
    }
    public void setWarningOutputStream(OutputStream os){
        if (os ==null) new NullPointerException ();
        if (os instanceof PrintStream)
            warningPS=(PrintStream)os;
        else
            warningPS=new PrintStream(os);
    }
    public void setErrorOutputStream(OutputStream os){
        if (os ==null) new NullPointerException ();
        if (os instanceof PrintStream)
            errorPS=(PrintStream)os;
        else
            errorPS=new PrintStream(os);
    }


    //////////////////////////////////

    public void log (byte level, Object logApp, String message){
        log(level, logApp, message, null);
    }
    public void log (byte level, Object logApp, Throwable throwable){
        log(level, logApp, EMPTY_STRING,throwable);
    }

    
	
    protected void log (byte level, Object logApp, String message, Throwable throwable){
        PrintStream ps;
        String initMessage;
        switch (level){
            case LEV_ERROR:
                initMessage=getErrorInit();
                ps=(errorPS!=null)?errorPS:System.err;
                break;
            case LEV_WARNING:
                initMessage=getWarningInit();
                ps=(warningPS!=null)?warningPS:System.err;
                break;
            case LEV_INFO:
                initMessage=getInfoInit();
                ps=(infoPS!=null)?infoPS:System.out;
                break;
            case LEV_DEBUG:
                initMessage=getDebugInit();
                ps=(debugPS!=null)?debugPS:System.out;
                break;
            case LEV_TRACE:
                initMessage=getTraceInit();
                ps=(tracePS!=null)?tracePS:System.out;
                break;
            default:
            	return;
        }
        String className;
        if (logApp==null)
            className="null";
        else if (logApp instanceof Class)
            className="class "+((Class)logApp).getName()+"";
        else{
            className=logApp.getClass().getName();
        }

        ps.println( initMessage+ separator +getDate()
                    + separator+"["+className+"]: "+message );
        if (throwable!=null)throwable.printStackTrace();
    }


    protected String getDate(){
        if (sDFormat==null)
            return new Date().toString();
        else
            return sDFormat.format(new Date());
    }
    protected String getTraceInit(){
        return "##TRACE##";
    }
    protected String getDebugInit(){
        return "##DEBUG##";
    }

    protected String getInfoInit(){
        return "##INFO##";
    }
    protected String getWarningInit(){
        return "##WARNING##";
    }

    protected String getErrorInit(){
        return "##ERROR##";
    }
	
	
}
