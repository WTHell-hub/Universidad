package Oasis.Logica;

import Oasis.Datos.*;
import Oasis.Interfaces.*;
import Oasis.Interfaces.Login.LoginController;
import Oasis.Interfaces.Login.LoginModel;
import Oasis.Interfaces.Login.LoginView;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controladora {

    public static void load() {
        try {
            Connection conn = GestorBD.getConn();

            PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM usuarios");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                LoginModel model = new LoginModel();
                LoginView view = new LoginView();
                new LoginController(model, view);
                view.setVisible(true);

            } else {
                new CrearUsuario();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void UpdateAfterCuadre(String name, int cantidad) {
        try {
            Connection conn = GestorBD.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE producto_tienda SET stock = ? WHERE nombre = ?"
            )) {
                stmt.setInt(1, cantidad);
                stmt.setString(2, name);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void UpdateProductos(String name, int cantidad, int idCuadre) {
        try {
            Connection conn = GestorBD.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE producto SET stock_posterior = ? WHERE nombre = ? AND id_cuadre = "+idCuadre
            )) {
                stmt.setInt(1, cantidad);
                stmt.setString(2, name);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar FlatDarkLaf");
        }

        Controladora.load();
    }
}