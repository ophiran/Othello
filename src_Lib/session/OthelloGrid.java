/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.io.Serializable;

/**
 *
 * @author Ophiran
 */
public class OthelloGrid implements Serializable{
    public String player1;
    public String player2;
    public String playerTurn;
    public int[][] grid = new int[8][8];
}
