package Oasis.Interfaces;

import Oasis.Datos.Actualizar;
import Oasis.Logica.Baja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class IProductoOff extends JDialog {

    public IProductoOff(Map<String, DefaultTableModel> modelsMap, JFrame parent, boolean bloquear) {
        super(parent, bloquear);
        JScrollPane scrollPane = new javax.swing.JScrollPane();
        String[] columns = {"ID", "Nombre", "Precio Compra", "Precio Venta", "Stock", "Tipo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable tablaProducto = new javax.swing.JTable(model);
        JButton btnAtras = new javax.swing.JButton();
        JButton btnBorrar = new javax.swing.JButton();

        Actualizar.modeloTabla(model, "producto_tienda", "stock = 0 AND tipo != 'CigarroSuelto'");

        scrollPane.setViewportView(tablaProducto);

        btnAtras.setText("<-");
        btnAtras.addActionListener(e -> {
            //Refrescamos las tablas de la interface principal
            Actualizar.modeloTabla(modelsMap.get("modelObjeto"),  "producto_tienda", "tipo != 'CigarroSuelto'");
            Actualizar.modeloTabla(modelsMap.get("modelPocosObjetos"),  "producto_tienda", " stock <= 20 AND tipo != 'CigarroSuelto'");

            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Refrescamos las tablas de la interface principal
                Actualizar.modeloTabla(modelsMap.get("modelObjeto"),  "producto_tienda", "tipo != 'CigarroSuelto'");
                Actualizar.modeloTabla(modelsMap.get("modelPocosObjetos"),  "producto_tienda", " stock <= 20 AND tipo != 'CigarroSuelto'");
            }
        });

        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(e -> {
            int row = tablaProducto.getSelectedRow();

            if (row > -1) {
                int column = tablaProducto.getColumnModel().getColumnIndex("ID");
                int valor;

                try {
                    valor = Integer.parseInt(tablaProducto.getValueAt(row, column).toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error interno en IProductoOff, llamar a un técnico", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                new Baja(model, "producto_tienda", "stock = 0 AND tipo != 'CigarroSuelto'", valor);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un objeto a borrar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(btnBorrar)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnAtras)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnBorrar)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        setUndecorated(true);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

