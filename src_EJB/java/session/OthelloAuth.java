/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Games;
import entities.Gamestate;
import entities.Players;
import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ophiran
 */
@Singleton
@Remote(OthelloAuthRemote.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class OthelloAuth implements OthelloAuthRemote {

    private Vector<Games> games;
    
    private Long gameIdGen = 0L;
    
    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;

    @Resource(lookup = "jms/javaee6/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/javaee6/Topic")
    private Topic topic;
    
    @Override
    public Collection<GameListInfo> getListGame() {
        Query query = em.createNamedQuery("Games.findAll");
        Vector<GameListInfo> gameList = new Vector<>();
        for(Games g : (List<Games>)query.getResultList()){
            gameList.add(g.getGameInfo());
        }
        return gameList;
    }

    @Override
    public Collection<GameListInfo> getListGame(GameStatesEnum state) {
        Query query = em.createNamedQuery("Gamestate.findByName").setParameter("name", state.getName());
        Gamestate gState = (Gamestate)query.getSingleResult();
        query = em.createNamedQuery("Games.findByState").setParameter("state", gState);
        Vector<GameListInfo> gameList = new Vector<>();
        for(Games g : (List<Games>)query.getResultList()){
            gameList.add(g.getGameInfo());
        }
        return gameList;
    }
    
    //Have to pay attention to not have two players with the same names (with the concurrency managment + synchronized)
    @Override
    public synchronized Long createGame(String nickname, String gameName, String password) {
        Players player;
        Query query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", nickname);
        if(query.getResultList().isEmpty()){
            player = new Players();
            player.setNickname(nickname);
            em.persist(player);
        }
        else {
            player = ((Players)query.getSingleResult());
        }
        
        Date startDate = new Date(System.currentTimeMillis());
        gameIdGen++;
        Games game = new Games(gameIdGen,gameName);
        query = em.createNamedQuery("Gamestate.findByName").setParameter("name", GameStatesEnum.OPEN.getName());
        game.setStartDate(startDate);
        game.setPassword(password);
        game.setPlayer1(player);
        game.setState((Gamestate)query.getSingleResult());
        em.persist(game);
        System.out.println("Returning game id: " + gameIdGen);
        
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            TextMessage tm = session.createTextMessage();
            tm.setLongProperty("GameId", gameIdGen);
            tm.setStringProperty("Type", "Create");
            tm.setLongProperty("Player", player.getId());
            session.createProducer(topic).send(tm);
        } catch (JMSException ex) {
            Logger.getLogger(OthelloAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gameIdGen;
    }

    @Override
    public synchronized Long joinGame(String nickname, Long gameId, String password) {
        Query query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        if(game.getPassword() == null || game.getPassword().equals(password)) {
            query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", nickname);
            Players player = (Players)query.getSingleResult();
            query = em.createNamedQuery("Gamestate.findByName").setParameter("name", GameStatesEnum.STARTED.getName());
            
            if(game.getPlayer2() == null) {
                game.setPlayer2(player);
                game.setState((Gamestate)query.getSingleResult());
            }
            else if(game.getPlayer1() == null) {
                game.setPlayer1(player);
                game.setState((Gamestate)query.getSingleResult());
            }
            else {
                return null;
            }
            
            Connection connection;
            try {
                connection = connectionFactory.createConnection();
                Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                TextMessage tm = session.createTextMessage();
                tm.setLongProperty("GameId", gameIdGen);
                tm.setStringProperty("Type", "Join");
                tm.setLongProperty("Player", player.getId());
                session.createProducer(topic).send(tm);
            } catch (JMSException ex) {
                Logger.getLogger(OthelloAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
            em.persist(game);
        }
        System.out.println("Returniong game id: " + game.getId());
        return game.getId();
    }

    @Override
    public boolean createUser(String nickname, String password) {
        System.out.println("Creating user " + nickname);
        Query query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", nickname);
        if(query.getResultList().isEmpty()){
            Players player = new Players();
            player.setNickname(nickname);
            player.setPassword(password);
            em.persist(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean connectUser(String nickname, String password) {
        System.out.println("Connection with user " + nickname);
        Query query = em.createNamedQuery("Players.findByAuthentication").setParameter("nickname", nickname).setParameter("password", password);
        if(query.getResultList().isEmpty()) return false;
        return true;
    }
    
    

}
