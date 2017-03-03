package org.bitsofinfo.app;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(name="TestSoapService",
			serviceName="TestSoapService",
			targetNamespace="http://TestSoapService.services.bitsofinfo.com/")
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL)
public class TestSoapService  {

	private final static Logger logger = LoggerFactory.getLogger(TestSoapService.class);
	
	public TestSoapService() {}

	@WebMethod
	public String someTestMethod(@WebParam(name="param1") String param1) {	
		System.out.println("someTestMethod() PARAM1: " + param1);
		return param1;
	}
	

}