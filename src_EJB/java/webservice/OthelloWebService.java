/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice;

import entities.Games;
import entities.Players;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ophiran
 */
@WebService(serviceName = "OthelloWebService")
@HandlerChain(file = "handlerChain.xml")
@Stateless()
public class OthelloWebService {

    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "infoPlayer")
    public PlayerProfile getInfoPlayer(@WebParam(name = "username",header = true,mode = WebParam.Mode.IN) String username) {
        
        Query query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", username);
        Players player = (Players)query.getSingleResult();
        query = em.createNamedQuery("Games.findWithPlayer").setParameter("player", player);
        List<Games> games = query.getResultList();
        
        PlayerProfile profile = new PlayerProfile();
        for(Games game : games){
            String gInfo = game.getGameName() + " " + game.getStartDate();
            gInfo = gInfo.replaceAll(" ", "&nbsp;");
            profile.gameList.add(gInfo);
        }
        System.out.println("Players win " + player.getWins());
        profile.wins = player.getWins();
        profile.loss = player.getLost();
        profile.username = player.getNickname();
        
        return profile;
    }
}
