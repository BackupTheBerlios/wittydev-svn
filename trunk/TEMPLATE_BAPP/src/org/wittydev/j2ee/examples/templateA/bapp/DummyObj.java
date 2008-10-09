package org.wittydev.j2ee.examples.templateA.bapp;

import java.io.Serializable;

import javax.jws.WebMethod;

public class DummyObj implements Serializable{
	String msg;
	public DummyObj(){
		this.msg="una stringa!!!";
	}
	
	public DummyObj(String msg){
		this.msg=msg;
	}
	@WebMethod
	public String getASting(){
		return msg;
	}
}
