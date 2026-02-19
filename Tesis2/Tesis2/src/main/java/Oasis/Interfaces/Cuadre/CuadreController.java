package Oasis.Interfaces.Cuadre;

import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;
import Oasis.Datos.Guardar;
import Oasis.Logica.Controladora;
import Oasis.Logica.ProductoTableModel;
import Oasis.Modelos.ModeloTabla;
import Oasis.Modelos.ProductoBase;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CuadreController {
    CuadreView view;
    CuadreModel model;

    public CuadreController(CuadreView view, CuadreModel model) {
        this.view = view;
        this.model = model;

        String[] columnas = {"Nombre", "Cantidad"};
        ProductoTableModel productoTableModel = new ProductoTableModel(model.getProductos(), columnas);
        view.getTableProductos().setModel(productoTableModel);

        //Tomamos la tabla y le aplicamos a la columna no1 el editor que creamos con las condiciones necesarias para que se pueda guardar la edición de la celda
        view.getTableProductos().getColumnModel().getColumn(1).setCellEditor(EstablecerEdicion());

        view.getBtnAceptar().addActionListener(ListenerCuadrar(model.getModels(), model.getIdCuadreHistorial()));
        view.getDinero().addActionListener(ListenerCuadrar(model.getModels(), model.getIdCuadreHistorial()));

        view.getBtnCancelar().addActionListener(e -> {
            //Refrescamos las tablas de la interface principal
            ActualizarTablasIP();

            view.dispose();
        });

        //Nos fijamos en si se cerró la ventana por alt+f4 0 de otra forma no estipulada y se cierra toda la app para que no hallan incoherencias en los datos
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Esto solo es para una función que descalifique, pero podría ser interesante
        view.getBtnModificar().setVisible(false);
    }

    //Con esto se lo paso solo a la columna 1, la cual es la de los números
    public DefaultCellEditor EstablecerEdicion() {

        return new DefaultCellEditor(new JTextField()) {
            @Override
            //Con esta función lo que se hace es detener la edición de cada celda en el momento en el que se le ordene y finalmente guarda los datos
            public boolean stopCellEditing() {
                //Se toman los datos del campo en el que se está trabajando, lo cual necesita un cast del mismo tipo que se especificó el inicio de la función en el
                //DefaultCellEditor pq la tabla necesita saber como va a tratar el campo que se está modificando y se obtiene mediante la función getComponent
                JTextField campo = (JTextField) getComponent();
                String valorIngresado = campo.getText();
                try {
                    //Se intenta convertir el tipo de dato a Integer por lo cual si falla lanza la excepción y da false para el stopCellEditor, lo cual asegura
                    //que el tipo de dato ingresado sea un entero
                    int valor = parseInt(valorIngresado);

                    //De esta forma se valida que los datos ingresados sean correctos comprobando que el número ingresado no sea negativo
                    if (valor < 0) {
                        throw new NumberFormatException();
                    }

                    //Comprueba que el número ingresado sean menor que la cantidad de objetos que había anteriormente
                    int row = view.getTableProductos().getSelectedRow();
                    String contenido = String.valueOf(view.getTableProductos().getValueAt(row, 0)).trim();
                    String nombre = contenido.substring(0, contenido.indexOf('-')).trim();

                    for (ProductoBase p: model.getProductos()) {
                        if (p.getName().equals(nombre)) {
                            if (p.getStock() < valor) {
                                throw new NumberFormatException();
                            }
                        }
                    }

                    //Se encarga de mostrar visualmente que la celda está bien devolviéndola a su color inicial en caso de que se haya puesto rojo
                    campo.setBorder(new LineBorder(Color.GRAY, 1));
                    return super.stopCellEditing();

                } catch (NumberFormatException e) {
                    campo.setBorder(new LineBorder(Color.RED, 1));
                    campo.requestFocus();
                    return false;
                }
            }
        };
    }

    public ActionListener ListenerCuadrar(Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {

        return (e -> {

            if (!Validaciones()) {
                return;
            }

            if (!GuardarCuadre(models, idCuadreHistorial)) {
                return;
            }

            view.dispose();
        });
    }

    public boolean Validaciones() {
        //Comprobar el dia del ultimo cuadre si es el mismo del de hoy
        if (SeHizoCuadre()) {
            return false;
        }

        //Se para la edición para que no quede la última celda para que no de error
        if (view.getTableProductos().isEditing()) {
            view.getTableProductos().getCellEditor().stopCellEditing();
        }

        if (dinero() < 0) {
            return false;
        }

        return true;
    }

    public double dinero() {
        try {
            int dinero = Integer.parseInt(view.getDinero().getText().trim());

            if (dinero < 0) {
                throw new NumberFormatException();
            }

            return dinero;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Verifica el numero que ingresó en la celda del dinero", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        return -1;
    }

    //Ya la pase
    public boolean SeHizoCuadre() {

        //Para que ser realice un solo cuadre se toma la última entrada de la tabla del cuadre y se verifica su fecha
        try {
            Connection conn = GestorBD.getConn();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cuadre ORDER BY id DESC LIMIT 1");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalDate lastDate = rs.getDate("fecha").toLocalDate();

                if (lastDate.equals(LocalDate.now())) {
                    JOptionPane.showMessageDialog(view, "Ya se hizo el cuadre de hoy", "Warning", JOptionPane.ERROR_MESSAGE);
                    view.dispose();
                    return true;
                } else {
                    return false;
                }
            } else {
                //Para el caso en el que la base de datos este en blanco
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Fallo en la búsqueda de la ultima fecha "+ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public boolean GuardarCuadre(Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {
        ArrayList<ModeloTabla> modeloAGuardar = new ArrayList<>();

        double dineroTotal;
        double dineroEsperado = 0;
        double faltante;

        //Se asegura que la celda del dinero este llena correctamente gracias a que si no se puede convertir a un integer es inválida la entrada
        try {
            dineroTotal = dinero();

            int cantVendida;

            for (ProductoBase p: model.getProductos()) {
                cantVendida = (int) (p.getStock() - p.getCantidad());

                dineroEsperado += cantVendida * p.getPrecioV();

                modeloAGuardar.add(new ModeloTabla(p.getName(), (int) p.getStock(), p.getCantidad(), cantVendida));
            }

            faltante = dineroEsperado - dineroTotal;

            ModoGuardado(modeloAGuardar, faltante, dineroTotal, idCuadreHistorial);

            ActualizarDB(models, idCuadreHistorial);

            ActualizarTablasIP();

            JOptionPane.showMessageDialog(view, "Cuadre guardado", "Message", JOptionPane.INFORMATION_MESSAGE);

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(view, "Error en el procedimiento", "Warning", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
//            return false;
        }
        return true;
    }

    public void ModoGuardado(ArrayList<ModeloTabla> modeloAGuardar, double faltante, double dineroTotal, Integer idCuadreHistorial) {
        Guardar.cuadreDelDia(modeloAGuardar, faltante, dineroTotal);
    }

    public void ActualizarDB(Map<String, DefaultTableModel> models, Integer idCuadreHistorial) {
        for (ProductoBase p : model.getProductos()) {
            Controladora.UpdateAfterCuadre(p.getName(), p.getCantidad());
        }
    }

    public void ActualizarTablasIP() {
        //Refrescamos las tablas de la interface principal
        Actualizar.modeloTabla(model.getModels().get("modelObjeto"),  "producto_tienda", "tipo != 'CigarroSuelto'");
        Actualizar.modeloTabla(model.getModels().get("modelPocosObjetos"),  "producto_tienda", " stock <= 20 AND tipo != 'CigarroSuelto'");
    }
}