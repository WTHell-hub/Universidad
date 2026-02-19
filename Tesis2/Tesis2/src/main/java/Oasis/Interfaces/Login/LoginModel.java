package Oasis.Interfaces.Login;

import Oasis.Datos.GestorBD;
import Oasis.Modelos.Usuario;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginModel {
    Usuario usuario;

    public boolean ComprobarCredenciales(String nombre, char[] clave) {
        try {
            Connection conn = GestorBD.getConn();

            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM usuarios WHERE nombre = ?");

            stmt.setString(1, nombre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String claveReal = rs.getString("password");

                if (Arrays.equals(claveReal.toCharArray(), clave)) {
                    usuario = new Usuario(nombre, claveReal);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}