package Oasis.Interfaces;

import Oasis.Datos.Actualizar;
import Oasis.Datos.Transferir;
import Oasis.Logica.Ordenar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class Reponer extends JDialog {
    private boolean guardar = false;

    public Reponer(Map<String, DefaultTableModel> models, JFrame parent, boolean bloquear) {
        super(parent, bloquear);
        ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
        JButton atras = new javax.swing.JButton();
        JScrollPane scrollPaneAlmacen = new javax.swing.JScrollPane();
        String[] columnasAlmacen = {"Nombre","Precio de compra", "Precio de venta", "Stock"};
        DefaultTableModel modeloAlmacen = new DefaultTableModel(columnasAlmacen, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaAlmacen = new javax.swing.JTable(modeloAlmacen);
        JTextField cantidad = new javax.swing.JTextField();
        JLabel txtIndicador = new javax.swing.JLabel();
        JButton btnAceptar = new javax.swing.JButton();
        JLabel txtAlmacen = new javax.swing.JLabel();
        JLabel txtTransferir = new javax.swing.JLabel();
        JRadioButton RLiquidos = new javax.swing.JRadioButton();
        JRadioButton RConfituras = new javax.swing.JRadioButton();
        JRadioButton RLacteosHelados = new javax.swing.JRadioButton();
        JScrollPane ScrollTienda = new javax.swing.JScrollPane();
        JPanel panelPrincipal = new javax.swing.JPanel();
        JLabel txtLiquidos = new javax.swing.JLabel();
        JScrollPane ScrollLiquidos = new javax.swing.JScrollPane();
        String[] columnas = {"id", "Name","Precio de compra", "Precio de venta", "Stock"};
        DefaultTableModel modeloLiquido = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableLiquidos = new javax.swing.JTable(modeloLiquido);
        JScrollPane ScrollConfituras = new javax.swing.JScrollPane();
        DefaultTableModel modeloConfituras = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableConfituras = new javax.swing.JTable(modeloConfituras);
        JLabel txtConfituras = new javax.swing.JLabel();
        JScrollPane ScrollLacteosHelados = new javax.swing.JScrollPane();
        DefaultTableModel modeloLacteosHelados = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableLacteosHelados = new javax.swing.JTable(modeloLacteosHelados);
        JScrollPane ScrollHogar = new javax.swing.JScrollPane();
        DefaultTableModel modeloHogar = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableHogar = new javax.swing.JTable(modeloHogar);
        JScrollPane ScrollCigarrosTabaco = new javax.swing.JScrollPane();
        DefaultTableModel modeloCigarroTabaco = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableCigarrosTabaco = new javax.swing.JTable(modeloCigarroTabaco);
        JLabel txtCigarroTabaco = new javax.swing.JLabel();
        JLabel txtHogar = new javax.swing.JLabel();
        JLabel txtLacteosHelados = new javax.swing.JLabel();
        JRadioButton RHogar = new javax.swing.JRadioButton();
        JRadioButton RCigarrosTabacos = new javax.swing.JRadioButton();

        //Actualizamos la lista del almacén de forma que esté ordenado alfabéticamente
        Ordenar.tabla("producto_almacen", modeloAlmacen);
        //Aquí actualizamos las demás tablas de forma indistinta, ya que no necesitamos tenerlas ordenadas
        Actualizar.modeloTabla(modeloLiquido, "producto_tienda", "stock > 0 AND tipo = 'Liquidos'");
        Actualizar.modeloTabla(modeloHogar, "producto_tienda", "stock > 0 AND tipo = 'Hogar'");
        Actualizar.modeloTabla(modeloConfituras,"producto_tienda", "stock > 0 AND tipo = 'Confituras'");
        Actualizar.modeloTabla(modeloLacteosHelados,"producto_tienda", "stock > 0 AND tipo = 'Helados'");
        Actualizar.modeloTabla(modeloCigarroTabaco,"producto_tienda", "stock > 0 AND tipo = 'Cigarros'");

        //Agregamos cada RButton al grupo para que solo uno este seleccionado siempre teniendo uno seleccionado, siendo RLiquidos el default
        RLiquidos.setSelected(true);
        buttonGroup.add(RHogar);
        buttonGroup.add(RCigarrosTabacos);
        buttonGroup.add(RConfituras);
        buttonGroup.add(RLiquidos);
        buttonGroup.add(RLacteosHelados);

        ActionListener transferir = (e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                int fila = tablaAlmacen.getSelectedRow();

                if (fila > -1) {
                    int column = tablaAlmacen.getColumnModel().getColumnIndex("Nombre");
                    String nombre = (String) tablaAlmacen.getValueAt(fila, column);

                    //Verificamos q lo q se agrego fue un número
                    try {
                        int cantProductos = Integer.parseInt(cantidad.getText().trim());

                        //Validación, no puede esta vacío el campo, tiene que ser un número y tiene que ser mayor que 0
                        if (!cantidad.getText().trim().isEmpty() && cantProductos > 0) {

                            HashMap<ButtonModel, String> mapa = new HashMap<>();
                            mapa.put(RHogar.getModel(), "Hogar");
                            mapa.put(RCigarrosTabacos.getModel(), "Cigarros");
                            mapa.put(RConfituras.getModel(), "Confituras");
                            mapa.put(RLiquidos.getModel(), "Liquidos");
                            mapa.put(RLacteosHelados.getModel(), "Helados");

                            String tipo = mapa.get(buttonGroup.getSelection());

                            Transferir.Datos(nombre, cantProductos, tipo);
                        } else {
                            JOptionPane.showMessageDialog(null, "Debe especificar una cantidad real", "Warning", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Cantidad a transferir invalida", "Warning", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un objeto", "Warning", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }

            @Override
            protected void done() {
                // Esto se ejecuta en el hilo de interfaz, después de que doInBackground termina
                cantidad.setText("");

                Ordenar.tabla("producto_almacen", modeloAlmacen);
                Actualizar.modeloTabla(modeloLiquido, "producto_tienda", "stock > 0 AND tipo = 'Liquidos'");
                Actualizar.modeloTabla(modeloHogar, "producto_tienda", "stock > 0 AND tipo = 'Hogar'");
                Actualizar.modeloTabla(modeloConfituras,"producto_tienda", "stock > 0 AND tipo = 'Confituras'");
                Actualizar.modeloTabla(modeloLacteosHelados,"producto_tienda", "stock > 0 AND tipo = 'Helados'");
                Actualizar.modeloTabla(modeloCigarroTabaco,"producto_tienda", "stock > 0 AND tipo = 'Cigarros'");

                guardar = true;
            }
        }.execute());

        atras.setText("<-");
        atras.addActionListener(e -> {
            if (guardar) {
                //Refrescamos las tablas de la interface principal
                Actualizar.modeloTabla(models.get("modelObjeto"),  "producto_tienda", "tipo != 'CigarroSuelto'");
                Actualizar.modeloTabla(models.get("modelPocosObjetos"),  "producto_tienda", " stock <= 20 AND tipo != 'CigarroSuelto'");
            }
            dispose();
        });

        //Nos fijamos en si se cerró la ventana por alt+f4 0 de otra forma no estipulada y se cierra toda la app para que no hallan incoherencias en los datos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        scrollPaneAlmacen.setViewportView(tablaAlmacen);

        txtIndicador.setText("->");

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(transferir);
        cantidad.addActionListener(transferir);

        txtAlmacen.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtAlmacen.setText("Almacén");

        txtTransferir.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 14)); // NOI18N
        txtTransferir.setText("Transferir");

        buttonGroup.add(RLiquidos);
        RLiquidos.setText("Líquidos");

        buttonGroup.add(RConfituras);
        RConfituras.setText("Confituras");

        buttonGroup.add(RLacteosHelados);
        RLacteosHelados.setText("Lácteos y Helados");

        txtLiquidos.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtLiquidos.setText("Líquidos");

        ScrollLiquidos.setViewportView(tableLiquidos);

        ScrollConfituras.setViewportView(tableConfituras);

        txtConfituras.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtConfituras.setText("Confituras");

        ScrollLacteosHelados.setViewportView(tableLacteosHelados);

        ScrollHogar.setViewportView(tableHogar);

        ScrollCigarrosTabaco.setViewportView(tableCigarrosTabaco);

        txtCigarroTabaco.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtCigarroTabaco.setText("Cigarros y Tabaco");

        txtHogar.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtHogar.setText("Hogar");

        txtLacteosHelados.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 18)); // NOI18N
        txtLacteosHelados.setText("Lácteos y Helados");

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
                panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addGap(165, 165, 165)
                                .addComponent(txtLiquidos, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtConfituras, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138))
                        .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ScrollLacteosHelados, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                                        .addComponent(ScrollLiquidos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(15, 15, 15)
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ScrollHogar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(ScrollConfituras, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrincipalLayout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(txtLacteosHelados, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtHogar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(181, 181, 181))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrincipalLayout.createSequentialGroup()
                                .addGap(164, 164, 164)
                                .addComponent(ScrollCigarrosTabaco, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                                .addGap(151, 151, 151))
                        .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addGap(328, 328, 328)
                                .addComponent(txtCigarroTabaco, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPrincipalLayout.setVerticalGroup(
                panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPrincipalLayout.createSequentialGroup()
                                                .addComponent(txtConfituras)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ScrollConfituras, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelPrincipalLayout.createSequentialGroup()
                                                .addComponent(txtLiquidos)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ScrollLiquidos, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtHogar)
                                        .addComponent(txtLacteosHelados))
                                .addGap(12, 12, 12)
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ScrollLacteosHelados, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                                        .addComponent(ScrollHogar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(7, 7, 7)
                                .addComponent(txtCigarroTabaco)
                                .addGap(12, 12, 12)
                                .addComponent(ScrollCigarrosTabaco, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ScrollTienda.setViewportView(panelPrincipal);

        buttonGroup.add(RHogar);
        RHogar.setText("Hogar");

        buttonGroup.add(RCigarrosTabacos);
        RCigarrosTabacos.setText("Cigarros y Tabaco");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(atras, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(82, 82, 82)
                                                .addComponent(txtAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(scrollPaneAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(22, 22, 22)
                                                                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtIndicador)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnAceptar))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(50, 50, 50)
                                                                .addComponent(txtTransferir, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(39, 39, 39)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(RLiquidos, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(RConfituras, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(RLacteosHelados, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                                                        .addComponent(RHogar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(RCigarrosTabacos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ScrollTienda, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)))
                                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(268, 268, 268)
                                .addComponent(txtTransferir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtIndicador)
                                        .addComponent(btnAceptar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RLiquidos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RConfituras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RLacteosHelados)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RHogar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RCigarrosTabacos)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(atras)
                                        .addComponent(txtAlmacen))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(scrollPaneAlmacen)
                                                .addContainerGap())
                                        .addComponent(ScrollTienda, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)))
        );

        setUndecorated(true);
        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dimension);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

