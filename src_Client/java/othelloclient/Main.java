/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package othelloclient;

import javax.ejb.EJB;
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
        mainWindow = new OthelloMainWindow();
        mainWindow.setVisible(true);
       
    }
}
