/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;

/**
 *
 * @author Ophiran
 */
@Stateless
public class OthelloAuth implements OthelloAuthRemote {

    @Override
    public boolean createGame() {
        return false;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public boolean joinGame() {
        return false;
    }

    @Override
    public void getListGame() {
    }

}
