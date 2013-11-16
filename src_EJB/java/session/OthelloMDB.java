/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Games;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    @Resource(lookup = "jms/javaee6/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    private Connection connection;
    
    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;
    
    @EJB
    public OthelloGameGestLocal othelloLoc;
    
    public OthelloMDB() {
    }
    
    @PostConstruct
    private void initConnection(){
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PreDestroy
    private void closeConnection() {
        try {
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("Message received");
        System.out.println(othelloLoc);
        try {
            String typeMsg = message.getStringProperty("Type");
            switch (typeMsg)
            {
                case "Create":
                    createGame(message);
                    break;
                case "Join":
                    joinGame(message);
                    break;
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createGame(Message message) throws JMSException{
        Long gameId = message.getLongProperty("GameId");
        System.out.println("Creating game " + gameId);
        Query query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        OthelloGrid grid = new OthelloGrid();
        if(game.getPlayer1() != null){
            grid.player1 = game.getPlayer1().getNickname();
        }
        else if(game.getPlayer2() != null){
            grid.player2 = game.getPlayer2().getNickname();
        }
        System.out.println(grid.player1 + "Vs" + grid.player2);
        //1 = white, 2 = black
        grid.grid[3][3] = 1;
        grid.grid[3][4] = 2;
        grid.grid[4][4] = 1;
        grid.grid[4][3] = 2;
        
        othelloLoc.addGrid(gameId, grid);
    }
    
    private void joinGame(Message message) throws JMSException{
        Long gameId = message.getLongProperty("GameId");
        System.out.println("Joining game " + gameId);
        OthelloGrid grid = othelloLoc.getGrid(gameId);
        Query query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        grid.player1 = game.getPlayer1().getNickname();
        grid.player2 = game.getPlayer2().getNickname();
        
        Random rand = new Random();
        float num = rand.nextFloat();
        if(num < 0.5) {
            grid.playerTurn = grid.player1;
        }
        else {
            grid.playerTurn = grid.player2;
        }
        
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        MessageProducer mp = session.createProducer(othelloTopic);
        ObjectMessage om = session.createObjectMessage();
        om.setLongProperty("GameId", gameId);
        om.setStringProperty("Type", "Grid");
        om.setObject(grid);
        mp.send(om);
        session.close();
        //publish the grid
    }
    
}
