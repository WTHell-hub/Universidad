package Oasis.Interfaces.Cuadre;

import javax.swing.*;
import java.awt.*;

public class CuadreView extends JDialog {

    private JTable tableProductos;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JButton btnModificar;
    private JLabel txtDinero;
    private JTextField dinero;

    public CuadreView(JFrame parent, boolean bloquear) {
        super(parent, bloquear);

        JScrollPane jScrollPane1 = new JScrollPane();
        tableProductos = new javax.swing.JTable();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        txtDinero = new JLabel();
        dinero = new JTextField();

        //Agg la tabla al Scroll para que se le pueda dar Scroll y se vea en la interface
        jScrollPane1.setViewportView(tableProductos);

        btnAceptar.setText("Aceptar");

        btnCancelar.setText("Cancelar");

        btnModificar.setText("Modificar");

        txtDinero.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 14));
        txtDinero.setText("Dinero Total:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnAceptar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificar)
                                .addGap(119, 119, 119)
                                .addComponent(txtDinero, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dinero, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(275, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 785, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAceptar)
                                        .addComponent(btnCancelar)
                                        .addComponent(btnModificar)
                                        .addComponent(txtDinero)
                                        .addComponent(dinero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setUndecorated(true);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public JTable getTableProductos() {
        return tableProductos;
    }

    public void setTableProductos(JTable tableProductos) {
        this.tableProductos = tableProductos;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public void setBtnAceptar(JButton btnAceptar) {
        this.btnAceptar = btnAceptar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public JLabel getTxtDinero() {
        return txtDinero;
    }

    public void setTxtDinero(JLabel txtDinero) {
        this.txtDinero = txtDinero;
    }

    public JTextField getDinero() {
        return dinero;
    }

    public void setDinero(JTextField dinero) {
        this.dinero = dinero;
    }
}