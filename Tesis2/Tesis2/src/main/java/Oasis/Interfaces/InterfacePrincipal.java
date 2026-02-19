package Oasis.Interfaces;

import Oasis.Interfaces.Cuadre.CuadreController;
import Oasis.Interfaces.Cuadre.CuadreModel;
import Oasis.Interfaces.Cuadre.CuadreView;
import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;
import Oasis.Interfaces.Login.LoginController;
import Oasis.Interfaces.Login.LoginModel;
import Oasis.Interfaces.Login.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InterfacePrincipal extends JFrame {

    public InterfacePrincipal() {
        JScrollPane scrollTablaObjetos = new JScrollPane();
        String[] columnas = {"id", "Name","Precio de compra", "Precio de venta", "Stock"};
        DefaultTableModel modelObjeto = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        JTable tablaObjetos = new JTable(modelObjeto);
        JLabel titulo = new JLabel();
        JScrollPane scrollTablaObjetosFaltantes = new JScrollPane();
        DefaultTableModel modelPocosObjetos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        JTable tablaPocosObjetos = new JTable(modelPocosObjetos);
        JLabel titulo2 = new JLabel();
        JButton btnCuadrar = new JButton();
        JButton btnReponer = new JButton();
        JButton btnEstadisticas = new JButton();
        JButton btnCerrarSeccion = new JButton();
        JButton btnAlmacen = new JButton();
        JButton btnHistorial = new JButton();
        JButton btnProductos0 = new JButton();
        JButton btnSalir = new JButton();

        //Se llenan las tablas con los valores en la db con sus respectivas condiciones
        Actualizar.modeloTabla(modelObjeto, "producto_tienda", "tipo != 'CigarroSuelto'");
        Actualizar.modeloTabla(modelPocosObjetos, "producto_tienda", " stock <= 20 AND tipo != 'CigarroSuelto'");

        scrollTablaObjetos.setViewportView(tablaObjetos);
        scrollTablaObjetosFaltantes.setViewportView(tablaPocosObjetos);

        titulo.setFont(new Font("Bell MT", Font.BOLD | Font.ITALIC, 48)); // NOI18N
        titulo.setText("Oasis's Coffee");

        titulo2.setFont(new Font("Arial Narrow", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        titulo2.setText("Objetos en falta:");

        //Esto es para mandar los DefaultTableModel para las ventanas segundarias para que cuando hagan los cambios actualicen la ventana principal
        Map<String, DefaultTableModel> modelMap = new HashMap<>();
        modelMap.put("modelObjeto", modelObjeto);
        modelMap.put("modelPocosObjetos", modelPocosObjetos);

        btnCuadrar.setText("Cuadre");
        btnCuadrar.addActionListener(e -> {
            CuadreModel model = new CuadreModel("producto_tienda", "id >= 0", modelMap, null);
            CuadreView view = new CuadreView(this, true);
            new CuadreController(view, model);
            view.setVisible(true);
        });

        btnReponer.setText("Reponer");
        btnReponer.addActionListener(e -> new Reponer(modelMap, this, true));

        btnEstadisticas.setText("Estadísticas");
        btnEstadisticas.addActionListener(e -> new IEstadisticas(this, true));

        btnAlmacen.setText("Almacén");
        btnAlmacen.addActionListener(e -> new IAdministrarDatos("producto_almacen", "Almacén", this, true));

        btnHistorial.setText("Historial");
        btnHistorial.addActionListener(e -> new Historial(modelMap, this, true));

        btnProductos0.setText("Productos 0");
        btnProductos0.addActionListener(e -> new IProductoOff(modelMap, this, true));

        btnCerrarSeccion.setText("Cerrar Sección");
        btnCerrarSeccion.addActionListener(e -> {

            LoginModel model = new LoginModel();
            LoginView view = new LoginView();
            new LoginController(model, view);

            GestorBD.cerrar();
            dispose();
            view.setVisible(true);
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Seguro que quiere salir?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            GestorBD.cerrar();

            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (c == KeyEvent.VK_E) {
                    btnEstadisticas.requestFocus();
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap(122, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(titulo2)
                                                        .addComponent(btnCerrarSeccion)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addComponent(btnReponer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnCuadrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(btnAlmacen)
                                                        .addComponent(btnEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnProductos0)
                                                        .addComponent(btnSalir)
                                                        .addComponent(btnHistorial)))
                                        .addComponent(scrollTablaObjetosFaltantes, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scrollTablaObjetos, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(titulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCuadrar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReponer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHistorial)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProductos0)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEstadisticas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCerrarSeccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSalir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(titulo2)
                                .addGap(18, 18, 18)
                                .addComponent(scrollTablaObjetosFaltantes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(scrollTablaObjetos, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
        );

        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
