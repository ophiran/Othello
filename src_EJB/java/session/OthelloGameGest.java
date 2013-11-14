/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.Vector;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ophiran
 */
@Singleton
public class OthelloGameGest implements OthelloGameGestLocal {

    private Vector<OthelloGrid> games = new Vector<>();
    
    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;
    
    @Override
    public void createGrid(Long gameId) {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
