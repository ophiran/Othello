/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Remote;

/**
 *
 * @author Ophiran
 */
@Remote
public interface OthelloAuthRemote {

    boolean createGame();

    boolean joinGame();

    void getListGame();
    
}
