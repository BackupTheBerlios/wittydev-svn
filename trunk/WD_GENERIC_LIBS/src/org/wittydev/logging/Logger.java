package org.wittydev.logging;

public interface Logger {
    public static final byte LEV_TRACE=63;
    public static final byte LEV_DEBUG=64;
    public static final byte LEV_INFO=65;
    public static final byte LEV_WARNING=66;
    public static final byte LEV_ERROR=67;
	
	public boolean isLogging(byte level);
	public boolean isLoggingTrace();
	public boolean isLoggingDebug();
	public boolean isLoggingInfo();
	public boolean isLoggingWarning();
	public boolean isLoggingError();


	public void setLogging(byte level, boolean enableLogging);
	public void setLoggingLevel(byte level);
	public void setLoggingTrace(boolean enableLogging);
	public void setLoggingDebug(boolean enableLogging);
	public void setLoggingInfo(boolean enableLogging);
	public void setLoggingWarning(boolean enableLogging);
	public void setLoggingError(boolean enableLogging);
	
	
	public void log(byte level, Object caller, String message);
	public void logTrace(Object caller, String message);
	public void logDebug(Object caller, String message);
	public void logInfo(Object caller, String message);
	public void logWarning(Object caller, String message);
	public void logError(Object caller, String message);
	
	
	public void log(byte level, Object caller, Throwable t);
	public void logTrace(Object caller, Throwable t);
	public void logDebug(Object caller, Throwable t);
	public void logInfo(Object caller, Throwable t);
	public void logWarning(Object caller, Throwable t);
	public void logError(Object caller, Throwable t);

	
	
}
