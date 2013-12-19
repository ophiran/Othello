/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package othelloclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import session.OthelloAuthRemote;
/**
 *
 * @author Ophiran
 */
public class Main {
    
    @EJB
    public static OthelloAuthRemote othelloAuth;
    
    public static OthelloMainWindow mainWindow;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println(othelloAuth);
        LookAndFeelInfo [] test = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo item : UIManager.getInstalledLookAndFeels()) {
            if("Nimbus".equals(item.getName())){
                try {
                    UIManager.setLookAndFeel(item.getClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        mainWindow = new OthelloMainWindow();
        mainWindow.setVisible(true);
       
    }
}
