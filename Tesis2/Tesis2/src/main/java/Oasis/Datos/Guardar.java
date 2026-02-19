package Oasis.Datos;

import Oasis.Modelos.ModeloTabla;
import Oasis.Modelos.ProductoBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guardar {
    public static void cuadreDelDia(ArrayList<ModeloTabla> list, double faltante, double dinero) {
        int idCuadre;
        Connection conn = null;

        try {
            conn = GestorBD.getConn();
            conn.setAutoCommit(false);

            PreparedStatement stmtCuadre = conn.prepareStatement("INSERT INTO cuadre (fecha, dinero, faltante) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmtCuadre.setDate(1, Date.valueOf(LocalDate.now()));
            stmtCuadre.setDouble(2, dinero);
            stmtCuadre.setDouble(3, faltante);
            stmtCuadre.executeUpdate();

            ResultSet rsCuadre = stmtCuadre.getGeneratedKeys();
            if (rsCuadre.next()) {
                idCuadre = rsCuadre.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el id del cuadre");
            }

            GuardarProductos(conn, list, idCuadre);
        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

            throw new RuntimeException(e);

            //ESTO ES EL CÓDIGO PARA CERRAR LA CONEXIÓN CUANDO NO SE ESTA USANDO UN TRY-WITH-RESOURCES
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la ree establecer el autocommit " + e.getMessage());
                }
            }
        }
    }

    public static void GuardarProductos(Connection conn, ArrayList<ModeloTabla> list, int idCuadre) {
        try {
            String sqlProducto = "INSERT INTO producto (nombre, stock, stock_posterior, cantidad_vendida, fecha, id_cuadre) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement stmtProducto = conn.prepareStatement(sqlProducto);

            for (ModeloTabla p: list) {
                stmtProducto.setString(1, p.getNombre());
                stmtProducto.setInt(2, p.getCuadre_anterior());
                stmtProducto.setInt(3, p.getCuadre_posterior());
                stmtProducto.setInt(4, p.getCantVendida());
                stmtProducto.setDate(5, Date.valueOf(LocalDate.now()));
                stmtProducto.setInt(6, idCuadre);
                stmtProducto.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ExecuteUpdate(ArrayList<ModeloTabla> list, double dinero, double faltante, int idCuadreHistorial) {
        Connection conn = null;
        try {
            conn = GestorBD.getConn();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtComprobar = conn.prepareStatement("UPDATE cuadre SET dinero = ?, faltante = ? WHERE id = "+idCuadreHistorial)) {

                stmtComprobar.setDouble(1, dinero);
                stmtComprobar.setDouble(2, faltante);
                int rsComprobar = stmtComprobar.executeUpdate();

                if (rsComprobar == 1) {
                    UpdateProductos(conn, list, idCuadreHistorial);
                } else {
                    throw new RuntimeException();
                }
            }

        } catch (Exception e) {
            if (conn != null) {

                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static void UpdateProductos(Connection conn, ArrayList<ModeloTabla> list, int idCuadreHistorial) {
        try {
            PreparedStatement stmtProducto = conn.prepareStatement(
                    "UPDATE producto SET stock_posterior = ?, cantidad_vendida = ? WHERE nombre = ? AND id = ?"
            );

            for (ModeloTabla p: list) {
                stmtProducto.setInt(1, p.getCuadre_posterior());
                stmtProducto.setInt(2, p.getCantVendida());
                stmtProducto.setString(3, p.getNombre());
                stmtProducto.setInt(4, idCuadreHistorial);
                stmtProducto.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {

            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            throw new RuntimeException(e);

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al re establecer el autocommit "+e.getMessage());
            }
        }
    }

    public static void usuario(String user, String password) {
        try {
            Connection conn = GestorBD.getConn();

            String sql = "INSERT INTO usuarios (nombre, password) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user);
            stmt.setString(2, password);

            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TRATAR DE HACER LA PARTE DE GUARDAR DINÁMICA
    public static void en(String donde, List<Object> valoresCampo, Connection conn) {
        try {
            //OBTENGO LOS NOMBRES DE LAS COLUMNAS DEL METADATA Q ES LA ESTRUCTURA DE LAS TABLAS EN LA BASE DE DATOS
            DatabaseMetaData meta = conn.getMetaData();
            //CON ESTO ESPECIFICAMOS Q SOLO QUEREMOS LA TABLA, SIN ESPECIFICAR VARIAS COSAS MÁS
            ResultSet rs = meta.getColumns(null, null, donde, null);

            //AQUÍ SE CREA LA LISTA EN LA Q SE GUARDARA LAS COLUMNAS Q CUMPLAN CON LOS REQUERIMIENTOS
            List<String> columnasNombre = new ArrayList<>();
            //CON ESTO RECORREMOS TODAS LAS COLUMNAS
            while (rs.next()) {
                String nombre = rs.getString("COLUMN_NAME");
                String autoincrement = rs.getString("IS_AUTOINCREMENT");

                //AQUÍ FILTRAMOS LOS Q SEAN AUTO_INCREMENT PQ ESOS VALORES LOS PONE MYSQL AUTOMÁTICAMENTE
                if (!"YES".equalsIgnoreCase(autoincrement)) {
                    columnasNombre.add(nombre);
                }
            }

            if (columnasNombre.size() != valoresCampo.size()) {
                throw new RuntimeException("Error con la cantidad de columnas ingresadas");
            }

            StringBuilder columnasSQL = new StringBuilder();
            StringBuilder valoresSQL = new StringBuilder();

            for (String s : columnasNombre) {
                columnasSQL.append(s).append(",");
                valoresSQL.append("?,");
            }

            columnasSQL.setLength(columnasSQL.length() - 1);
            valoresSQL.setLength(valoresSQL.length() - 1);

            try(PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO "+donde+" ("+columnasSQL+") VALUES ("+valoresSQL+")"
            )) {

                for (int i = 0; i < valoresCampo.size(); i++) {
                    stmt.setObject(i+1, valoresCampo.get(i));
                }

                stmt.executeUpdate();

            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}