/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.Collection;
import javax.ejb.Remote;

/**
 *
 * @author Ophiran
 */
@Remote
public interface OthelloAuthRemote {

    Long createGame(String nickname,String gameName,String password);

    Long joinGame(String nickname,Long gameId,String password);

    Collection<GameListInfo> getListGame();
    
    Collection<GameListInfo> getListGame(GameStatesEnum state);
    
    boolean createUser(String nickname,String password);
    
    boolean connectUser(String nickname,String password);
    
    void refreshGrid(Long gameId);
}
