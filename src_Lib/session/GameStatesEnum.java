/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

/**
 *
 * @author ophiran
 */
public enum GameStatesEnum {
    OPEN("OPEN"),
    STARTED("STARTED"),
    SAVED("SAVED"),
    FINISHED("FINISHED");
    
    String name;
    
    GameStatesEnum(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return getName(); //To change body of generated methods, choose Tools | Templates.
    }
}
