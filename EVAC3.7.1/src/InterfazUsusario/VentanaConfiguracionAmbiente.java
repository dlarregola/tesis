/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazUsusario;

import LogicaAplicacion.Mapa;
import LogicaAplicacion.Proyecto;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;

/**
 *
 * @author Cristian
 */
public class VentanaConfiguracionAmbiente extends javax.swing.JInternalFrame {

    /**
     * Creates new form VentanaConfiguracionAmbiente
     */
    ImageIcon[] ambienteImagenes;
    ImageIcon[] agenteImagenes;
    ImageIcon[] sensorImagenes;
    String[] ambienteStrings = {"Muro", "Obstaculo", "Salida", "Fuego", "Combustible"};
    //CAMBIADO PARA EL CURSO DE LA PLATA 01/09/2018
    //String[] agenteStrings = {"SMC", "SMTE", "SPDE", "PEE", "SML"};
    String[] agenteStrings = {"SMC", "SMTE", "SPDE"};
    String[] sensorStrings = {"Densidad"}; //agregar sensor
    private Mapa mapaAmbiente;
    private ButtonGroup grupoBotones = new ButtonGroup();
    private static VentanaConfiguracionAmbiente ventanaConfiguracionAmbiente;
    
    private VentanaConfiguracionAmbiente(){
        initComponents();
       
        ambienteImagenes = new ImageIcon[5];
        agenteImagenes = new ImageIcon[5];
        sensorImagenes=new ImageIcon[1];
          
        for (int i = 0; i < 5; i++) {
            comboAmbiente.addItem(new Integer(i));
            ambienteImagenes[i] = createImageIcon("/Recursos/" + ambienteStrings[i]+ ".gif");
            //ambienteImagenes[i] = new javax.swing.ImageIcon(getClass().getResource("/Recursos/" + ambienteStrings[i]+ ".gif"));
            //images[i] = createImageIcon("images/" + petStrings[i] + ".gif");
            if (ambienteImagenes[i] != null) {
                ambienteImagenes[i].setDescription("Sin Imagen");
            }
        }
        
        //CAMBIADO PARA EL CURSO DE LA PLATA 01/09/2018
        //for (int i = 0; i < 5; i++) {
        for (int i = 0; i < 3; i++) {
            comboAgentes.addItem(new Integer(i));
            agenteImagenes[i] = createImageIcon("/Recursos/" + agenteStrings[i]+ ".png");
            //agenteImagenes[i] = new javax.swing.ImageIcon(getClass().getResource("/Recursos/" + agenteStrings[i]+ ".png"));
            //images[i] = createImageIcon("images/" + petStrings[i] + ".gif");
            if (agenteImagenes[i] != null) {
                agenteImagenes[i].setDescription("Imagen No Disponible");
            }
        }
        
         comboSensores.addItem(new Integer(0));
         //sensorImagenes[0] = new javax.swing.ImageIcon(getClass().getResource("/Recursos/" + sensorStrings[0]+ ".png"));
         sensorImagenes[0] = createImageIcon("/Recursos/" + sensorStrings[0]+ ".png");
         if (sensorImagenes[0] != null) {
                sensorImagenes[0].setDescription("Imagen No Disponible");
         }
         
        ComboBoxRendererAgente rendererAgente= new ComboBoxRendererAgente();
        this.comboAgentes.setRenderer(rendererAgente);
        ComboBoxRendererAmbiente rendererAmbiente= new ComboBoxRendererAmbiente();
        this.comboAmbiente.setRenderer(rendererAmbiente);
        ComboBoxRendererSensor rendererSensor= new ComboBoxRendererSensor();
        this.comboSensores.setRenderer(rendererSensor);

        this.grupoBotones.add(this.botonAmbiente);
        this.grupoBotones.add(this.botonAgentes);
        this.grupoBotones.add(this.botonSensores);
        this.grupoBotones.add(this.botonBorrar);
        this.grupoBotones.add(this.botonInicio);
        this.botonAmbiente.setSelected(true);
        activarCombos();
        
        this.mapaAmbiente=new Mapa();
        this.mapaAmbiente.addMouseListener((MouseListener)new MEmapa(this, this.mapaAmbiente));
        this.mapaAmbiente.addMouseMotionListener((MouseMotionListener)new MEmapaMove(this, this.mapaAmbiente));
        this.scrollPane1.add(mapaAmbiente);
        
        this.comboVelMinima.setSelectedIndex(Proyecto.getProyecto().getVelocidadMinima()-1);
        this.comboVelMaxima.setSelectedIndex(Proyecto.getProyecto().getVelocidadMaxima()-1);
        this.textReaccionMinimo.setText(Integer.toString(Proyecto.getProyecto().getTiempoReaccionMinimo()));
        this.textReaccionMaximo.setText(Integer.toString(Proyecto.getProyecto().getTiempoReaccionMaximo()));
    }
    
    public static VentanaConfiguracionAmbiente getVentanaConfiguracionAmbiente(){
        if(ventanaConfiguracionAmbiente==null){
            ventanaConfiguracionAmbiente=new VentanaConfiguracionAmbiente();
        }
        return(ventanaConfiguracionAmbiente);
    }
    
    private void activarCombos(){
        if (this.botonAmbiente.isSelected()) {
            this.comboAmbiente.setEnabled(true);
            this.comboSensores.setEnabled(false);
            this.comboAgentes.setEnabled(false);
        }
        if (this.botonAgentes.isSelected()) {
            this.comboAmbiente.setEnabled(false);
            this.comboSensores.setEnabled(false);
            this.comboAgentes.setEnabled(true);
        }
        if (this.botonSensores.isSelected()) {
            this.comboAmbiente.setEnabled(false);
            this.comboSensores.setEnabled(true);
            this.comboAgentes.setEnabled(false);
        }
        if(this.botonBorrar.isSelected()){
            this.comboAmbiente.setEnabled(false);
            this.comboSensores.setEnabled(false);
            this.comboAgentes.setEnabled(false);
        }
    }
    
    public int botonSeleccionado() {
        if (this.botonAmbiente.isSelected()) {
            return this.comboAmbiente.getSelectedIndex();
        }
        if (this.botonAgentes.isSelected()) {
            return this.comboAgentes.getSelectedIndex()+5;
        }
        if (this.botonSensores.isSelected()) {
            return this.comboAgentes.getSelectedIndex()+10;
        }
        if (this.botonBorrar.isSelected()){
            return(11);
        }
        if(this.botonInicio.isSelected()){
            return(12);
        }
        return 0;
    }
    
     protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = VentanaConfiguracionAmbiente.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
                return null;
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
        botonAmbiente = new javax.swing.JToggleButton();
        comboAmbiente = new javax.swing.JComboBox();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        jSeparator5 = new javax.swing.JToolBar.Separator();
        botonAgentes = new javax.swing.JToggleButton();
        comboAgentes = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        comboVelMinima = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        comboVelMaxima = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        textReaccionMinimo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        textReaccionMaximo = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        jSeparator6 = new javax.swing.JToolBar.Separator();
        botonSensores = new javax.swing.JToggleButton();
        comboSensores = new javax.swing.JComboBox();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        jSeparator7 = new javax.swing.JToolBar.Separator();
        botonBorrar = new javax.swing.JToggleButton();
        botonInicio = new javax.swing.JToggleButton();
        botonValidar = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0));
        jSeparator3 = new javax.swing.JToolBar.Separator();
        botonVolver = new javax.swing.JButton();
        scrollPane1 = new java.awt.ScrollPane();

        setTitle("Dospisición del Proyecto");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/AC3D.png"))); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMinimumSize(new java.awt.Dimension(678, 31));

        botonAmbiente.setText("Ambiente");
        botonAmbiente.setFocusable(false);
        botonAmbiente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAmbiente.setMaximumSize(new java.awt.Dimension(65, 35));
        botonAmbiente.setMinimumSize(new java.awt.Dimension(65, 35));
        botonAmbiente.setPreferredSize(new java.awt.Dimension(65, 35));
        botonAmbiente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAmbiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAmbienteActionPerformed(evt);
            }
        });
        jToolBar1.add(botonAmbiente);

        jToolBar1.add(comboAmbiente);
        jToolBar1.add(filler2);
        jToolBar1.add(jSeparator5);

        botonAgentes.setText("Agentes");
        botonAgentes.setFocusable(false);
        botonAgentes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAgentes.setMaximumSize(new java.awt.Dimension(65, 35));
        botonAgentes.setMinimumSize(new java.awt.Dimension(65, 35));
        botonAgentes.setPreferredSize(new java.awt.Dimension(65, 35));
        botonAgentes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAgentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgentesActionPerformed(evt);
            }
        });
        jToolBar1.add(botonAgentes);

        comboAgentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboAgentesActionPerformed(evt);
            }
        });
        jToolBar1.add(comboAgentes);

        jLabel1.setText("   Velocidad   Mín:");
        jToolBar1.add(jLabel1);

        comboVelMinima.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0.4", "0.8", "1.2", "1.6", "2.0", "2.4", "2.8", "3.2", "3.6", "4.0" }));
        jToolBar1.add(comboVelMinima);

        jLabel2.setText("  Max:");
        jToolBar1.add(jLabel2);

        comboVelMaxima.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0.4", "0.8", "1.2", "1.6", "2.0", "2.4", "2.8", "3.2", "3.6", "4.0" }));
        jToolBar1.add(comboVelMaxima);

        jLabel3.setText("     Tpo. Reacción   Min: ");
        jToolBar1.add(jLabel3);

        textReaccionMinimo.setMaximumSize(new java.awt.Dimension(30, 20));
        textReaccionMinimo.setMinimumSize(new java.awt.Dimension(30, 20));
        textReaccionMinimo.setPreferredSize(new java.awt.Dimension(30, 20));
        jToolBar1.add(textReaccionMinimo);

        jLabel4.setText("   Max:");
        jToolBar1.add(jLabel4);

        textReaccionMaximo.setMaximumSize(new java.awt.Dimension(30, 20));
        textReaccionMaximo.setMinimumSize(new java.awt.Dimension(30, 20));
        textReaccionMaximo.setPreferredSize(new java.awt.Dimension(30, 20));
        jToolBar1.add(textReaccionMaximo);
        jToolBar1.add(filler6);
        jToolBar1.add(jSeparator6);

        botonSensores.setText("Sensores");
        botonSensores.setFocusable(false);
        botonSensores.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSensores.setMaximumSize(new java.awt.Dimension(65, 35));
        botonSensores.setMinimumSize(new java.awt.Dimension(65, 35));
        botonSensores.setPreferredSize(new java.awt.Dimension(65, 35));
        botonSensores.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSensores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSensoresActionPerformed(evt);
            }
        });
        jToolBar1.add(botonSensores);

        jToolBar1.add(comboSensores);
        jToolBar1.add(filler4);
        jToolBar1.add(jSeparator7);

        botonBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/borrar.png"))); // NOI18N
        botonBorrar.setText("Borrar");
        botonBorrar.setFocusable(false);
        botonBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonBorrar.setMaximumSize(new java.awt.Dimension(65, 35));
        botonBorrar.setMinimumSize(new java.awt.Dimension(65, 35));
        botonBorrar.setPreferredSize(new java.awt.Dimension(65, 35));
        botonBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });
        jToolBar1.add(botonBorrar);

        botonInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/inicio.png"))); // NOI18N
        botonInicio.setText("Inicio");
        botonInicio.setFocusable(false);
        botonInicio.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonInicio.setMaximumSize(new java.awt.Dimension(40, 35));
        botonInicio.setMinimumSize(new java.awt.Dimension(40, 35));
        botonInicio.setPreferredSize(new java.awt.Dimension(40, 35));
        botonInicio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(botonInicio);

        botonValidar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/validar.png"))); // NOI18N
        botonValidar.setText("Validar/Configurar");
        botonValidar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonValidar.setMaximumSize(new java.awt.Dimension(30, 35));
        botonValidar.setMinimumSize(new java.awt.Dimension(30, 35));
        botonValidar.setPreferredSize(new java.awt.Dimension(30, 35));
        botonValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonValidarActionPerformed(evt);
            }
        });
        jToolBar1.add(botonValidar);

        progressBar.setMinimumSize(new java.awt.Dimension(10, 14));
        progressBar.setOpaque(true);
        progressBar.setPreferredSize(new java.awt.Dimension(10, 14));
        jToolBar1.add(progressBar);
        jToolBar1.add(filler5);

        jSeparator3.setMinimumSize(new java.awt.Dimension(6, 3));
        jSeparator3.setPreferredSize(new java.awt.Dimension(6, 3));
        jToolBar1.add(jSeparator3);

        botonVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/volverChico.png"))); // NOI18N
        botonVolver.setText("Volver");
        botonVolver.setFocusable(false);
        botonVolver.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonVolver.setMaximumSize(new java.awt.Dimension(65, 35));
        botonVolver.setMinimumSize(new java.awt.Dimension(65, 35));
        botonVolver.setPreferredSize(new java.awt.Dimension(65, 35));
        botonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverActionPerformed(evt);
            }
        });
        jToolBar1.add(botonVolver);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)
            .addComponent(scrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverActionPerformed
        // TODO add your handling code here:
        VentanaPrincipal.getVentanaPrincipal().setEstadoMenuPrincipal(true,true,true);
        this.dispose();
        ventanaConfiguracionAmbiente=null;
    }//GEN-LAST:event_botonVolverActionPerformed

    private void botonAmbienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAmbienteActionPerformed
        // TODO add your handling code here:
        activarCombos();
    }//GEN-LAST:event_botonAmbienteActionPerformed

    private void botonAgentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgentesActionPerformed
        // TODO add your handling code here:
        activarCombos();
    }//GEN-LAST:event_botonAgentesActionPerformed

    private void botonSensoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSensoresActionPerformed
        // TODO add your handling code here:
        activarCombos();
    }//GEN-LAST:event_botonSensoresActionPerformed

    private void botonValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonValidarActionPerformed
        // TODO add your handling code here:   
        if(this.mapaAmbiente.validarMapa()){
            VentanaPrincipal.getVentanaPrincipal().getLabelEstado().setText(" ESTADO: < Validado >");
        }
        else{
            VentanaPrincipal.getVentanaPrincipal().getLabelEstado().setText(" ESTADO: <No validado>");
        }   
    }//GEN-LAST:event_botonValidarActionPerformed

    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarActionPerformed
        // TODO add your handling code here:
        activarCombos();
    }//GEN-LAST:event_botonBorrarActionPerformed

    private void comboAgentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboAgentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboAgentesActionPerformed

    public JProgressBar getProgressBar() {
        return progressBar;
    }
   
    class MEmapa extends MouseAdapter{
/*     */   private Mapa mapa;
/*     */   
/*     */  MEmapa(VentanaConfiguracionAmbiente paramVentanaConfiguracionAmbiente, Mapa mapa)
/*     */   {
/* 150 */     this.mapa = mapa;
/*     */   }

/*     */   public void mouseClicked(MouseEvent e){
/* 158 */     this.mapa.setRatonX(e.getX());
/* 159 */     this.mapa.setRatonY(e.getY());
/*     */     
/* 161 */     this.mapa.repaint();
/*     */   }
/*     */ }
    
    class MEmapaMove extends MouseMotionAdapter {
/*     */   private Mapa mapa;
/*     */   
/*     */   MEmapaMove(VentanaConfiguracionAmbiente paramVentanaConfiguracionAmbiente, Mapa mapa){
/* 168 */     this.mapa = mapa;
/*     */   }
/*     */   
/*     */   public void mouseDragged(MouseEvent e)
/*     */   {
/* 173 */     this.mapa.setRatonX(e.getX());
/* 174 */     this.mapa.setRatonY(e.getY());
/*     */     
/* 176 */     this.mapa.repaint();
/*     */   }
/*     */ }
    
    //-----------------------------------------------------------------------
    
     class ComboBoxRendererAmbiente extends JLabel implements ListCellRenderer {
         private Font uhOhFont;
         public ComboBoxRendererAmbiente() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            ImageIcon icon = ambienteImagenes[selectedIndex];
            String ambiente = ambienteStrings[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(ambiente);
                setFont(list.getFont());
            } else {
                setUhOhText(ambiente + " (Sin Imagen)",list.getFont());
            }
            return this;
        }
        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }

     class ComboBoxRendererAgente extends JLabel implements ListCellRenderer {
         private Font uhOhFont;
         public ComboBoxRendererAgente() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            ImageIcon icon = agenteImagenes[selectedIndex];
            String ambiente = agenteStrings[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(ambiente);
                setFont(list.getFont());
            } else {
                setUhOhText(ambiente + " (no image available)",list.getFont());
            }
            return this;
        }
        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
     
     class ComboBoxRendererSensor extends JLabel implements ListCellRenderer {
         private Font uhOhFont;
         public ComboBoxRendererSensor() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            ImageIcon icon = sensorImagenes[selectedIndex];
            String ambiente = sensorStrings[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(ambiente);
                setFont(list.getFont());
            } else {
                setUhOhText(ambiente + "(no image available)",list.getFont());
            }
            return this;
        }
        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
    
    //CRISTIAN 06/08/2018
     public int getVelMinima(){
         return this.comboVelMinima.getSelectedIndex();
     }
     
     public int getVelMaxima(){
         return this.comboVelMaxima.getSelectedIndex();
     }
     
     public String getTiempoReaccion(){
         return ( "[" + this.textReaccionMinimo.getText().trim() + "-" + this.textReaccionMaximo.getText().trim() + "]");
     }
     
    
    //-----------------------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton botonAgentes;
    private javax.swing.JToggleButton botonAmbiente;
    private javax.swing.JToggleButton botonBorrar;
    private javax.swing.JToggleButton botonInicio;
    private javax.swing.JToggleButton botonSensores;
    private javax.swing.JButton botonValidar;
    private javax.swing.JButton botonVolver;
    private javax.swing.JComboBox comboAgentes;
    private javax.swing.JComboBox comboAmbiente;
    private javax.swing.JComboBox comboSensores;
    private javax.swing.JComboBox<String> comboVelMaxima;
    private javax.swing.JComboBox<String> comboVelMinima;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JProgressBar progressBar;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JTextField textReaccionMaximo;
    private javax.swing.JTextField textReaccionMinimo;
    // End of variables declaration//GEN-END:variables
}
