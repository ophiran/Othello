/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package othelloclient;

import graphicalInterfaces.GameListPanel;
import graphicalInterfaces.ChatPanel;
import graphicalInterfaces.MenuPanel;
import graphicalInterfaces.CreateGameDiag;
import graphicalInterfaces.CreateUserPanel;
import graphicalInterfaces.GameBoard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import session.GameListInfo;

/**
 *
 * @author Ophiran
 */
public class OthelloMainWindow extends javax.swing.JFrame implements ActionListener{

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
    
    //GameList
    public Collection<GameListInfo> gameList;
    /**
     * Creates new form OthelloMainWindow
     */
    public OthelloMainWindow(){
        initComponents();
        initLayout();
        initListeners();
        /*
        try {
            tmpPic = ImageIO.read(this.getClass().getResourceAsStream("/ressources/board.jpg"));
            image = new JLabel(new ImageIcon(tmpPic));
        } catch (IOException ex) {
            Logger.getLogger(OthelloMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jPanelBoard.setLayout(new BorderLayout());
        jPanelBoard.add(image);
        jPanelBoard.repaint();*/
        switchMenuPanel();
        
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
        
        
        //this.getContentPane().add(menuPanel);
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
    }
    
    public String getNickName(){
        return menuPanel.getNickNameField().getText();
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
