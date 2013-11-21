/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package othelloclient;

import graphicalInterfaces.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import session.GameListInfo;
import session.OthelloGrid;

/**
 *
 * @author Ophiran
 */
public class OthelloMainWindow extends javax.swing.JFrame implements ActionListener,MessageListener{

    //Graphic layout
    private MenuPanel menuPanel = new MenuPanel();
    private GameBoard gameBoard = new GameBoard();
    private ChatPanel chatPanel = new ChatPanel();
    private JPanel mainPanel = new JPanel();
    private GameListPanel gameListPanel = new GameListPanel();
    private GroupLayout layout;
    private GroupLayout layoutMainPanel;
    private CreateUserPanel userPanel = new CreateUserPanel();
    
    //Game infos
    public Long gameId;
    public GameStat currentStatus = GameStat.NOTCONNECTED; //Might be useless
    public String gamePassword = "";
    public boolean currentTurn = false;
    
    //GameList
    public Collection<GameListInfo> gameList;
    
    //JMS config
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private Topic topic;
    
    private OthelloGrid gameGrid = new OthelloGrid();
    /**
     * Creates new form OthelloMainWindow
     */
    public OthelloMainWindow(){
        initComponents();
        initLayout();
        initJMS();
        initListeners();
        switchMenuPanel();
        
    }
    
    private void initJMS() {
        try {
            Context ctx = new InitialContext();
            connectionFactory = (TopicConnectionFactory)ctx.lookup("jms/javaee6/ConnectionFactory");
            topic = (Topic)ctx.lookup("jms/javaee6/Topic");
            connection = connectionFactory.createConnection();
            session = connection.createSession();
        } catch (NamingException ex) {
            Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initLayout(){
        this.setResizable(false);
        
        menuPanel.setVisible(true);
        gameBoard.setVisible(true);
        chatPanel.setVisible(true);
        mainPanel.setVisible(true);
        
        
        layout = new GroupLayout(this.getContentPane());
        layoutMainPanel = new GroupLayout(mainPanel);
        
        //Layout for MainPanel
        mainPanel.setLayout(layoutMainPanel);
        layoutMainPanel.setAutoCreateGaps(true);
        layoutMainPanel.setAutoCreateContainerGaps(false);
        
        
        //Create horizontal group (MainPanel)
        GroupLayout.SequentialGroup hGroupMP = layoutMainPanel.createSequentialGroup();
        hGroupMP.addGroup(layoutMainPanel.createParallelGroup().addComponent(menuPanel));
        hGroupMP.addGroup(layoutMainPanel.createParallelGroup().addComponent(gameBoard));
        layoutMainPanel.setHorizontalGroup(hGroupMP);
        
        //Create vertical group (MainPanel)
        GroupLayout.SequentialGroup vGroupMP = layoutMainPanel.createSequentialGroup();
        vGroupMP.addGroup(layoutMainPanel.createParallelGroup(Alignment.LEADING).addComponent(menuPanel).addComponent(gameBoard));
        layoutMainPanel.setVerticalGroup(vGroupMP);
        
        //Layout for JFrame
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(false);
        
        //Create horizontal group (JFrame)
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(mainPanel).addComponent(chatPanel));
        layout.setHorizontalGroup(hGroup);
        
        //Create vertical group (JFrame)
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(mainPanel));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(chatPanel));
        layout.setVerticalGroup(vGroup);
        
        //Hide score
        menuPanel.getScorePanel().setVisible(false);
        menuPanel.wipeScore();
        pack();
        repaint();
    }
    
    private void switchGamePanel(){
        if(gameBoard.isVisible()) {
            gameList = Main.othelloAuth.getListGame();
            System.out.println("Size of list: " + gameList.size());
            gameListPanel.setList(gameList);
            gameBoard.setVisible(false);
            layoutMainPanel.replace(gameBoard, gameListPanel);
            gameListPanel.setVisible(true);
        }
        else {
            if(gameList != null){
                gameList.clear();
            }
            gameListPanel.setVisible(false);
            layoutMainPanel.replace(gameListPanel,gameBoard);
            gameBoard.setVisible(true);
        }
        pack();
        repaint();
    }
    
    private void switchMenuPanel(){
        if(menuPanel.isVisible()){
            menuPanel.setVisible(false);
            layoutMainPanel.replace(menuPanel,userPanel);
            userPanel.setVisible(true);
        }
        else {
            userPanel.setVisible(false);
            layoutMainPanel.replace(userPanel, menuPanel);
            menuPanel.setVisible(true);
        }
        pack();
        repaint();
    }
    
    private void initListeners(){
        //Create user panel listeners
        userPanel.getCreateButton().addActionListener(this);
        userPanel.getConnectButton().addActionListener(this);
        
        
        //Menu panel listeners
        menuPanel.getListButton().addActionListener(this);
        menuPanel.getCreateButton().addActionListener(this);
        menuPanel.getJoinButton().addActionListener(this);
        menuPanel.getQuitButton().addActionListener(this);
        gameBoard.getBoard().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardClicked(e);
            }
        });
    }
    
    public String getNickName(){
        return menuPanel.getNickNameField().getText();
    }
    
    private void boardClicked(MouseEvent e){
        if(currentTurn && (currentStatus == GameStat.PLAYER1 || currentStatus == GameStat.PLAYER2)){
            Point src = e.getPoint();
            int boardPosX = gameBoard.getBoardX(src.x);
            int boardPosY = gameBoard.getBoardY(src.y);
            System.out.println("X = " + boardPosX + " : Y = " + boardPosY);
            if(gameGrid.grid[boardPosX][boardPosY] == 0){
                try {
                    MessageProducer mp = session.createProducer(topic);
                    TextMessage tm = session.createTextMessage();
                    tm.setLongProperty("GameId", gameId);
                    tm.setStringProperty("Type", "Movement");
                    tm.setStringProperty("Player", getNickName());
                    tm.setIntProperty("X", boardPosX);
                    tm.setIntProperty("Y", boardPosY);
                    mp.send(tm);
                    mp.close();
                } catch (JMSException ex) {
                    Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        }
    }
    
    @Override
    public void onMessage(Message message) {
        OthelloGrid gameGridRcv;
        String type = "";
        try {
            type = message.getStringProperty("Type");
            System.out.println("Received message of type " + type);
            if(type.equals("Grid")) {
                gameGridRcv = (OthelloGrid)((ObjectMessage)message).getObject();
                updateGrid(gameGridRcv);
                updateScore();
            }
        } catch (JMSException ex) {
            Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateScore(){
        menuPanel.getPlayer1().setText(gameGrid.player1 + " (White)");
        menuPanel.getPlayer2().setText(gameGrid.player2 + " (Black)");
        menuPanel.getPlayer1Score().setText(String.valueOf(gameGrid.player1Score));
        menuPanel.getPlayer2Score().setText(String.valueOf(gameGrid.player2Score));
        menuPanel.getCurrentPlayer().setText(gameGrid.playerTurn);
    }
    
    private void updateGrid(OthelloGrid gridRcv){
        System.out.println("Updating grid...");
        for(int y = 0; y<8; y++){
            for(int x = 0; x<8; x++){
                if(gridRcv.grid[x][y] != gameGrid.grid[x][y]){
                    gameBoard.placePiece(x, y, gridRcv.grid[x][y]);
                    repaint();
                }
                gameGrid.grid[x][y] = gridRcv.grid[x][y];
            }
        }
        gameGrid.playerTurn = gridRcv.playerTurn;
        gameGrid.player1 = gridRcv.player1;
        gameGrid.player2 = gridRcv.player2;
        if(gameGrid.player1.equals(getNickName())){
            currentStatus = GameStat.PLAYER1;
        }
        else if(gameGrid.player2.equals(getNickName())){
            currentStatus = GameStat.PLAYER2;
        }
        else {
            currentStatus = GameStat.SPECTATOR;
        }
        
        gameGrid.player2 = gridRcv.player2;
        gameGrid.player1Score = gridRcv.player1Score;
        gameGrid.player2Score = gridRcv.player2Score;
        gameGrid.winner = gridRcv.winner;
        if(gameGrid.playerTurn.equals(getNickName())){
            currentTurn = true;
        }
        else {
            currentTurn = false;
        }
        
        if(currentStatus == GameStat.PLAYER1 && gameGrid.winner.equals(gameGrid.player1) || 
                currentStatus == GameStat.PLAYER2 && gameGrid.winner.equals(gameGrid.player2)){
            System.out.println("You have won!!");
            gameBoard.hasWon(true);
        }
        else if(!gameGrid.winner.isEmpty() && (currentStatus == GameStat.PLAYER1 || currentStatus == GameStat.PLAYER2)) {
            System.out.println("You have lost :(");
            gameBoard.hasWon(false);
        }
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(userPanel.getCreateButton())){
            boolean createUser;
            createUser = Main.othelloAuth.createUser(userPanel.getUsername(), userPanel.getPassword());
            if(createUser) {
                menuPanel.getNickNameField().setText(userPanel.getUsername());
                this.currentStatus = GameStat.CONNECTED;
                switchMenuPanel();
            }
            else {
                //show error to the user
            }
        }
        if(e.getSource().equals(userPanel.getConnectButton())){
            boolean connectedUser;
            connectedUser = Main.othelloAuth.connectUser(userPanel.getUsername(), userPanel.getPassword());
            if(connectedUser) {
                menuPanel.getNickNameField().setText(userPanel.getUsername());
                this.currentStatus = GameStat.CONNECTED;
                switchMenuPanel();
            }
            else {
                //show error to the user
            }
        }
        if(e.getSource().equals(menuPanel.getQuitButton())){
            this.dispose();
        }
        if(e.getSource().equals(menuPanel.getListButton())){
            switchGamePanel();
            //Main.othelloAuth.getListGame();
        }
        if(e.getSource().equals(menuPanel.getCreateButton())){
            new CreateGameDiag(this, rootPaneCheckingEnabled).setVisible(true);
            if(gameId != null){
                try {
                    messageConsumer = session.createConsumer(topic, "GameId = " + gameId/* + " AND Type = 'Grid'"*/);
                    messageConsumer.setMessageListener(this);
                    connection.start();
                    //Show score
                    menuPanel.getScorePanel().setVisible(true);
                } catch (JMSException ex) {
                    Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(e.getSource().equals(menuPanel.getJoinButton())){
            GameListInfo info = (GameListInfo)gameListPanel.getList().getSelectedValue();
            if(info != null && (!info.player1.isEmpty() || !info.player2.isEmpty())){
                new joinGameDiag(this, rootPaneCheckingEnabled).setVisible(true);
                gameId = Main.othelloAuth.joinGame(menuPanel.getNickNameField().getText(), info.gameId, gamePassword);
                if(gameId != null){
                    try {
                        messageConsumer = session.createConsumer(topic, "GameId = " + gameId/* + "' AND Type = 'Grid'"*/);
                        messageConsumer.setMessageListener(this);
                        connection.start();
                        //Show score
                        menuPanel.getScorePanel().setVisible(true);
                        Main.othelloAuth.refreshGrid(gameId);
                    } catch (JMSException ex) {
                        Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("Game Id Client : " + gameId);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(670, 650));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    


    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
