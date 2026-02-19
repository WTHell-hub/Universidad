package Oasis.Datos;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Transferir {

    public static void Datos(String nombre, int cant, String tipo) {
        if (cant > 0) {
            Connection conn = null;
            try {
                conn = GestorBD.getConn();
                conn.setAutoCommit(false);

                int filasAfectadas;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE producto_almacen SET stock = stock - ? WHERE nombre = ? AND stock >= ?"
                )) {
                    stmt.setInt(1, cant);
                    stmt.setString(2, nombre);
                    stmt.setInt(3, cant);

                    filasAfectadas= stmt.executeUpdate();

                }

                if (filasAfectadas == 1) {

                    int verificar;
                    try (PreparedStatement stmtVerificar = conn.prepareStatement(
                            "UPDATE producto_tienda SET stock = stock + ?, tipo = ? WHERE nombre = ?"
                    )) {
                        stmtVerificar.setInt(1, cant);
                        stmtVerificar.setString(2, tipo);
                        stmtVerificar.setString(3, nombre);

                        verificar = stmtVerificar.executeUpdate();
                    }

                    if (verificar == 1) {

                        //Recoger los datos del producto transferido para tener un historial de los productos repuestos
                        try (PreparedStatement stmtSeleccionar = conn.prepareStatement(
                                "SELECT * FROM producto_almacen WHERE nombre = ?"
                        )) {
                            stmtSeleccionar.setString(1, nombre);

                            ResultSet rs = stmtSeleccionar.executeQuery();

                            if (rs.next()) {
                                double pCompra = rs.getDouble("pCompra");
                                double pVenta = rs.getDouble("pVenta");
                                List<Object> objetoRepuesto = Arrays.asList(Date.valueOf(LocalDate.now()), nombre, pCompra, pVenta, cant);

                                Guardar.en("historial_productos_repuestos", objetoRepuesto, conn);
                            }
                        }

                        conn.commit();
                    } else {

                        try (PreparedStatement stmtSeleccionar = conn.prepareStatement(
                                "SELECT * FROM producto_almacen WHERE nombre = ?"
                        )) {
                            stmtSeleccionar.setString(1, nombre);

                            ResultSet rs = stmtSeleccionar.executeQuery();

                            if (rs.next()) {
                                double pCompra = rs.getDouble("pCompra");
                                double pVenta = rs.getDouble("pVenta");
                                List<Object> objetoTienda = Arrays.asList(nombre, pCompra, pVenta, cant, tipo);
                                List<Object> objetoRepuesto = Arrays.asList(Date.valueOf(LocalDate.now()), nombre, pCompra, pVenta, cant);

                                Guardar.en("producto_tienda", objetoTienda, conn);
                                Guardar.en("historial_productos_repuestos", objetoRepuesto, conn);
                            }

                            conn.commit();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No dispones de tal cant", "Warning", JOptionPane.ERROR_MESSAGE);
                    conn.rollback();
                }

            } catch (SQLException e) {

                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                throw new RuntimeException(e);
            } finally {
                if (conn != null) {

                    try {
                        conn.setAutoCommit(true);
                    } catch (SQLException e) {
                        System.out.println("[Error] Error al re establecer el autocommit "+e.getMessage());
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Tiene q ingresar un valor real", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }
}
