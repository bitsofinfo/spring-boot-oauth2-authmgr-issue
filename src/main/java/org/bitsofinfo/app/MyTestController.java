package org.bitsofinfo.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyTestController {
	
	public MyTestController() {}

	@RequestMapping(value="/test-rest/test-method", 
					method=RequestMethod.GET)
	public String testMethod() {
		
		return "test-method was invoked";
    }


	
}
