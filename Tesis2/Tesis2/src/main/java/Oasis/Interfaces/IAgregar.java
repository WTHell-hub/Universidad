package Oasis.Interfaces;

import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;
import Oasis.Datos.Guardar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class IAgregar extends JDialog {

    public IAgregar(DefaultTableModel model, String dataBase, JDialog parent, boolean bloquear) {
        super(parent, bloquear);
        JLabel txtNombre = new JLabel();
        JTextField nombre = new JTextField();
        JLabel txtPCompra = new JLabel();
        JTextField pCompra = new JTextField();
        JLabel txtPVenta = new JLabel();
        JTextField pVenta = new JTextField();
        JLabel txtCantidad = new JLabel();
        JTextField cantidad = new JTextField();
        JButton btnCancelar = new JButton();
        JButton btnAceptar = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        txtNombre.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtNombre.setText("Nombre:");

        txtPCompra.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtPCompra.setText("Precio Comp:");

        txtPVenta.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtPVenta.setText("Precio Venta:");

        txtCantidad.setFont(new Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtCantidad.setText("Cantidad:");

        ActionListener agregar = e -> {

            try {
                String nombreAux = nombre.getText();
                int cantidadAux = parseInt(cantidad.getText().trim());
                double pCompraAux = parseDouble(pCompra.getText().trim());
                double pVentaAux = parseDouble(pVenta.getText().trim());

                if (!nombreAux.trim().isEmpty()) {
                    if (cantidadAux > 0 && pCompraAux > 0 && pVentaAux > 0) {
                        try {
                            Connection conn = GestorBD.getConn();

                            PreparedStatement stmt = conn.prepareStatement(
                                    "UPDATE "+dataBase+" SET pCompra = ?, pVenta = ?, stock = stock + ? WHERE nombre = ?"
                            );

                            stmt.setDouble(1, pCompraAux);
                            stmt.setDouble(2, pVentaAux);
                            stmt.setInt(3, cantidadAux);
                            stmt.setString(4, nombreAux);

                            int filasAfectadas = stmt.executeUpdate();

                            if (filasAfectadas == 0) {
                                List<Object> valores = Arrays.asList(nombreAux, pCompraAux, pVentaAux, cantidadAux);
                                Guardar.en(dataBase, valores, conn);
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        //ACTUALIZAR TABLA
                        Actualizar.modeloTabla(model, dataBase);

                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese números validos en los campos", "Warning", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un nombre valido", "Warning", JOptionPane.ERROR_MESSAGE);
                }

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese los datos adecuados en donde se le pide", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        };

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(e -> SwingUtilities.getWindowAncestor(btnCancelar).dispose());

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(agregar);
        cantidad.addActionListener(agregar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtNombre)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nombre, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(txtPVenta, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtPCompra, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(pCompra, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                                        .addComponent(pVenta)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtCantidad, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(2, 2, 2)
                                                                .addComponent(btnCancelar)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnAceptar))
                                                        .addComponent(cantidad, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNombre)
                                        .addComponent(nombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPCompra)
                                        .addComponent(pCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtPVenta)
                                        .addComponent(pVenta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtCantidad)
                                        .addComponent(cantidad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancelar)
                                        .addComponent(btnAceptar))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
