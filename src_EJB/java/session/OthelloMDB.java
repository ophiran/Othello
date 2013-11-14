/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Ophiran
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/javaee6/Topic"),
    //@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/javaee6/Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/javaee6/Topic")
})
public class OthelloMDB implements MessageListener {
    
    @Resource(lookup = "jms/javaee6/Topic")
    private Destination othelloTopic;
    
    public OthelloMDB() {
    }
    
    @Override
    public void onMessage(Message message) {
        
    }
    
    public void createNewGameTopic(Long gameId){
        System.out.println("Value of topic " + othelloTopic);
    }
}
