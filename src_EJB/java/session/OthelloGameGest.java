/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.HashMap;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ophiran
 */
@Singleton
public class OthelloGameGest implements OthelloGameGestLocal {

    private HashMap<Long,OthelloGrid> games = new HashMap<>();
    
    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;
    
    @Override
    public void createGrid(Long gameId) {
        OthelloGrid newGrid = new OthelloGrid();
        //newGrid.grid[]
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
