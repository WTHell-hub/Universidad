package Oasis.Interfaces.Login;

import Oasis.Datos.GestorBD;
import Oasis.Interfaces.InterfacePrincipal;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginController {
    private final LoginModel model;
    private final LoginView view;
    
    public LoginController(LoginModel model, LoginView view) {
        this.model = model;
        this.view = view;

        this.view.getBtnAceptar().addActionListener(accion());
        this.view.getPassword().addActionListener(accion());

        this.view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GestorBD.cerrar();
            }
        });
    }

    public ActionListener accion() {
        return e -> {
            String user = view.getNombre().getText().trim();
            char[] password = view.getPassword().getPassword();

            if (model.ComprobarCredenciales(user, password)) {
                new InterfacePrincipal();
                view.dispose();
            } else {
                view.getNombre().setText("");
                view.getPassword().setText("");
                view.getNombre().requestFocusInWindow();
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}