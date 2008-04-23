package org.wittydev.core;

public class WDException extends Exception {
	public static int CODE_UNDEFINED_EXCEPTION=0;
	public static int CODE_SYSTEM_EXCEPTION=1;
	public static int CODE_APPPLICATION_EXCEPTION=2;
	int code=CODE_UNDEFINED_EXCEPTION;

	public WDException(int code, String message, Throwable exception) {
		super(message, exception);
		this.code=code;
	}
	public WDException(int code, String message) {
		super(message);
		this.code=code;
	}
	
	public WDException(String message, Throwable exception) {
		super(message, exception);
	}
	
	public WDException(String message) {
		super(message);
	}

	public int getCode(){
		return code;
	}
	
}
