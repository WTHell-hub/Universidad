package Oasis.Interfaces;

import Oasis.Interfaces.Cuadre.CuadreControllerHistorial;
import Oasis.Interfaces.Cuadre.CuadreModel;
import Oasis.Interfaces.Cuadre.CuadreView;
import Oasis.Datos.Actualizar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class Historial extends JDialog {

    public Historial(Map<String, DefaultTableModel> models, JFrame parent, boolean bloquear) {
        super(parent, bloquear);
        JScrollPane scrollCuadre = new javax.swing.JScrollPane();
        String[] columnasCuadre = {"ID", "Fecha", "Dinero Total", "Faltante"};
        DefaultTableModel modelCuadre = new DefaultTableModel(columnasCuadre, 0) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        JTable tablaCuadres = new javax.swing.JTable(modelCuadre);
        JButton atras = new javax.swing.JButton();
        JButton btnRectificar = new JButton();
        JScrollPane scrollProductos = new javax.swing.JScrollPane();
        String[] columnasProductos = {"ID", "Nombre", "Cuadre Anterior", "Cuadre Posterior", "Cantidad Vendida"};
        DefaultTableModel modelProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        JTable tablaProductos = new javax.swing.JTable(modelProductos);

        tablaProductos.removeColumn(tablaProductos.getColumnModel().getColumn(0));

        scrollCuadre.setViewportView(tablaCuadres);

        Actualizar.modeloTabla(modelCuadre, "cuadre");

        tablaCuadres.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tablaCuadres.getSelectedRow() != -1) {
                    int fila = tablaCuadres.getSelectedRow();
                    int columna = tablaCuadres.getColumnModel().getColumnIndex("Fecha");
                    Object fecha = tablaCuadres.getValueAt(fila,columna);
                    Actualizar.modeloTabla(modelProductos, "producto", "fecha = "+"'"+fecha+"'");
                }
            }
        });

        atras.setText("<-");
        atras.addActionListener(e -> dispose());

        btnRectificar.setText("Rectificar");
        btnRectificar.addActionListener(e -> {
            int row = tablaCuadres.getSelectedRow();

            if (row > -1) {
                int id = (int) tablaCuadres.getValueAt(row, 0);

                models.put("ModelCuadre", modelCuadre);
                models.put("ModelProducto", modelProductos);

                CuadreModel model = new CuadreModel("producto", "id_cuadre = "+id, models, id);
                CuadreView view = new CuadreView(null, true);
                new CuadreControllerHistorial(view, model);
                view.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Seleccione la fila a modificar", "Advertence", JOptionPane.WARNING_MESSAGE);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        scrollProductos.setViewportView(tablaProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(atras, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnRectificar)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(scrollCuadre, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(atras)
                                        .addComponent(btnRectificar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                                        .addComponent(scrollCuadre))
                                .addContainerGap())
        );

        setUndecorated(true);
        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}