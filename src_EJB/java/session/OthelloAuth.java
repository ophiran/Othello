/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Games;
import entities.Players;
import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
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

    @Override
    public Collection<GameListInfo> getListGame() {
        Query query = em.createNamedQuery("Games.findAll");
        Vector<GameListInfo> gameList = new Vector<>();
        for(Games g : (List<Games>)query.getResultList()){
            gameList.add(g.getGameInfo());
        }
        return gameList;
    }

    //Have to pay attention to not have two players with the same names
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
        game.setStartDate(startDate);
        game.setPassword(password);//need to add the gamename attribute
        game.setPlayer1(player);
        em.persist(game);
        return gameIdGen;
    }

    @Override
    public synchronized Long joinGame(String nickname, Long gameId, String password) {
        Query query;
        
        query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", nickname);
        Players player = (Players)query.getSingleResult();
        query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        if(game.getPassword() == null || game.getPassword().equals(password)) {
            if(game.getPlayer2() == null) {
                game.setPlayer2(player);
            }
            else if(game.getPlayer1() == null) {
                game.setPlayer1(player);
            }
            else {
                return null;
            }
        }
        em.persist(game);
        return game.getId();
    }

    @Override
    public boolean createUser(String nickname, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
