package Oasis.Interfaces;

import Oasis.Datos.Actualizar;
import Oasis.Logica.Baja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class IAdministrarDatos extends JDialog {

    public IAdministrarDatos(String dataBase, String lugar, JFrame parent, boolean bloquear) {
        super(parent, bloquear);
        JLabel titulo = new JLabel();
        JButton btnAgregar = new JButton();
        JButton btnBaja = new JButton();
        JButton btnCerrar = new JButton();
        JButton btnModificar = new JButton();
        JScrollPane jScrollPane1 = new JScrollPane();
        DefaultTableModel model = getDefaultTableModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        JTable tabla = new JTable(model);
        tabla.setRowSorter(sorter);

        //ACTUALIZAR TABLA
        Actualizar.modeloTabla(model, dataBase);

        titulo.setFont(new Font("Arial Black", Font.BOLD | Font.ITALIC, 24));
        titulo.setText(lugar);

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(e -> {
            if (titulo.getText().equals("Tienda")) {
                JOptionPane.showMessageDialog(this,
                        "No puedes agregar productos directamente desde la tienda",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);

            } else {
                new IAgregar(model, dataBase, this, true);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();

            //Esta condicional es para asegurarnos de que halla algún elemento seleccionado en la tabla, y es > -1 pq si no se selecciona ningún elemento el getSelectCount da como resultado -1
            if (fila > -1) {
                int column = tabla.getColumnModel().getColumnIndex("ID");
                Object id = tabla.getValueAt(fila, column);

                new Modificar(id, model, dataBase, this, true);
            }
        });

        btnBaja.setText("Baja");
        btnBaja.addActionListener(e -> {
            int fila = tabla.getSelectedRow();

            //Esta condicional es para asegurarnos de que halla algún elemento seleccionado en la tabla, y es > -1 pq si no se selecciona ningún elemento el getSelectCount da como resultado -1
            if (fila > -1) {
                int columnNombre = tabla.getColumnModel().getColumnIndex("Nombre");
                String nombre = String.valueOf(tabla.getValueAt(fila, columnNombre));

                if (!nombre.trim().startsWith("Cigarro")) {

                    int columnID = tabla.getColumnModel().getColumnIndex("ID");
                    Object id = tabla.getValueAt(fila, columnID);

                    int opcion = JOptionPane.showConfirmDialog(
                            null,
                            "Desea borrar "+nombre+"?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) {
                        new Baja(model, dataBase, (int) id);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Los cigarros sueltos no se pueden borrar", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAgregar)
                                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnModificar)
                                        .addComponent(btnBaja)
                                        .addComponent(btnCerrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(titulo)
                                                .addGap(53, 53, 53)
                                                .addComponent(btnAgregar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnModificar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnBaja)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCerrar)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dimension);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static DefaultTableModel getDefaultTableModel() {
        String[] columnas = {"ID", "Nombre", "Precio Comp", "Precio Venta" , "Stock"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Definimos el tipo de cada columna
                return switch (columnIndex) {
                    case 0 -> String.class; // ID
                    case 1 -> String.class;  // Nombre
                    case 2 -> Double.class; // Cantidad
                    case 3 -> Double.class;
                    case 4 -> Integer.class;
                    default -> Object.class;
                };
            }
        };
        return model;
    }
}

