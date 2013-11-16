/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Local;

/**
 *
 * @author Ophiran
 */
@Local
public interface OthelloGameGestLocal {

    OthelloGrid getGrid(Long gameId);
    void addGrid(Long gameId,OthelloGrid grid);
}
