package Oasis.Logica;

import Oasis.Datos.Actualizar;
import Oasis.Datos.GestorBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Baja {

    public Baja(DefaultTableModel model, String dataBase, int id) {
        try {
            Connection conn = GestorBD.getConn();

            //SE ENCARGA DE BORRAR EL OBJETO DEL ALMACÉN
            PreparedStatement stmtBorrar = conn.prepareStatement(
                    "DELETE FROM "+dataBase+" WHERE id = ?"
            );

            stmtBorrar.setInt(1, id);

            //SE ENCARGA DE VERIFICAR SI SE BORRO ALGÚN ELEMENTO EN CASO CONTRARIO SACA UN ERROR
            int filasAfectadas = stmtBorrar.executeUpdate();

            if (filasAfectadas == 0) {
                JOptionPane.showMessageDialog(null, "Producto no encontrado", "Warning", JOptionPane.ERROR_MESSAGE);
            } else {
                //Actualiza la tala luego de borrar
                Actualizar.modeloTabla(model, dataBase);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Baja(DefaultTableModel model, String dataBase, String condicion, int id) {
        try {
            Connection conn = GestorBD.getConn();

            //SE ENCARGA DE BORRAR EL OBJETO DEL ALMACÉN
            PreparedStatement stmtBorrar = conn.prepareStatement(
                    "DELETE FROM "+dataBase+" WHERE id = ?"
            );

            stmtBorrar.setInt(1, id);

            //SE ENCARGA DE VERIFICAR SI SE BORRO ALGÚN ELEMENTO EN CASO CONTRARIO SACA UN ERROR
            int filasAfectadas = stmtBorrar.executeUpdate();

            if (filasAfectadas == 0) {
                JOptionPane.showMessageDialog(null, "Producto no encontrado", "Warning", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Successful", "Notificación", JOptionPane.INFORMATION_MESSAGE);
            }

            //Actualiza la tala luego de borrar
            Actualizar.modeloTabla(model, dataBase, condicion);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

