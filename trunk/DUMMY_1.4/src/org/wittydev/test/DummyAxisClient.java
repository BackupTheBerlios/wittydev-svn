package org.wittydev.test;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.Options;

import javax.xml.rpc.ParameterMode;

public class DummyAxisClient {
	public static void main(String [] args) throws Exception {
	    String endpoint="http://127.0.0.1:8080/TEMPLATE_WAPP/ServerInfoRPC?wsdl";
	
	
	    Service  service = new Service();
	    Call     call    = (Call) service.createCall();
	    
	
	    String method="getServerTime";
	    
	    call.setTargetEndpointAddress(new java.net.URL(endpoint));
	    
	    call.setOperationName( method );
	    /*call.addParameter("op1", XMLType.XSD_INT, ParameterMode.PARAM_MODE_IN);
	    call.addParameter("op2", XMLType.XSD_INT, ParameterMode.PARAM_MODE_IN);*/
	    call.setReturnType(XMLType.XSD_LONG);
	
	    //Long ret = (Long) call.invoke( new Object [] { i1, i2 });
	    Long ret = (Long) call.invoke( new Object [] {});
	    //Long ret = (Long) call.invoke();
	
	    System.out.println("Got result : " + ret);
	}

}
