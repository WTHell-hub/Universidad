package Oasis.Interfaces;

import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;
import Oasis.Modelos.ProductoBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static java.lang.Double.sum;
import static java.lang.Integer.parseInt;

public class Modificar extends javax.swing.JDialog {

    public Modificar(Object id, DefaultTableModel model, String dataBase, JDialog parent, boolean bloquear) {
        super(parent, bloquear);
        JButton btnAtras = new javax.swing.JButton();
        JLabel txtModificar = new javax.swing.JLabel();
        JLabel txtNombre = new javax.swing.JLabel();
        JTextField nombre = new javax.swing.JTextField();
        JLabel txtPCompra = new javax.swing.JLabel();
        JTextField pCompra = new javax.swing.JTextField();
        JLabel txtPVenta = new javax.swing.JLabel();
        JTextField pVenta = new javax.swing.JTextField();
        JLabel txtStock = new javax.swing.JLabel();
        JTextField stock = new javax.swing.JTextField();
        JButton btnAceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        //Se llaman los datos del producto elegido por el usuario
        ArrayList<ProductoBase> productoAModificar = new ArrayList<>();

        Actualizar.lista(productoAModificar, dataBase, "id = " + id);
        String nombreOld = productoAModificar.get(0).getName();
        double pCompraOld = productoAModificar.get(0).getPrecioC();
        double pVentaOld = productoAModificar.get(0).getPrecioV();
        int stockOld = (int) productoAModificar.get(0).getStock();

        ActionListener modificar = (e -> {
            String nombreNew;
            double pCompraNew;
            double pVentaNew;
            int stockNew;

            try {
                nombreNew = nombre.getText();
                pCompraNew = parseDouble(pCompra.getText());
                pVentaNew = parseDouble(pVenta.getText());
                stockNew = parseInt(stock.getText());

                try {
                    Connection conn = GestorBD.getConn();

                    try (PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE "+dataBase+" SET nombre = ?, pCompra = ?, pVenta = ?, stock = ? WHERE id = ?"
                    )) {
                        stmt.setString(1, nombreNew);
                        stmt.setDouble(2, pCompraNew);
                        stmt.setDouble(3, pVentaNew);
                        stmt.setInt(4, stockNew);
                        stmt.setInt(5, (int) id);

                        stmt.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                dispose();
                Actualizar.modeloTabla(model, dataBase);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Datos incorrectos", "Warning", JOptionPane.ERROR_MESSAGE);

                nombre.setText(nombreOld);
                pCompra.setText(String.valueOf(pCompraOld));
                pVenta.setText(String.valueOf(pVentaOld));
                stock.setText(String.valueOf(stockOld));
            }
        });

        btnAtras.setText("<-");
        btnAtras.addActionListener(e -> dispose());

        txtModificar.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24)); // NOI18N
        txtModificar.setText("Modificar");

        txtNombre.setText("Nombre: ");
        nombre.setText(nombreOld);
        nombre.addActionListener(modificar);

        txtPCompra.setText("Precio de Compra:");
        pCompra.setText(String.valueOf(pCompraOld));
        pCompra.addActionListener(modificar);

        txtPVenta.setText("Precio de Venta:");
        pVenta.setText(String.valueOf(pVentaOld));
        pVenta.addActionListener(modificar);

        txtStock.setText("Stock:");
        stock.setText(String.valueOf(stockOld));
        stock.addActionListener(modificar);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(modificar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnAceptar)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(47, 47, 47)
                                                        .addComponent(txtModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(31, 31, 31)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtNombre)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtPCompra)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(pCompra))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtPVenta)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(pVenta))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtStock)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAtras)
                                        .addComponent(txtModificar))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNombre)
                                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPCompra)
                                        .addComponent(pCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPVenta)
                                        .addComponent(pVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtStock)
                                        .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(btnAceptar)
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}