/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.HashMap;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;

/**
 *
 * @author Ophiran
 */
@Singleton
public class OthelloGameGest implements OthelloGameGestLocal {

    private HashMap<Long,OthelloGrid> games = new HashMap<>();
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    

    @Override
    public OthelloGrid getGrid(Long gameId) {
        return games.get(gameId);
    }

    @Override
    public void addGrid(Long gameId,OthelloGrid grid) {
        games.put(gameId, grid);
    }

}
