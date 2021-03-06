/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazUsusario;

import LogicaAplicacion.Proyecto;
import LogicaAplicacion.ThreadSimulacion;
import LogicaAplicacion.Utilidades;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author Cristian
 */
public class VentanaSimulacion extends javax.swing.JInternalFrame {

    /**
     * Creates new form VentanaSimulacion
     */
    private static VentanaSimulacion ventanaSimulacion;
    private ThreadSimulacion threadSimulacion;
    
    private VentanaSimulacion() {
        initComponents();
    }
    
    public static VentanaSimulacion getVentanaSimulacion(){
        if (ventanaSimulacion==null){
            ventanaSimulacion=new VentanaSimulacion();
            ventanaSimulacion.textProyecto.setText(Proyecto.getProyecto().getNombreProyecto());
            ventanaSimulacion.textPersonas.setText(Integer.toString(Proyecto.getProyecto().getCantidadPersonas()));
            ventanaSimulacion.textFuego.setText(Double.toString(0.4 * Proyecto.getProyecto().getPropagacionFuego()) + " mts./seg."  );
            ventanaSimulacion.textHumo.setText(Double.toString(0.4 * Proyecto.getProyecto().getPropagacionHumo()) + " mts./seg.");
            ventanaSimulacion.textAnchoAlto.setText(Integer.toString(Proyecto.getProyecto().getTamañoy()) + " mts.   -   " + Integer.toString(Proyecto.getProyecto().getTamañox())+ " mts.");
            ventanaSimulacion.textEjecuciones.setText("30");
            ventanaSimulacion.textOcupadasLibres.setText("Ocupadas:" + Utilidades.celdasOcupadas() + "   Disponibles:" + Utilidades.celdasDisponibles() );
        }
        return(ventanaSimulacion);
    }
    
    public String getCantidadEjecuciones(){
        return(textEjecuciones.getText().trim());
    }
    
    public JProgressBar getBarraProgreso(){
        return(barraProgreso);
    }
    
    public JTextArea getParciales(){
        return(textParciales);
    }
    
    public JTextArea getFinales(){
        return(textFinales);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        botonIniciar = new javax.swing.JToggleButton();
        botonDetener = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        botonImprimir = new javax.swing.JButton();
        botonVolver = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        barraProgreso = new javax.swing.JProgressBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textAnchoAlto = new javax.swing.JTextField();
        textHumo = new javax.swing.JTextField();
        textFuego = new javax.swing.JTextField();
        textPersonas = new javax.swing.JTextField();
        textEjecuciones = new javax.swing.JTextField();
        textProyecto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textParciales = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textFinales = new javax.swing.JTextArea();
        textOcupadasLibres = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setTitle("Replicación de Ejecuciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/AC3D.png"))); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(131591, 32769));
        jToolBar1.setMinimumSize(new java.awt.Dimension(678, 31));
        jToolBar1.setPreferredSize(new java.awt.Dimension(678, 31));

        botonIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/playc.png"))); // NOI18N
        botonIniciar.setText("Iniciar");
        botonIniciar.setFocusable(false);
        botonIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonIniciar.setMaximumSize(new java.awt.Dimension(129, 29));
        botonIniciar.setMinimumSize(new java.awt.Dimension(129, 29));
        botonIniciar.setPreferredSize(new java.awt.Dimension(129, 29));
        botonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIniciarActionPerformed(evt);
            }
        });
        jToolBar1.add(botonIniciar);

        botonDetener.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/stopc.png"))); // NOI18N
        botonDetener.setText("Detener/Reiniciar");
        botonDetener.setFocusable(false);
        botonDetener.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonDetener.setMaximumSize(new java.awt.Dimension(129, 29));
        botonDetener.setMinimumSize(new java.awt.Dimension(129, 29));
        botonDetener.setPreferredSize(new java.awt.Dimension(129, 29));
        botonDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDetenerActionPerformed(evt);
            }
        });
        jToolBar1.add(botonDetener);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(jSeparator1);

        botonImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/imprimirChico.png"))); // NOI18N
        botonImprimir.setText("Imprimir");
        botonImprimir.setFocusable(false);
        botonImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonImprimir.setMaximumSize(new java.awt.Dimension(80, 29));
        botonImprimir.setMinimumSize(new java.awt.Dimension(80, 29));
        botonImprimir.setPreferredSize(new java.awt.Dimension(80, 29));
        jToolBar1.add(botonImprimir);

        botonVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/volverChico.png"))); // NOI18N
        botonVolver.setText("Volver");
        botonVolver.setFocusable(false);
        botonVolver.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonVolver.setMaximumSize(new java.awt.Dimension(80, 29));
        botonVolver.setMinimumSize(new java.awt.Dimension(80, 29));
        botonVolver.setPreferredSize(new java.awt.Dimension(80, 29));
        botonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverActionPerformed(evt);
            }
        });
        jToolBar1.add(botonVolver);
        jToolBar1.add(jSeparator3);
        jToolBar1.add(barraProgreso);
        jToolBar1.add(jSeparator4);

        jLabel1.setText("Proyecto");

        jLabel2.setText("Cantidad de Ejecuciones");

        jLabel3.setText("Cantidad de Personas");

        jLabel4.setText("Propagación Fuego");

        jLabel5.setText("Propagación Humo");

        jLabel6.setText("Alto y Ancho en Metros");

        textAnchoAlto.setEditable(false);

        textHumo.setEditable(false);

        textFuego.setEditable(false);

        textPersonas.setEditable(false);

        textProyecto.setEditable(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Resultados Parciales");

        textParciales.setColumns(20);
        textParciales.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        textParciales.setRows(5);
        jScrollPane1.setViewportView(textParciales);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Resultados Finales");

        textFinales.setColumns(20);
        textFinales.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        textFinales.setRows(5);
        jScrollPane2.setViewportView(textFinales);

        jLabel9.setText("Celdas Ocupadas/Libres");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textEjecuciones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                    .addComponent(textProyecto, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textPersonas)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel9))
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textOcupadasLibres, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(textHumo, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                    .addComponent(textFuego, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(textAnchoAlto, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel7)
                        .addGap(4, 4, 4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textEjecuciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(textPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(textFuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(textHumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(textAnchoAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textOcupadasLibres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverActionPerformed
        // TODO add your handling code here:
        if(VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion()!=null){
            try {
                VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion().parar();
                VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion().join();
                VentanaSimulacion.getVentanaSimulacion().setThreadSimulacion(null);
            } catch (InterruptedException ex) {
                Logger.getLogger(VentanaAnimacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        VentanaPrincipal.getVentanaPrincipal().setEstadoMenuPrincipal(true,true,true);
        this.dispose();
        ventanaSimulacion=null;
    }//GEN-LAST:event_botonVolverActionPerformed

    private void botonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIniciarActionPerformed
        // TODO add your handling code here:
        
        if(this.getThreadSimulacion()==null){
            ThreadSimulacion threadSimulacion = new ThreadSimulacion(1, 1);
            VentanaSimulacion.getVentanaSimulacion().setThreadSimulacion(threadSimulacion);
            VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion().start();
        }
    }//GEN-LAST:event_botonIniciarActionPerformed

    private void botonDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDetenerActionPerformed
        // TODO add your handling code here:
        if(VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion()!=null){
            try {
                VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion().parar();
                VentanaSimulacion.getVentanaSimulacion().getThreadSimulacion().join();
                VentanaSimulacion.getVentanaSimulacion().setThreadSimulacion(null);
            } catch (InterruptedException ex) {
                Logger.getLogger(VentanaAnimacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getParciales().setText("");
            this.getFinales().setText("");
            this.getBarraProgreso().setValue(0);
        }
    }//GEN-LAST:event_botonDetenerActionPerformed


    public ThreadSimulacion getThreadSimulacion() {
        return this.threadSimulacion;
    }

    public void setThreadSimulacion(ThreadSimulacion threadSimulacion) {
        this.threadSimulacion = threadSimulacion;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JButton botonDetener;
    private javax.swing.JButton botonImprimir;
    private javax.swing.JToggleButton botonIniciar;
    private javax.swing.JButton botonVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField textAnchoAlto;
    private javax.swing.JTextField textEjecuciones;
    private javax.swing.JTextArea textFinales;
    private javax.swing.JTextField textFuego;
    private javax.swing.JTextField textHumo;
    private javax.swing.JTextField textOcupadasLibres;
    private javax.swing.JTextArea textParciales;
    private javax.swing.JTextField textPersonas;
    private javax.swing.JTextField textProyecto;
    // End of variables declaration//GEN-END:variables
}
