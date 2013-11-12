/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

import java.io.Serializable;

/**
 *
 * @author ophiran
 */
public class GameListInfo implements Serializable{
    public Long gameId;
    public String gameName;
    public String player1;
    public String player2;
    public String startDate;

    public GameListInfo() {
        
    }
    
    public GameListInfo(Long gameId,String gameName,String player1,String player2,String startDate){
        this.gameId = gameId;
        this.gameName = gameName;
        this.player1 = player1;
        this.player2 = player2;
        this.startDate = startDate;
    }
    
    @Override
    public String toString() {
        return gameName;
    }
    
    
}
