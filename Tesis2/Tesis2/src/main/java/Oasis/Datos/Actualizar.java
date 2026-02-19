package Oasis.Datos;

import Oasis.Modelos.ProductoBase;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actualizar {
    public static void modeloTabla(DefaultTableModel model, String donde) {
        if (model != null) {
            try {
                Connection conn = GestorBD.getConn();

                try(PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM "+donde
                )) {
                    model.setRowCount(0);

                    ResultSet rs = stmt.executeQuery();
                    ResultSetMetaData metadata = rs.getMetaData();
                    int columnas = metadata.getColumnCount();

                    while (rs.next()) {
                        Object[] fila = new Object[columnas];
                        for (int i = 1; i <= columnas; i++) {
                            fila[i-1] = rs.getObject(i);
                        }
                        model.addRow(fila);
                    }

                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar los modelos del historial", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modeloTabla(DefaultTableModel model, String donde, String condicion) {
        try {
            Connection conn = GestorBD.getConn();

            try(PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM "+donde+" WHERE "+condicion
            )) {
                model.setRowCount(0);

                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData metadata = rs.getMetaData();
                int columnas = metadata.getColumnCount();
                boolean tieneTipo = false;

                for (int i = 1; i < columnas; i++) {
                    if (metadata.getColumnName(i).equalsIgnoreCase("tipo")) {
                        tieneTipo = true;
                        break;
                    }
                }

                Object[] fila = new Object[columnas];
                ArrayList<Object[]> filaCigarro = new ArrayList<>();

                while (rs.next()) {
                    if (tieneTipo && rs.getString("tipo").equalsIgnoreCase("CigarroSuelto")) {
                        Object[] aux = new Object[columnas];
                        for (int i = 1; i <= columnas; i++) {
                            aux[i-1] = rs.getObject(i);
                        }
                        filaCigarro.add(aux);
                    } else {
                        for (int i = 1; i <= columnas; i++) {
                            fila[i-1] = rs.getObject(i);
                        }
                        model.addRow(fila);
                    }
                }

                //Se ordena de manera que se obtiene por pares con la condicion de que no sea nulo en el proceso, si no es nulo obtiene los valores del elemento
                //1.º y el 2.º y los compara como string
                if (!filaCigarro.isEmpty()) {
                    filaCigarro.sort((a, b) -> {
                        String nombreA = a[1] == null ? "" : a[1].toString();
                        String nombreB = b[1] == null ? "" : b[1].toString();
                        return nombreA.compareToIgnoreCase(nombreB);
                    });
                    for (Object[] o: filaCigarro) {
                        model.addRow(o);
                    }
                }

            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void lista(ArrayList<ProductoBase> lista, String donde) {
        lista.clear();
        try {
            Connection conn = GestorBD.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM "+donde
            )) {
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int column = meta.getColumnCount();

                ArrayList<ProductoBase> cigarros = new ArrayList<>();
                ArrayList<ProductoBase> cigarroSueltos = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Integer> columnas = new HashMap<>();
                    for (int i = 1; i <= column; i++) {
                        columnas.put(meta.getColumnName(i), i);
                    }

                    //Hay error aquí con el nombre, no se está pasando creo no he revisado bien
                    String name = columnas.containsKey("nombre") ? rs.getString("nombre") : "";
                    double precioC = columnas.containsKey("pCompra") ? rs.getDouble("pCompra") : 0.0;
                    double precioV = columnas.containsKey("pVenta") ? rs.getDouble("pVenta") : 0.0;
                    int stock = columnas.containsKey("stock") ? rs.getInt("stock") : 0;
                    String tipo = columnas.containsKey("tipo") ? rs.getString("tipo") : "";

                    switch (tipo) {
                        case "Cigarros": {cigarros.add(new ProductoBase(name, precioC, precioV, stock, tipo)); break;}
                        case "CigarroSuelto": {cigarroSueltos.add(new ProductoBase(name, precioC, precioV, stock, tipo)); break;}
                        default: lista.add(new ProductoBase(name, precioC, precioV, stock, tipo));
                    }
                }

                for (ProductoBase p: cigarros) {
                    lista.add(new ProductoBase(p.getName(), p.getPrecioC(), p.getPrecioV(), p.getStock(), p.getTipo()));
                }

                for (ProductoBase p: cigarroSueltos) {
                    lista.add(new ProductoBase(p.getName(), p.getPrecioC(), p.getPrecioV(), p.getStock(), p.getTipo()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void lista(ArrayList<ProductoBase> lista, String donde, String condicion) {
        lista.clear();
        try {
            Connection conn = GestorBD.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM "+donde+" WHERE "+condicion
            )) {
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int column = meta.getColumnCount();
                List<ProductoBase> cigarrosAlFinal = new ArrayList<>();

                while (rs.next()) {

                    Map<String, Integer> columnas = new HashMap<>();
                    for (int i = 1; i <= column; i++) {
                        columnas.put(meta.getColumnName(i), i);
                    }

                    //Derive haber código para hacer que funcione para cualquier tabla, desarrolla más adelante
                    //Use el Map para el objetivo de arriba, puede ser directamente con el rs.get...
                    //De esta forma por lo menos garantizo que se pasen el nombre y el stock que es lo único que comparten mis tablas, reformar más adelante

                    String name = columnas.containsKey("nombre") ? rs.getString("nombre") : "";
                    double precioC = columnas.containsKey("pCompra") ? rs.getDouble("pCompra") : 0.0;
                    double precioV = columnas.containsKey("pVenta") ? rs.getDouble("pVenta") : 0.0;
                    int stock = columnas.containsKey("stock") ? rs.getInt("stock") : 0;
                    String tipo = columnas.containsKey("tipo") ? rs.getString("tipo") : "asd";
                    int cantidad = columnas.containsKey("stock_posterior") ? rs.getInt("stock_posterior") : 0;

                    if (tipo.equalsIgnoreCase("CigarroSuelto")) {
                        cigarrosAlFinal.add(new ProductoBase(name, precioC, precioV, stock, tipo));
                    } else {
                        if (columnas.containsKey("stock_posterior")) {
                            lista.add(new ProductoBase(name, precioC, precioV, stock, tipo, cantidad));
                        } else {
                            lista.add(new ProductoBase(name, precioC, precioV, stock, tipo));
                        }
                    }
                }
                lista.addAll(cigarrosAlFinal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
