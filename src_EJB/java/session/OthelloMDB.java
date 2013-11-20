/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Colors;
import entities.Games;
import entities.Gamestate;
import entities.Moves;
import entities.Players;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ophiran
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/javaee6/Topic"),
    //@ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "durable"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/javaee6/Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/javaee6/Topic")
})
public class OthelloMDB implements MessageListener {
    
    @Resource(lookup = "jms/javaee6/Topic")
    private Destination othelloTopic;
    @Resource(lookup = "jms/javaee6/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    private Connection connection;
    
    @PersistenceContext(unitName = "OthelloEAR-ejbPU")
    private EntityManager em;
    
    @EJB
    public OthelloGameGestLocal othelloLoc;
    
    public OthelloMDB() {
    }
    
    @PostConstruct
    private void initConnection(){
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PreDestroy
    private void closeConnection() {
        try {
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("Message received");
        System.out.println(othelloLoc);
        try {
            String typeMsg = message.getStringProperty("Type");
            switch (typeMsg)
            {
                case "Create":
                    createGame(message);
                    break;
                case "Join":
                    joinGame(message);
                    break;
                case "Movement":
                    playMove(message);
                    break;
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createGame(Message message) throws JMSException{
        Long gameId = message.getLongProperty("GameId");
        System.out.println("Creating game " + gameId);
        Query query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        OthelloGrid grid = new OthelloGrid();
        if(game.getPlayer1() != null){
            grid.player1 = game.getPlayer1().getNickname();
        }
        else if(game.getPlayer2() != null){
            grid.player2 = game.getPlayer2().getNickname();
        }
        System.out.println(grid.player1 + "Vs" + grid.player2);
        //1 = white, 2 = black
        grid.grid[3][3] = 1;
        grid.grid[3][4] = 2;
        grid.grid[4][4] = 1;
        grid.grid[4][3] = 2;
        
        grid.player1Score = 2;
        grid.player2Score = 2;
        
        grid.movNum = 0;
        grid.winner = "";
        othelloLoc.addGrid(gameId, grid);
    }
    
    private void joinGame(Message message) throws JMSException{
        Long gameId = message.getLongProperty("GameId");
        System.out.println("Joining game " + gameId);
        OthelloGrid grid = othelloLoc.getGrid(gameId);
        Query query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
        Games game = (Games)query.getSingleResult();
        
        grid.player1 = game.getPlayer1().getNickname();
        grid.player2 = game.getPlayer2().getNickname();
        
        Random rand = new Random();
        float num = rand.nextFloat();
        if(num < 0.5) {
            grid.playerTurn = grid.player1;
        }
        else {
            grid.playerTurn = grid.player2;
        }
        
        sendGrid(gameId, grid);
        //publish the grid
    }
    
    private void sendGrid(Long gameId,OthelloGrid grid) throws JMSException{
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        MessageProducer mp = session.createProducer(othelloTopic);
        ObjectMessage om = session.createObjectMessage();
        om.setLongProperty("GameId", gameId);
        om.setStringProperty("Type", "Grid");
        om.setObject(grid);
        mp.send(om);
        session.close();
        mp.close();
    }
    
    private void playMove(Message message) throws JMSException {
        Long gameId = message.getLongProperty("GameId");
        String playerTurn = message.getStringProperty("Player");
        int xCoord = message.getIntProperty("X");
        int yCoord = message.getIntProperty("Y");
        OthelloGrid grid = othelloLoc.getGrid(gameId);
        boolean isValid = false; //verify if the placement was valid
        int playerColor;
        
        if(grid.playerTurn.equals(playerTurn)){
            if(grid.player1.equals(playerTurn)){
                playerColor = 1;
            }
            else {
                playerColor = 2;
            }
            
            if(checkLine(xCoord, yCoord, -1, -1, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, -1, 0, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, -1, 1, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, 0, -1, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, 0, 1, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, 1, -1, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, 1, 0, playerColor, grid, true)){
                isValid = true;
            }
            if(checkLine(xCoord, yCoord, 1, 1, playerColor, grid, true)){
                isValid = true;
            }
            
            if(isValid){
                System.out.println("Putting a " + playerColor + " at position (" + xCoord +"," + yCoord + ")");
                grid.grid[xCoord][yCoord] = playerColor;
                grid.movNum++;
                //To modify and add an option to save
                
                Moves move = new Moves(gameId, grid.movNum);
                Query query = em.createNamedQuery("Players.findByNickname").setParameter("nickname", playerTurn);
                move.setPosX(xCoord);
                move.setPosY(yCoord);
                move.setPlayer(((Players)query.getSingleResult()).getId());
                query = em.createNamedQuery("Games.findById").setParameter("id", gameId);
                Games curGame = (Games)query.getSingleResult();
                move.setGames(curGame);
                query = em.createNamedQuery("Colors.findByColorId").setParameter("colorId", playerColor);
                move.setColor((Colors)query.getSingleResult());
                move.setDateMove(new Date(System.currentTimeMillis()));
                em.persist(move);
                
                if(playerTurn.equals(grid.player1)){
                    grid.player1Score++;
                }
                else {
                    grid.player2Score++;
                }
                
                if(playerTurn.equals(grid.player1) && hasValidPlacement(grid, 2)){
                    grid.playerTurn = grid.player2;
                }
                else if(hasValidPlacement(grid, 1)){
                    grid.playerTurn = grid.player1;
                }
                else {
                    if(grid.player1Score>grid.player2Score) {
                        grid.winner = grid.player1;
                    }
                    else{
                        grid.winner = grid.player2;
                    }
                    othelloLoc.removeGrid(gameId);
                    query = em.createNamedQuery("Gamestate.findByName").setParameter("name", GameStatesEnum.FINISHED);
                    curGame.setState((Gamestate)query.getSingleResult());
                    em.persist(curGame);
                }
            }
            sendGrid(gameId, grid);
        }
        
    }
    
    
    /*
    direction   (incX,incY) = { (-1,-1);(0,-1);(1,-1)
                                (-1,0);(*,*);(1,0)
                                (-1,1);(0,1);(1,1)
    */
    private boolean checkLine(int xCoord,int yCoord,int incX,int incY,int playerColor,OthelloGrid grid,boolean flip){
        boolean isValid = false;
        boolean stopped = false;
        boolean found = false;
        int n = 1;
        
        if((xCoord+incX)<8 && (xCoord+incX)>=0 && (yCoord+incY)<8 && (yCoord+incY)>=0 
                && grid.grid[xCoord+incX][yCoord+incY] == playerColor){
            return isValid;
        }
        
        while((xCoord+incX*n)<8 && (xCoord+incX*n)>=0 && (yCoord+incY*n)<8 && (yCoord+incY*n)>=0 && !stopped && !found){
            if(grid.grid[xCoord+incX*n][yCoord+incY*n] == playerColor && n > 1){
                found = true;
                isValid = true;
            }
            else if(grid.grid[xCoord+incX*n][yCoord+incY*n] == 0) {
                stopped = true;
            }
            n++;
        }
        if(flip && found){
            for(int i = n-1;i!=0;i--){
                if(grid.grid[xCoord+incX*i][yCoord+incY*i] != playerColor){
                    if(grid.grid[xCoord+incX*i][yCoord+incY*i] != 0){
                        if(grid.playerTurn.equals(grid.player1)){
                            grid.player1Score++;
                            grid.player2Score--;
                        }
                        else {
                            grid.player2Score++;
                            grid.player1Score--;
                        }
                    }
                }
                grid.grid[xCoord+incX*i][yCoord+incY*i] = playerColor;
            }
        }
        
        return isValid;
    }
    
    
    private boolean hasValidPlacement(OthelloGrid grid,int playerColor) {
        int searchColor;
        if(playerColor == 2) {
            searchColor = 1;
        }
        else {
            searchColor = 2;
        }
        
        for(int y = 0;y<8;y++){
            for(int x = 0;x<8;x++){
                if(grid.grid[x][y] == searchColor){
                    if(x < 7 && grid.grid[x+1][y] == 0) {
                        if(checkLine(x+1, y, -1, -1, playerColor, grid, false)||
                                checkLine(x+1, y, 0, -1, playerColor, grid, false)||
                                checkLine(x+1, y, 1, -1, playerColor, grid, false)||
                                checkLine(x+1, y, -1, 0, playerColor, grid, false)||
                                checkLine(x+1, y, 1, 0, playerColor, grid, false)||
                                checkLine(x+1, y, -1, 1, playerColor, grid, false)||
                                checkLine(x+1, y, 0, 1, playerColor, grid, false)||
                                checkLine(x+1, y, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(x > 0 && grid.grid[x-1][y] == 0) {
                        if(checkLine(x-1, y, -1, -1, playerColor, grid, false)||
                                checkLine(x-1, y, 0, -1, playerColor, grid, false)||
                                checkLine(x-1, y, 1, -1, playerColor, grid, false)||
                                checkLine(x-1, y, -1, 0, playerColor, grid, false)||
                                checkLine(x-1, y, 1, 0, playerColor, grid, false)||
                                checkLine(x-1, y, -1, 1, playerColor, grid, false)||
                                checkLine(x-1, y, 0, 1, playerColor, grid, false)||
                                checkLine(x-1, y, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(x < 7 && y<7 && grid.grid[x+1][y+1] == 0) {
                        if(checkLine(x+1, y+1, -1, -1, playerColor, grid, false)||
                                checkLine(x+1, y+1, 0, -1, playerColor, grid, false)||
                                checkLine(x+1, y+1, 1, -1, playerColor, grid, false)||
                                checkLine(x+1, y+1, -1, 0, playerColor, grid, false)||
                                checkLine(x+1, y+1, 1, 0, playerColor, grid, false)||
                                checkLine(x+1, y+1, -1, 1, playerColor, grid, false)||
                                checkLine(x+1, y+1, 0, 1, playerColor, grid, false)||
                                checkLine(x+1, y+1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(x < 7 && y>0 && grid.grid[x+1][y-1] == 0) {
                        if(checkLine(x+1, y-1, -1, -1, playerColor, grid, false)||
                                checkLine(x+1, y-1, 0, -1, playerColor, grid, false)||
                                checkLine(x+1, y-1, 1, -1, playerColor, grid, false)||
                                checkLine(x+1, y-1, -1, 0, playerColor, grid, false)||
                                checkLine(x+1, y-1, 1, 0, playerColor, grid, false)||
                                checkLine(x+1, y-1, -1, 1, playerColor, grid, false)||
                                checkLine(x+1, y-1, 0, 1, playerColor, grid, false)||
                                checkLine(x+1, y-1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(x > 0 && y<7 && grid.grid[x-1][y+1] == 0) {
                        if(checkLine(x-1, y+1, -1, -1, playerColor, grid, false)||
                                checkLine(x-1, y+1, 0, -1, playerColor, grid, false)||
                                checkLine(x-1, y+1, 1, -1, playerColor, grid, false)||
                                checkLine(x-1, y+1, -1, 0, playerColor, grid, false)||
                                checkLine(x-1, y+1, 1, 0, playerColor, grid, false)||
                                checkLine(x-1, y+1, -1, 1, playerColor, grid, false)||
                                checkLine(x-1, y+1, 0, 1, playerColor, grid, false)||
                                checkLine(x-1, y+1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(x > 0 && y>0 && grid.grid[x-1][y-1] == 0) {
                        if(checkLine(x-1, y, -1, -1, playerColor, grid, false)||
                                checkLine(x-1, y-1, 0, -1, playerColor, grid, false)||
                                checkLine(x-1, y-1, 1, -1, playerColor, grid, false)||
                                checkLine(x-1, y-1, -1, 0, playerColor, grid, false)||
                                checkLine(x-1, y-1, 1, 0, playerColor, grid, false)||
                                checkLine(x-1, y-1, -1, 1, playerColor, grid, false)||
                                checkLine(x-1, y-1, 0, 1, playerColor, grid, false)||
                                checkLine(x-1, y-1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(y<7 && grid.grid[x][y+1] == 0) {
                        if(checkLine(x, y+1, -1, -1, playerColor, grid, false)||
                                checkLine(x, y+1, 0, -1, playerColor, grid, false)||
                                checkLine(x, y+1, 1, -1, playerColor, grid, false)||
                                checkLine(x, y+1, -1, 0, playerColor, grid, false)||
                                checkLine(x, y+1, 1, 0, playerColor, grid, false)||
                                checkLine(x, y+1, -1, 1, playerColor, grid, false)||
                                checkLine(x, y+1, 0, 1, playerColor, grid, false)||
                                checkLine(x, y+1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                    if(y>0 && grid.grid[x][y-1] == 0) {
                        if(checkLine(x, y-1, -1, -1, playerColor, grid, false)||
                                checkLine(x, y-1, 0, -1, playerColor, grid, false)||
                                checkLine(x, y-1, 1, -1, playerColor, grid, false)||
                                checkLine(x, y-1, -1, 0, playerColor, grid, false)||
                                checkLine(x, y-1, 1, 0, playerColor, grid, false)||
                                checkLine(x, y-1, -1, 1, playerColor, grid, false)||
                                checkLine(x, y-1, 0, 1, playerColor, grid, false)||
                                checkLine(x, y-1, 1, 1, playerColor, grid, false)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
}
