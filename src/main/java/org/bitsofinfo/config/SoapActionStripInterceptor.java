package org.bitsofinfo.config;

import java.util.List;
import java.util.Map;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.SoapActionInInterceptor;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class SoapActionStripInterceptor extends AbstractSoapInterceptor {

	public SoapActionStripInterceptor() {
        super(Phase.READ);
        addBefore(SoapActionInInterceptor.class.getName());
    }

    @Override
	public void handleMessage(SoapMessage message) throws Fault {
 
        if (message.getVersion() instanceof Soap11) {
            Map<String, List<String>> headers = CastUtils.cast((Map)message.get(Message.PROTOCOL_HEADERS));
            headers.remove("SOAPAction");
            headers.remove("soapaction");
            headers.remove("Soapaction");
        }
    }

}
