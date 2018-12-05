/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazUsusario;

import LogicaAplicacion.Batch;
import LogicaAplicacion.Proyecto;
import LogicaAplicacion.ThreadSimulacion;
import java.awt.FileDialog;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristian
 */
public class VentanaBatch extends javax.swing.JInternalFrame {

    /**
     * Creates new form VentanaBatch
     */
    
    private static VentanaBatch ventanaBatch;
    private ThreadSimulacion threadSimulacion;
    private  LinkedList listaBatch;
    private  Batch batchActual;
    private  int filaSeleccionada;
    private boolean pausado=false;
    
    private VentanaBatch(){
        this.listaBatch = new LinkedList();
        this.filaSeleccionada=-1;
        initComponents();
    }
    
    public static VentanaBatch getVentanaBatch(){
        if(ventanaBatch==null){
            ventanaBatch=new VentanaBatch();
        }
        return(ventanaBatch);
    }

    public Batch getBatchActual() {
        return batchActual;
    }

    public void setBatchActual(Batch batchActual) {
        this.batchActual = batchActual;
    }

    public ThreadSimulacion getThreadSimulacion() {
        if(this.threadSimulacion==null){
            return (null);
        }
        return this.threadSimulacion;
    }
        
    public void setThreadSimulacion(ThreadSimulacion threadSimulacion) {
        this.threadSimulacion = threadSimulacion;
    }
    
    private void setSelectedRow(int fila) {
        this.filaSeleccionada = fila;
    }

    private int getSelectedRow() {
        return this.filaSeleccionada;
    }
    
    public String getCantidadEjecuciones(){
        return(textEjecuciones.getText().trim());
    }
    
    public JProgressBar getBarraProgreso(){
        return(barraProgreso);
    }
    private void eliminar (){
        listaBatch.remove(this.getSelectedRow());
    }
    
    private void mostrar(){
        Batch itemBatch;
        while (((DefaultTableModel)this.tablaDatos.getModel()).getRowCount() > 0) {
                ((DefaultTableModel)this.tablaDatos.getModel()).removeRow(0);
            }
            
            ListIterator iterador = listaBatch.listIterator();
            while (iterador.hasNext()) {
                itemBatch = (Batch)iterador.next();
                Object[] fila = new Object[7];
                fila[0] = itemBatch.getRutaProyecto();
                fila[1] = itemBatch.getEvacuadosCaidos();
                fila[2] = itemBatch.getResultadoProyecto();
                fila[3] = itemBatch.getIntervaloEvacuacion99();
                fila[4] = itemBatch.getTiempoMedioEvacuacionPersona();
                fila[5] = itemBatch.getEspacioMedioRecorrido();
                fila[6] = itemBatch.getDescripcionPuertas();
                ((DefaultTableModel)this.tablaDatos.getModel()).addRow(fila);
            }
    }

    
    private void ejecutar(){
        Batch itemBatch;
        ListIterator iterador = listaBatch.listIterator();
        this.getBarraProgreso().setMinimum(0);
        this.getBarraProgreso().setMaximum(listaBatch.size());
        while (iterador.hasNext()) {
            itemBatch = (Batch)iterador.next();
            batchActual=itemBatch;
            Proyecto.setNullProyectoActual();
            Proyecto.setProyectoBatch(itemBatch.getRutaProyecto());
            ThreadSimulacion threadBatch = new ThreadSimulacion(30, 2);
            VentanaBatch.getVentanaBatch().setThreadSimulacion(threadBatch);
            VentanaBatch.getVentanaBatch().getThreadSimulacion().start();
            try {
                VentanaBatch.getVentanaBatch().getThreadSimulacion().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(VentanaBatch.class.getName()).log(Level.SEVERE, null, ex);
            }

            mostrar();
            System.out.println("proyecto: ");
            this.getBarraProgreso().setValue(this.getBarraProgreso().getValue()+1);
            this.paintAll(this.getGraphics());
            System.out.println("Termine proyecto: " + itemBatch.getRutaProyecto() + " - " + itemBatch.getResultadoProyecto());
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

        jToolBar1 = new javax.swing.JToolBar();
        botonAnadirProyecto = new javax.swing.JButton();
        botonEliminarProyecto = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        textEjecuciones = new javax.swing.JTextField();
        botonComenzarEjecucion = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        barraProgreso = new javax.swing.JProgressBar();
        botonVolver = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDatos = new javax.swing.JTable();

        setTitle("Ejecución Por Lotes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/AC3D.png"))); // NOI18N

        jToolBar1.setRollover(true);

        botonAnadirProyecto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevoChico.png"))); // NOI18N
        botonAnadirProyecto.setText("Añadir Proyecto");
        botonAnadirProyecto.setFocusable(false);
        botonAnadirProyecto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonAnadirProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAnadirProyectoActionPerformed(evt);
            }
        });
        jToolBar1.add(botonAnadirProyecto);

        botonEliminarProyecto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarChico.png"))); // NOI18N
        botonEliminarProyecto.setText("Eliminar Proyecto");
        botonEliminarProyecto.setFocusable(false);
        botonEliminarProyecto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonEliminarProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarProyectoActionPerformed(evt);
            }
        });
        jToolBar1.add(botonEliminarProyecto);
        jToolBar1.add(jSeparator2);

        jLabel1.setText("Replicas  ");
        jToolBar1.add(jLabel1);

        textEjecuciones.setMaximumSize(new java.awt.Dimension(60, 20));
        textEjecuciones.setMinimumSize(new java.awt.Dimension(60, 20));
        textEjecuciones.setPreferredSize(new java.awt.Dimension(60, 20));
        jToolBar1.add(textEjecuciones);

        botonComenzarEjecucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/fechaHastaChico.png"))); // NOI18N
        botonComenzarEjecucion.setText("Comenzar Ejecución");
        botonComenzarEjecucion.setFocusable(false);
        botonComenzarEjecucion.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonComenzarEjecucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonComenzarEjecucionActionPerformed(evt);
            }
        });
        jToolBar1.add(botonComenzarEjecucion);
        jToolBar1.add(jSeparator1);
        jToolBar1.add(barraProgreso);

        botonVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/volverChico.png"))); // NOI18N
        botonVolver.setText("Volver");
        botonVolver.setFocusable(false);
        botonVolver.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonVolver.setMaximumSize(new java.awt.Dimension(80, 29));
        botonVolver.setMinimumSize(new java.awt.Dimension(80, 29));
        botonVolver.setPreferredSize(new java.awt.Dimension(80, 29));
        botonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverActionPerformed(evt);
            }
        });
        jToolBar1.add(botonVolver);

        tablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ruta", "Evacuados/Caidos", "Media de Medias TTE", "TTE Intervalo 99% ", "TMEPP Intervalo 99%", "EMR Intervalo 99%", "Información Puertas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablaDatosMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDatos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAnadirProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAnadirProyectoActionPerformed
        // TODO add your handling code here:
         JFrame abrir = new JFrame("ABRIR");
         FileDialog dialogoAbrir = new FileDialog(abrir, "Abrir Archivo", 0);
         dialogoAbrir.setFile("*.evc");
         dialogoAbrir.setVisible(true);
         if (dialogoAbrir.getDirectory() != null && dialogoAbrir.getFile() != null) {
             Batch batch = new Batch();
             batch.setRutaProyecto(dialogoAbrir.getDirectory() + dialogoAbrir.getFile());
             batch.setResultadoProyecto("");
             batch.setIntervaloEvacuacion99("");
             listaBatch.add(batch);
             mostrar();
        }
    }//GEN-LAST:event_botonAnadirProyectoActionPerformed

    private void botonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverActionPerformed
        // TODO add your handling code here:
        if(VentanaAnimacion.getVentanaAnimacion().getThreadSimulacion()!=null){
            try {
                VentanaBatch.getVentanaBatch().getThreadSimulacion().parar();
                VentanaBatch.getVentanaBatch().getThreadSimulacion().join();
                VentanaBatch.getVentanaBatch().setThreadSimulacion(null);
            } catch (InterruptedException ex) {
                Logger.getLogger(VentanaAnimacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        VentanaPrincipal.getVentanaPrincipal().setEstadoMenuPrincipal(true,true,true);
        this.dispose();
        ventanaBatch=null;
    }//GEN-LAST:event_botonVolverActionPerformed

    private void botonEliminarProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarProyectoActionPerformed
        // TODO add your handling code here:
        if(getSelectedRow()>-1){
            eliminar();
        }
        else{
            JOptionPane.showMessageDialog(this,"Debe Seleccionar un Registro");
        }
        setSelectedRow(-1);
        mostrar();
    }//GEN-LAST:event_botonEliminarProyectoActionPerformed

    private void tablaDatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDatosMouseReleased
        // TODO add your handling code here:
        if (!this.tablaDatos.getSelectionModel().isSelectionEmpty()) {
            int seleccion = this.tablaDatos.getSelectionModel().getLeadSelectionIndex();
            this.setSelectedRow(seleccion);
        }
    }//GEN-LAST:event_tablaDatosMouseReleased

    private void botonComenzarEjecucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonComenzarEjecucionActionPerformed
        // TODO add your handling code here:
        ejecutar();
    }//GEN-LAST:event_botonComenzarEjecucionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JButton botonAnadirProyecto;
    private javax.swing.JButton botonComenzarEjecucion;
    private javax.swing.JButton botonEliminarProyecto;
    private javax.swing.JButton botonVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tablaDatos;
    private javax.swing.JTextField textEjecuciones;
    // End of variables declaration//GEN-END:variables
}
