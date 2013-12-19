/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Ophiran
 */
public class handler implements SOAPHandler<SOAPMessageContext>{

    @Override
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        //if(context.getMessage().)
        boolean outbound = (boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if(outbound){
            Logger.getGlobal().info("Outbound: "+ GregorianCalendar.getInstance() + " " + context.getMessage().toString());
        }
        else {
            Logger.getGlobal().info("Inbound: "+ GregorianCalendar.getInstance() + " " + context.getMessage().toString());
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
    
}
