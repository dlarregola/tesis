/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import InterfazUsusario.VentanaPrincipal;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Cristian
 */
public class EVAC3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        // TODO code application logic here
        try{
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        VentanaPrincipal ventanaPrincipal = VentanaPrincipal.getVentanaPrincipal();
        ventanaPrincipal.setExtendedState(6);
        ventanaPrincipal.setDefaultCloseOperation(3);
        ventanaPrincipal.setVisible(true);
    }
    
}
