/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Ophiran
 */
@Stateless
@LocalBean
public class OthelloAuth {

    public boolean createGame() {
        return false;
    }
    
    public boolean joinGame() {
        return false;
    }

    public void getListGames() {
    }

}
